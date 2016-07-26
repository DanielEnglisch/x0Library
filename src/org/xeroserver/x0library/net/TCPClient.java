package org.xeroserver.x0library.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.SocketException;

import org.xeroserver.x0library.log.Logger;

public abstract class TCPClient {

	private Socket socket = null;
	private Logger logger = null;
	private String address = null;
	private int port = -1;
	private boolean connected = false, closeRequest = false;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	private boolean sending = false;

	public TCPClient(String addr, int port) {
		this.address = addr;
		this.port = port;
		logger = new Logger();
	}

	public final boolean connect() {
		if (!connected) {
			try {
				socket = new Socket(address, port);
			} catch (IOException e) {
				logger.fatal("Connection failed!");
				e.printStackTrace();
				return false;
			}

			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				logger.error("Failed to create stream!");
				e.printStackTrace();
				return false;
			}
			connected = true;
			startListening();
			return true;
		} else
			logger.error("Client already connected!");

		return false;
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

					if (e instanceof StreamCorruptedException) {
						onError();
						break;
					}

					logger.error("Failed to receive packet!");
					e.printStackTrace();
				}

				if (p != null) {

					if (p.getID().equals("DisconnectPacket")) {
						onServerStop();
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
			logger.info("Connection closed!");

		};

		new Thread(task).start();

	}

	public final void disconnect() {
		if (connected) {

			// Wait until sendint finished!
			while (sending) {
				try {
					logger.info("Send in progress sleeping for 100ms...");
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

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

	public final Logger getLogger() {
		return this.logger;
	}

	public final void send(Packet p) {
		if (connected) {
			sending = true;
			try {
				out.writeObject(p);
			} catch (IOException e) {
				logger.error("Failed to write packet with id " + p.getID());
				e.printStackTrace();
				sending = false;
			}

			try {
				out.flush();
			} catch (IOException e) {
				logger.error("Failed to flush packet with id " + p.getID());
				e.printStackTrace();
				sending = false;
			}
			sending = false;

		} else
			logger.error("Can't send packet! Client not connected!");
	}

	// Override
	public abstract void onReceive(Packet p);

	// Override
	public abstract void onServerStop();

	public abstract void onError();

}
