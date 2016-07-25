package org.xeroserver.x0library.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import org.xeroserver.x0library.log.Logger;

public abstract class TCPServer {

	private int port = -1;
	private ServerSocket server = null;
	private Logger logger = null;
	private boolean running = false, closeRequest = false;
	private ArrayList<ServerClient> clients = new ArrayList<ServerClient>();

	public TCPServer(int port) {
		this.port = port;
		this.logger = new Logger();
	}

	public boolean start() {
		if (!running) {
			try {
				server = new ServerSocket(port);
			} catch (IOException e) {
				logger.fatal("Failed to start server!");
				e.printStackTrace();
				return false;

			}

			running = true;

			startListening();
			return true;

		} else
			logger.error("Server already running!");

		return false;
	}

	public Logger getLogger() {
		return this.logger;
	}

	private void startListening() {
		Runnable task = () -> {
			logger.info("Server started! Listening on port " + port + "...");

			while (!closeRequest) {
				Socket socket = null;
				try {
					socket = server.accept();
				} catch (IOException e) {

					if (e instanceof SocketException)
						break;
					logger.error("Failed to accept socket!");
					e.printStackTrace();
				}

				if (socket != null) {
					ServerClient sc = new ServerClient(socket);
					clients.add(sc);
					new Thread(sc).start();
				}

			}

			running = false;
			closeRequest = false;
			logger.info("Server stopped!");
		};

		new Thread(task).start();
	}

	public void stop() {
		if (running) {
			closeRequest = true;
			for (ServerClient sc : clients) {
				sc.stop();
			}
			try {
				server.close();
			} catch (IOException e) {
				logger.error("Failed to close the server!");
				e.printStackTrace();
			}
		} else
			logger.log("Can't close! Server not running!");
	}

	public void broadcast(Packet p) {
		for (ServerClient sc : clients) {
			sc.send(p);
		}
	}

	public void send(Packet p, ServerClient sc) {
		sc.send(p);
	}

	public class ServerClient implements Runnable {

		private Socket socket = null;
		private String address = null;
		private ObjectOutputStream out = null;
		private ObjectInputStream in = null;
		private boolean closeRequest = false;
		private boolean isClosed = false;

		public ServerClient(Socket socket) {
			this.socket = socket;
			this.address = socket.getRemoteSocketAddress().toString();

			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				logger.error("Failed to create stream for client " + address + " !");
				e.printStackTrace();
				closeRequest = true;
				return;
			}

			onConnect(this);
		}

		public void stop() {
			send(new DisconnectPacket());
			closeRequest = true;
		}

		public String getAddress() {
			return address;
		}

		public boolean isClosed() {
			return isClosed;
		}

		public void send(Packet p) {

			if (isClosed()) {
				logger.error("Can't send packet! Client " + address + " is closed!");
				return;
			}

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

		}

		public void run() {
			while (!closeRequest) {
				Packet p = null;
				try {
					p = (Packet) in.readObject();
				} catch (ClassNotFoundException e) {
					logger.warning("Received unknown packet!");
					e.printStackTrace();
				} catch (IOException e) {

					if (e instanceof EOFException) {
						break;
					}

					logger.error("Failed to receive packet!");
					e.printStackTrace();
				}

				onReceive(p, this);
			}

			// Closing
			try {
				isClosed = true;
				out.close();
				in.close();
				socket.close();
				clients.remove(this);
			} catch (IOException e) {
				logger.error("Failed to close streams or socket!");
				e.printStackTrace();
			}

			onDisconnect(this);
		}

	}

	// Override
	public abstract void onReceive(Packet p, ServerClient sc);

	// Override
	public abstract void onConnect(ServerClient sc);

	// Override
	public abstract void onDisconnect(ServerClient sc);

}

class DisconnectPacket implements Packet {

	private static final long serialVersionUID = 2352930055489023628L;

	@Override
	public String getID() {
		return "DisconnectPacket";
	}

}
