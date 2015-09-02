package org.solq.dht.test.dhtnet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import org.junit.Test;

public class TestUdp {
	static final int BROADCAST_PORT = 9999;
	static InetAddress broadcastAddress = null;

	static {
		try {
			broadcastAddress = InetAddress.getByName("230.0.0.1");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		new TestUdp().server();
	}

	@Test
	public void server() throws IOException {
		@SuppressWarnings("resource")
		MulticastSocket socket = new MulticastSocket(BROADCAST_PORT);
		socket.joinGroup(broadcastAddress);
		// 设置本MulticastSocket发送的数据报会被回送到自身
		socket.setLoopbackMode(false);

		byte[] inBuff = new byte[4096];
		DatagramPacket inPacket = new DatagramPacket(inBuff, inBuff.length);
		while (true) {
			socket.receive(inPacket);
			System.out.println("聊天信息：" + new String(inBuff, 0, inPacket.getLength()));
			System.out.println("socket addr ：" + inPacket.getAddress());
		}

		// if (socket != null) {
		// // 让该Socket离开该多点IP广播地址
		// socket.leaveGroup(broadcastAddress);
		// socket.close();
		// }
	}

	@Test
	public void client() throws IOException {
		MulticastSocket socket = new MulticastSocket(BROADCAST_PORT);
		socket.joinGroup(broadcastAddress);
		socket.setLoopbackMode(false);
		DatagramPacket outPacket = new DatagramPacket(new byte[0], 0, broadcastAddress, BROADCAST_PORT);
		outPacket.setData("abcerserser".getBytes());
		socket.send(outPacket);
		socket.leaveGroup(broadcastAddress);
		socket.close();
	}
}
