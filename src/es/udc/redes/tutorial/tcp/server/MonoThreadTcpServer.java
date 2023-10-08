package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String argv[]) throws IOException {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }
        ServerSocket socket = null;
        try {
            int port = Integer.parseInt(argv[0]);
            // Create a server socket
            socket = new ServerSocket(port);
            // Set a timeout of 300 secs
            socket.setSoTimeout(300000);            
            while (true) {
                // Wait for connections
                Socket accept = socket.accept();
                // Set the input channel
                BufferedReader sEntrance = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                // Set the output channel
                PrintWriter sExit = new PrintWriter(accept.getOutputStream(), true);
                // Receive the client message
                String readLine = sEntrance.readLine();
                System.out.println("SERVER: Received " + sEntrance +
                        " to " + socket.getInetAddress().toString() +
                        ":" + socket.getLocalPort());
                // Send response to the client
                sExit.println(readLine);
                System.out.println("SERVER: Sending " + sExit
                        + " from " + socket.getInetAddress().toString()
                        + ":" + socket.getLocalPort());
                // Close the streams
                sEntrance.close();
                sExit.close();
            }
        // Uncomment next catch clause after implementing the logic            
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
	    //Close the socket
            assert socket != null;
            socket.close();
        }
    }
}
