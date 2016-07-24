package org.xeroserver.x0library.examples;

import org.xeroserver.x0library.net.Packet;
import org.xeroserver.x0library.net.TCPClient;
import org.xeroserver.x0library.net.TCPServer;

//Sorry for this rather complicated example, will be improved soon...
public class TCPExample {

	public static void main(String[] args) throws Exception {

		TCPServer s = new TCPServer(9999) {

			public void onReceive(Packet p, ServerClient sc) {
				if (p.getID().equals("msg")) {
					Msg msg = (Msg) p;
					getLogger().log("Client " + sc.getAddress() + " said " + msg.getMsg());
				}
			}

			public void onConnect(ServerClient sc) {
				getLogger().log("Client " + sc.getAddress() + " connected!");
			}

			public void onDisconnect(ServerClient sc) {
				getLogger().log("Client " + sc.getAddress() + " disconnected!");
			}
		};
		s.getLogger().setName("SRV");
		s.start();

		Thread.sleep(2000);

		TCPClient c = new TCPClient("localhost", 9999) {
			public void onReceive(Packet p) {
				if (p.getID().equals("msg")) {
					Msg msg = (Msg) p;
					getLogger().log("Server said  " + msg.getMsg());
				}
			}

			public void onDisconnect() {
				getLogger().log("I got disconnected!");
			}
		};
		c.getLogger().setName("Client1");

		TCPClient c2 = new TCPClient("localhost", 9999) {
			public void onReceive(Packet p) {
				if (p.getID().equals("msg")) {
					Msg msg = (Msg) p;
					getLogger().log("Server said  " + msg.getMsg());
				}
			}

			public void onDisconnect() {
				getLogger().log("I got disconnected!");
			}
		};
		c2.getLogger().setName("Client2");

		c.connect();
		c2.connect();

		Thread.sleep(2000);

		c.send(new Msg("Hello I am Client1 how are you?"));
		c2.send(new Msg("Hello I am Client2 how are you?"));

		Thread.sleep(2000);

		c2.disconnect();

		Thread.sleep(2000);

		s.stop();

		Thread.sleep(2000);

	}

}

class Msg implements Packet {

	private static final long serialVersionUID = 5219881599648514025L;
	private String msg = null;

	public Msg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String getID() {
		return "msg";
	}

}
