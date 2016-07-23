package org.xeroserver.x0library.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.xeroserver.x0library.log.Logger;

public class SingleUDP implements Runnable {

	// Loggerblock
	private Logger logger = new Logger();

	public Logger getLogger() {
		return this.logger;
	}
	// -----------

	private Thread run, send, receive;
	private int serverport;
	private DatagramSocket server;
	private boolean running = false;

	private boolean connectionSet = false;
	private InetAddress remoteAddress;
	private int remotePort;

	private ArrayList<PacketReceiver> receivers = new ArrayList<PacketReceiver>();

	public SingleUDP(int serverport) {

		this.serverport = serverport;

		run = new Thread(this, "SimpleUDP_" + serverport);

		run.start();

	}

	@Override
	public void run() {

		try {

			server = new DatagramSocket(serverport);

		} catch (Exception e) {
			logger.fatal("Couldn't start the server!");
			e.printStackTrace();
			return;
		}

		running = true;

		startReceiveThread();

	}

	public void registerReceiver(PacketReceiver rec) {
		this.receivers.add(rec);
	}

	private void startReceiveThread() {
		receive = new Thread("SimpleUDP_Receive_" + serverport) {
			public void run() {
				while (running) {
					byte[] data = new byte[1014];
					DatagramPacket packet = new DatagramPacket(data, data.length);

					try {
						server.receive(packet);
					} catch (Exception e) {
						logger.error("Error receiving a packet!");
					}

					Packet p = reconstructPacket(packet.getData());
					processPacket(p);
				}
			}
		};

		receive.start();
	}

	private void processPacket(Packet p) {

		for (PacketReceiver r : receivers) {
			r.recievePacket(p);
		}
	}

	public void setConnection(String address, int port) {
		try {
			remoteAddress = InetAddress.getByName(address);
			remotePort = port;

		} catch (UnknownHostException e) {
			logger.error("Invalid address/port!");
			e.printStackTrace();
		}

		this.connectionSet = true;
	}

	public void disconnect() {
		this.connectionSet = false;
	}

	public void stop() {
		running = false;
	}

	public void send(Packet p) {
		if (!connectionSet || !running) {
			logger.error("Can't send packet with no connection information! Is the server even running?");
			return;
		}

		byte[] data = constructPacket(p);

		send = new Thread("SimpleUDP_SEND_" + serverport) {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, remoteAddress, remotePort);

				try {
					server.send(packet);
				} catch (IOException e) {
					logger.error("Couldn't send packet!");
					e.printStackTrace();
				}

			}
		};

		send.start();
	}

	private Packet reconstructPacket(byte[] data) {
		Packet p = null;

		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			p = (Packet) in.readObject();

		} catch (ClassNotFoundException e) {
			logger.error("Error reconstructing packet!");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Error reconstructing packet!");
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException ex) {
				logger.error("Error reconstructing packet!");
				ex.printStackTrace();
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				logger.error("Error reconstructing packet!");
				ex.printStackTrace();
			}
		}

		return p;

	}

	private byte[] constructPacket(Packet p) {

		byte[] yourBytes = null;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(p);
			yourBytes = bos.toByteArray();

		} catch (IOException e) {
			logger.error("Error constructing packet!");
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				logger.error("Error constructing packet!");
				ex.printStackTrace();
			}
			try {
				bos.close();
			} catch (IOException ex) {
				logger.error("Error constructing packet!");
				ex.printStackTrace();
			}
		}

		return yourBytes;

	}

	public interface PacketReceiver {

		public void recievePacket(Packet p);

	}

}
