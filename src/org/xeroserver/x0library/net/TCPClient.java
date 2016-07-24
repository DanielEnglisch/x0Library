package org.xeroserver.x0library.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import org.xeroserver.x0library.log.Logger;

public class TCPClient {

	private Socket socket = null;
	private Logger logger = null;
	private String address = null;
	private int port = -1;
	private boolean connected = false, closeRequest = false;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;

	public TCPClient(String addr, int port) {
		this.address = addr;
		this.port = port;
		logger = new Logger();
	}

	public void connect() {
		if (!connected) {
			try {
				socket = new Socket(address, port);
			} catch (IOException e) {
				logger.fatal("Connection failed!");
				e.printStackTrace();
				return;
			}

			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				logger.error("Failed to create stream!");
				e.printStackTrace();
				return;
			}
			connected = true;

			startListening();
		} else
			logger.error("Client already connected!");

	}

	private void startListening() {

		Runnable task = () -> {

			logger.info("Connection established to " + address + ":" + port + " !");

			while (!closeRequest) {

				Packet p = null;
				try {
					p = (Packet) in.readObject();
				} catch (ClassNotFoundException e) {
					logger.warning("Received unknown packet!");
					e.printStackTrace();
				} catch (IOException e) {

					if (e instanceof SocketException || e instanceof EOFException) {
						break;
					}

					logger.error("Failed to receive packet!");
					e.printStackTrace();
				}

				if (p != null) {

					if (p.getID().equals("DisconnectPacket")) {
						break;
					} else
						onReceive(p);

				}

			}

			// Closing
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				logger.error("Failed to close streams!");
				e.printStackTrace();
			}

			connected = false;
			closeRequest = false;
			onDisconnect();
		};

		new Thread(task).start();

	}

	public void disconnect() {
		if (connected) {
			closeRequest = true;
			try {
				socket.close();
			} catch (IOException e) {
				logger.error("Failed to close socket!");
				e.printStackTrace();
			}

		} else
			logger.error("Can't disconnect! Client not connected!");

	}

	public Logger getLogger() {
		return this.logger;
	}

	public void send(Packet p) {
		if (connected) {
			try {
				out.writeObject(p);
			} catch (IOException e) {
				logger.error("Failed to write packet with id " + p.getID());
				e.printStackTrace();
			}

			try {
				out.flush();
			} catch (IOException e) {
				logger.error("Failed to flush packet with id " + p.getID());
				e.printStackTrace();
			}

		} else
			logger.error("Can't send packet! Client not connected!");
	}

	// Override
	public void onReceive(Packet p) {
		logger.info("OVR: Received packer with id " + p.getID());
	}

	// Override
	public void onDisconnect() {
		logger.info("OVR: Server stopped or client disconnected!");
	}

}
