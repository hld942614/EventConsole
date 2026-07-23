//package com.project.uhdbackend.socket;
//
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.util.Arrays;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.project.uhdbackend.controller.UHDConsoleController;
//import com.project.uhdbackend.entity.UHDConsoleMessage;
//
//@Component
//public class SocketServer extends Thread {
//
//	@Autowired
//	private UHDConsoleController uhdConsoleController;
//
//	public void run() {
//		System.out.println("伺服器已啟動 !");
//		// DatagramSocket (listen on specific port)
//		DatagramSocket socket = null;
//		try {
//			socket = new DatagramSocket(8765);
//			while (true) {
//				// Buffer to hold received data
//				byte[] data = new byte[1024];
//
//				// Create DatagramPacket to receive data
//				DatagramPacket packet = new DatagramPacket(data, data.length);
//
//				// Receive the message (waits for a message)
//				socket.receive(packet);
//
//				// Get sender's information
//				InetAddress senderAddress = packet.getAddress();
//				int senderPort = packet.getPort();
//
//				// Convert received data to string (assuming it's text)
//				String message = new String(packet.getData(), 0, packet.getLength());
//				saveMsg(message);
//
//				System.out.println("Received message from " + senderAddress + ":" + senderPort);
//				System.out.println("Message: " + message);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (!socket.isClosed()) {
//				socket.close();
//			}
//		}
//
//	}
//
//	public void saveMsg(String message) {
//		String[] msgArray = message.split("\\|");
//		System.out.println(Arrays.toString(msgArray));
//		String eventId = msgArray[1];
//		String eventMsg = msgArray[2];
//		String sender = msgArray[3];
//		UHDConsoleMessage uhdMsg = new UHDConsoleMessage();
//		uhdMsg.setEventId(eventId);
//		uhdMsg.setEventMessage(eventMsg);
//		uhdMsg.setSender(sender);
//		uhdConsoleController.saveUHDConsoleMessage(uhdMsg);
//	}
//}
