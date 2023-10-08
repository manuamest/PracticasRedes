package es.udc.redes.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class WebServer {

    public static void main(String[] argv) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.webserver.WebServer <port>");
            System.exit(-1);
        }
        ServerSocket server = null;
        try {
            // Create a server socket
            server = new ServerSocket(Integer.parseInt(argv[0]));
            // Set a timeout of 300 secs
            server.setSoTimeout(300000);

            while (true) {
                // Wait for connections
                Socket socket = server.accept();
                // Create ServerThread object, with the new connection as parameter
                es.udc.redes.webserver.ServerThread thread = new ServerThread(socket);
                // Initiate thread using the start() method
                thread.start();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally{
            //Close the socket
            try {
                assert server != null;
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}