package org.xeroserver.x0library.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.xeroserver.x0library.log.Logger;

public class SingleTCP {

	private TCPServer server = null;
	private TCPClient client = null;

	// Loggerblock
	private Logger logger = new Logger();

	public Logger getLogger() {
		return this.logger;
	}
	// -----------

	public SingleTCP(int port) {
		server = new TCPServer(port, logger, this);
	}

	public SingleTCP(String addr, int port) {
		client = new TCPClient(addr, port, logger, this);
	}

	public void packetReceived(Packet p) {

	}

	final public void sendPacket(Packet p) {

		if (server != null) {
			while (!server.isReady()) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			server.send(p);
		} else {
			while (!client.isReady()) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			client.send(p);
		}
	}

	public void stop() {
		if (server != null)
			server.stop();
		else
			client.stop();
	}

	public void start() {
		if (server != null)
			new Thread(server).start();
		else {
			new Thread(client).start();
		}

	}

}

class TCPClient implements Runnable {

	private Socket client = null;
	private String addr = null;
	private int port = -1;
	private Logger logger = null;
	private boolean isCloseRequested = false;
	private SingleTCP parent = null;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private boolean isReady = false;

	public TCPClient(String addr, int port, Logger logger, SingleTCP parent) {
		this.addr = addr;
		this.port = port;
		this.logger = logger;
		this.parent = parent;
	}

	public void send(Packet p) {
		try {
			out.writeObject(p);
			out.flush();
		} catch (IOException e) {
			logger.error("Failed to send packet " + p.getIdentifier());
			e.printStackTrace();
		}
	}

	public boolean isReady() {
		return isReady;
	}

	public void stop() {
		isCloseRequested = true;
	}

	@Override
	public void run() {

		try {
			logger.info("Connecting to server " + addr + ":" + port);
			client = new Socket(addr, port);
			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());
			isReady = true;
			logger.info("Ready to receive/send!");

			while (!isCloseRequested) {
				Packet p = (Packet) in.readObject();
				if (p != null) {
					parent.packetReceived(p);
				}
			}
			in.close();
			out.close();
			client.close();
			logger.info("Client closed!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

class TCPServer implements Runnable {

	private int port = -1;
	private Socket client = null;
	private ServerSocket server = null;
	private boolean isCloseRequested = false;
	private Logger logger = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private SingleTCP parent = null;
	private boolean isReady = false;

	public TCPServer(int port, Logger logger, SingleTCP parent) {
		this.port = port;
		this.logger = logger;
		this.parent = parent;
	}

	public boolean isReady() {
		return isReady;
	}

	public void stop() {
		isCloseRequested = true;
	}

	public void send(Packet p) {
		try {
			out.writeObject(p);
			out.flush();
		} catch (IOException e) {
			logger.error("Failed to send packet " + p.getIdentifier());
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			server = new ServerSocket(port);
			logger.info("Listening on port " + port);
			client = server.accept();
			logger.info("Client connected " + client.getRemoteSocketAddress());
			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());
			isReady = true;
			logger.info("Ready to receive/send!");

			while (!isCloseRequested) {
				Packet p = (Packet) in.readObject();
				if (p != null) {
					parent.packetReceived(p);
				}
			}

			in.close();
			out.close();
			client.close();
			logger.info("Client disconnected!");
			server.close();
			logger.info("Server stopped!");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
