package org.xeroserver.x0_Library.net.SimpleUDP;

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

import org.xeroserver.x0_Library.Log.Logger;
import org.xeroserver.x0_Library.net.SimpleUDP.Packet.Packet;

public class SimpleUDP implements Runnable {

	private Thread run, send, receive;
	private int serverport;
	private DatagramSocket server;
	private boolean running = false;

	private boolean connectionSet = false;
	private InetAddress remoteAddress;
	private int remotePort;

	private ArrayList<UDPReceiver> receivers = new ArrayList<UDPReceiver>();

	public SimpleUDP(int serverport) {

		this.serverport = serverport;

		run = new Thread(this, "SimpleUDP_" + serverport);

		run.start();

	}

	@Override
	public void run() {

		try {

			server = new DatagramSocket(serverport);

		} catch (Exception e) {
			l.fatal("Couldn't start the server!");
			e.printStackTrace();
			return;
		}

		l.info("Server started!");
		running = true;

		startReceiveThread();

	}

	public void registerReceiver(UDPReceiver rec) {
		this.receivers.add(rec);
		l.info("Sucessfully registered receiver!");
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
						l.error("Error receiving a packet!");
					}

					Packet p = reconstructPacket(packet.getData());
					processPacket(p);
				}
			}
		};

		receive.start();
		l.info("Started listening on port " + serverport);
	}

	private void processPacket(Packet p) {

		for (UDPReceiver r : receivers) {
			switch (p.getPacketType()) {
			case Packet.STRING:
				r.receiveString(p.getString());
				break;
			case Packet.INT:
				r.receiveInt(p.getInt());
				break;
			case Packet.DOUBLE:
				r.receiveDouble(p.getDouble());
				break;
			case Packet.FLOAT:
				r.receiveFloat(p.getFloat());
				break;
			}
		}
	}

	public void setConnection(String address, int port) {
		try {
			remoteAddress = InetAddress.getByName(address);
			remotePort = port;

		} catch (UnknownHostException e) {
			l.error("Invalid address/port!");
			e.printStackTrace();
		}

		this.connectionSet = true;
		l.info("Connection established to " + address + ":" + port);

	}

	public void disconnect() {
		this.connectionSet = false;
		l.info("Connection disconnected!");

	}

	public void stop() {
		l.info("Stopping server...");
		running = false;
	}

	public void send(Packet p) {
		if (!connectionSet || !running) {
			l.error("Can't send packet with no connection information! Is the server even running?");
			return;
		}

		byte[] data = constructPacket(p);

		send = new Thread("SimpleUDP_SEND_" + serverport) {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, remoteAddress, remotePort);

				try {
					server.send(packet);
				} catch (IOException e) {
					l.error("Couldn't send packet!");
					e.printStackTrace();
				}

				l.info("Sent Packet!");

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
			l.error("Error reconstructing packet!");
			e.printStackTrace();
		} catch (IOException e) {
			l.error("Error reconstructing packet!");
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException ex) {
				l.error("Error reconstructing packet!");
				ex.printStackTrace();
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				l.error("Error reconstructing packet!");
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
			l.error("Error constructing packet!");
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				l.error("Error constructing packet!");
				ex.printStackTrace();
			}
			try {
				bos.close();
			} catch (IOException ex) {
				l.error("Error constructing packet!");
				ex.printStackTrace();
			}
		}

		return yourBytes;

	}

	// LoggerBlock:
	private Logger l = new Logger("SimpleUDP", Logger.ERRORS_ONLY);

	public Logger getLogger() {
		return l;
	}
	// --

}
