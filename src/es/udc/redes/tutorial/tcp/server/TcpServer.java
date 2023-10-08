package es.udc.redes.tutorial.tcp.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/** Multithread TCP echo server. */

public class TcpServer {

  public static void main(String argv[]) throws IOException {
    if (argv.length != 1) {
      System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
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
        Socket client = socket.accept();
        // Create a ServerThread object, with the new connection as parameter
        ServerThread server = new ServerThread(client);
        // Initiate thread using the start() method
        server.start();
      }
    // Uncomment next catch clause after implementing the logic
     } catch (SocketTimeoutException e) {
      System.err.println("Nothing received in 300 secs");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
     } finally{
      //Close the socket
      assert socket != null;
      socket.close();
    }
  }
}
