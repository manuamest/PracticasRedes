package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;


public class ServerThread extends Thread {

    es.udc.redes.webserver.State state;


    private final SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");


    private Socket socket;

    public ServerThread(Socket s) {
        // Store the socket s
        this.socket = s;
    }

    public void run() {
        String dirFile = System.getProperty("user.dir");
        BufferedReader sIn;
        OutputStream sOut;

        try {
            // Set the input channel
            sIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Set the output channel
            sOut = socket.getOutputStream();
            // Receive the message from the client
            String received = sIn.readLine();
            String header = null;
            String aux = sIn.readLine();
            if (aux != null) {
                while (!aux.equals("")) {
                    if (aux.contains("If-Modified-Since")) {
                        header = aux;
                        break;
                    } else {
                        aux = sIn.readLine();
                    }
                }
            }
            if (received != null){
                StringTokenizer token = new StringTokenizer(received);
                String metodo = token.nextToken().toUpperCase(); //Accede al primer token, corresponde a la peticion
                String fileString = token.nextToken();   //Accede al segundo token que corresponde al archivo
                Date date = new Date();
                if (metodo.equals("GET") || metodo.equals("HEAD")){
                    File fileRead = new File(dirFile + fileString);
                    File file = new File(dirFile + "/error404.html");
                    byte[] fileContent;

                    if (!fileRead.exists()) {
                        state = es.udc.redes.webserver.State.NOT_FOUND;
                        fileContent = Files.readAllBytes(file.toPath());
                        fileString = "error404.html";
                    } else {
                        state = es.udc.redes.webserver.State.OK;
                        file = new File(dirFile + fileString);//Le asigna un path al archivo solicitado
                        fileContent = Files.readAllBytes(file.toPath());
                    }
                        if (header != null && header.contains("If-Modified-Since")) {
                            String stringMod = header.substring(19);
                            Date askedDate = dateFormat.parse(stringMod);
                            Date modDate = dateFormat.parse(dateFormat.format((int) file.lastModified()));

                            if (askedDate.after(modDate) || askedDate.equals(modDate)) {
                                state = es.udc.redes.webserver.State.NOT_MODIFIED;
                            } else {
                                state = es.udc.redes.webserver.State.OK;
                                file = new File(dirFile + fileString);
                                fileContent = Files.readAllBytes(file.toPath());
                            }
                        }

                    String respuesta = "HTTP/1.0 " + state.getState() +
                            "\nDate: " + dateFormat.format(date) + "Server: WebServer_715" +
                            "\nLast-Modified: " + dateFormat.format(file.lastModified()) +
                            "\nContent-Length: " + fileContent.length + "" +
                            "\nContent-Type: " + getType(fileString)+
                            "\n" + "" + "" + "\n";

                    sOut.write(respuesta.getBytes(StandardCharsets.UTF_8));
                    if (metodo.equals("GET") && state != es.udc.redes.webserver.State.NOT_MODIFIED) {
                        sOut.write(fileContent);
                    }
                } else {
                    state = es.udc.redes.webserver.State.BAD_REQUEST;
                    File file = new File(dirFile + "/error400.html");
                    byte[] fileContent = Files.readAllBytes(file.toPath());
                    String respuesta = "HTTP/1.0 " + state.getState() +
                            "\nDate: " + dateFormat.format(date) + "Server: WebServer_715" +
                            "\nLast-Modified: " + dateFormat.format(file.lastModified()) +
                            "\nContent-Length: " + fileContent.length + "" +
                            "\nContent-Type: " + getType(fileString)+
                            "\n" + "" + "" + "\n";
                    sOut.write(respuesta.getBytes(StandardCharsets.UTF_8));
                    sOut.write(fileContent);
                }
            }
            sOut.close();
            sIn.close();
        } catch (SocketTimeoutException e) {
                System.err.println("Nothing received in 300 secs"); //Si pasan 5 minutos el programa dara error y printeara esto
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage()); //En caso de cualquier error diferente al TimeOut mandara el mensaje propio del error
        } finally {
            try {
                if (socket != null)                         //En caso de que haya socket este se cerrara
                    socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getType (String file){
        if (file.endsWith(".html") || file.endsWith(".htm"))
            return "text/html";
        if (file.endsWith(".plain") || file.endsWith(".txt"))
            return "text/plain";
        if (file.endsWith(".gif"))
            return "image/gif";
        if (file.endsWith(".png"))
            return "image/png";
        return "application/octet-stream";                   //Mensaje correspondiente a los elementos con type distinto de los anteriores
    }
}
