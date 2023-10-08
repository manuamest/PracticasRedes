package es.udc.redes.tutorial.tcp.server;
import java.net.*;
import java.io.*;

/** Thread that processes an echo server connection. */

public class ServerThread extends Thread {

  private final Socket socket;

  public ServerThread(Socket s) {
    // Store the socket s
    socket = s;
  }

  public void run() {
    try {
      // Set the input channel
      BufferedReader sEntrance = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      // Set the output channel
      PrintWriter sExit = new PrintWriter(socket.getOutputStream(), true);
      // Receive the message from the client
      String readLine = sEntrance.readLine();
      System.out.println("SERVER: Received " + sEntrance +
              " to " + socket.getInetAddress().toString() +
              ":" + socket.getLocalPort());
      // Sent the echo message to the client
      sExit.println(readLine);
      System.out.println("SERVER: sending " + sExit
              + " from " + socket.getInetAddress().toString()
              + ":" + socket.getLocalPort());
      // Close the streams
      sEntrance.close();
      sExit.close();
    // Uncomment next catch clause after implementing the logic
     } catch (SocketTimeoutException e) {
      System.err.println("Nothing received in 300 secs");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      } finally {
	// Close the socket
      try {
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}

