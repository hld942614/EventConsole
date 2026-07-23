package com.project.uhdbackend.socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SocketClient {
	public static void main(String[] args) throws Exception {

//		// Message to send
//		String message = "E|34TTQD|ezFTP error|172.16.6.1234567";
//
//		// DatagramSocket (bind to any available port)
//		DatagramSocket socket = new DatagramSocket(5678);
//
//		// Convert message to byte array
//		byte[] data = message.getBytes();
//
//		// Get receiver's address (replace with target IP if necessary)
//		InetAddress receiverAddress = InetAddress.getByName("172.16.233.44");
//
//		// Specify receiver port number
//		int receiverPort = 8765;
//
//		// Create DatagramPacket
//		DatagramPacket packet = new DatagramPacket(data, data.length, receiverAddress, receiverPort);
//
//		// Send the message
//		socket.send(packet);
//
//		System.out.println("Message sent to " + receiverAddress + ":" + receiverPort);
//
//		// Close the socket (optional but recommended)
//		socket.close();
		try {
			DatagramSocket ds = new DatagramSocket(5678);

			String str = "E|34TTQD|ezFTP error|172.16.6.123456";
			byte[] buf = str.getBytes();

			InetAddress ip = InetAddress.getByName("172.16.233.44");

			DatagramPacket dp = new DatagramPacket(buf, buf.length, ip, 8765);
			ds.send(dp);
			System.out.println("Message send successfully! ");
			ds.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
