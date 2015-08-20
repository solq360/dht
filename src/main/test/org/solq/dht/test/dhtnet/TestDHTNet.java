package org.solq.dht.test.dhtnet;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.solq.dht.core.protocol.krpc.impl.FindNodeMessage;

//http://www.tuicool.com/articles/iAbiue
public class TestDHTNet {

	@Test
	public void server() throws IOException {
		final int port = 9999;
		DatagramChannel channel = DatagramChannel.open();
		DatagramSocket socket = channel.socket();
		channel.configureBlocking(false);
		socket.bind(new InetSocketAddress(port));

		System.out.println("start server : " + port);
		Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_READ);
 
		ByteBuffer byteBuffer = ByteBuffer.allocate(65536);
		while (true) {
			try {
				int eventsCount = selector.select();
				if (eventsCount > 0) {
					Set<SelectionKey> selectedKeys = selector.selectedKeys();
					Iterator<SelectionKey> iterator = selectedKeys.iterator();
					while (iterator.hasNext()) {
						SelectionKey sk = iterator.next();
						iterator.remove();
						if (sk.isReadable()) {
							DatagramChannel datagramChannel = (DatagramChannel) sk.channel();
							SocketAddress target = datagramChannel.receive(byteBuffer);

							byteBuffer.flip();
							byte[] dst = new byte[byteBuffer.limit()];
							byteBuffer.get(dst);
							byteBuffer.clear();
							System.out.println("address : " + target.toString());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static final Set<SocketAddress> adds = new HashSet<>();

	static {
		adds.add(new InetSocketAddress("127.0.0.1", 9999));
		adds.add(new InetSocketAddress("120.25.105.27", 9999));
		adds.add(new InetSocketAddress("router.bittorrent.com", 6881));
		adds.add(new InetSocketAddress("dht.transmissionbt.com", 6881));
		adds.add(new InetSocketAddress("router.utorrent.com", 6881));		
	}

	static void bootstrapJoinDHT(DatagramChannel channel) {
		adds.forEach(add -> {
			try {
				byte[] message = FindNodeMessage.ofRequest().toRequestMessage();
				channel.send(ByteBuffer.wrap(message), add);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Test
	public void client() throws IOException {
		final int port = 7777;
		DatagramChannel channel = DatagramChannel.open();
		DatagramSocket socket = channel.socket();
		channel.configureBlocking(false);
		socket.bind(new InetSocketAddress(port));

		System.out.println("start server : " + port);

		Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_READ);
		bootstrapJoinDHT(channel);
		System.out.println("join DHT net ");

		ByteBuffer byteBuffer = ByteBuffer.allocate(65536);
		while (true) {
			try {
				int eventsCount = selector.select();
				if (eventsCount > 0) {
					Set<SelectionKey> selectedKeys = selector.selectedKeys();
					Iterator<SelectionKey> iterator = selectedKeys.iterator();
					while (iterator.hasNext()) {
						SelectionKey sk = iterator.next();
						iterator.remove();
						if (sk.isReadable()) {
							DatagramChannel datagramChannel = (DatagramChannel) sk.channel();
							SocketAddress target = datagramChannel.receive(byteBuffer);

							byteBuffer.flip();
							byte[] dst = new byte[byteBuffer.limit()];
							byteBuffer.get(dst);
							byteBuffer.clear();
							System.out.println("address : " + target.toString());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
