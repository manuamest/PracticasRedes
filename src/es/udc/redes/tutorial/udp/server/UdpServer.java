package es.udc.redes.tutorial.udp.server;

import java.net.*;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }
        DatagramSocket sDatagram = null;
        try {
            // Create a server socket
            int port_number = Integer.parseInt(argv[0]);
            sDatagram = new DatagramSocket(port_number);
            // Set maximum timeout to 300
            sDatagram.setSoTimeout(300000);
            while (true) {
                // Prepare datagram for reception
                byte array[] = new byte[1024];
                DatagramPacket dgramRec = new DatagramPacket(array, array.length);
                // Receive the dgramRec
                sDatagram.receive(dgramRec);
                // Prepare datagram to send response
                int portRem = dgramRec.getPort();
                InetAddress dirClient = dgramRec.getAddress();
                DatagramPacket dgramEnv = new DatagramPacket(array, array.length, dirClient, portRem);
                // Send response
                sDatagram.send(dgramEnv);
            }

            // Uncomment next catch clause after implementing the logic
        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the socket
            sDatagram.close();
        }
    }
}