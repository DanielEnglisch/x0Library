package org.xeroserver.x0library.examples;

import javax.swing.JFrame;

import org.xeroserver.x0library.gui.X0InputField;
import org.xeroserver.x0library.log.Logger;
import org.xeroserver.x0library.net.Packet;
import org.xeroserver.x0library.net.SingleUDP;
import org.xeroserver.x0library.net.SingleUDP.PacketReceiver;

/*
 * This simple program demonstrates how quickly you can create something rather complex as a network chat application with under 60 lines of code by using x0_Library.
 * Used Classes: SingleUDP (Networking), SingleUDPReceiver (Networking), Logger (Displaying received messaged), X0InputField (Processing input)
 */

public class ChatDemo implements PacketReceiver {

	private Logger l;

	public static void main(String[] args) {
		new ChatDemo("localhost", 6666, 9999);
		new ChatDemo("localhost", 9999, 6666);
	}

	public ChatDemo(String remote_address, int remotePort, int port) {

		l = new Logger("Logger - " + port);
		l.showGUI();

		SingleUDP udp = new SingleUDP(port);
		udp.setConnection(remote_address, remotePort);
		udp.registerReceiver(this);

		JFrame f = new JFrame("PrivateChat - " + port);
		f.setSize(300, 50);
		f.setLayout(null);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		X0InputField xif = new X0InputField(new int[] { X0InputField.CLEAR_ON_ENTER, X0InputField.NO_COLOR }, false) {

			private static final long serialVersionUID = -6751565266815075263L;

			@Override
			public void update() {
				udp.send(new StringPacket("[" + port + "] " + getStringValue()));
			}
		};
		xif.setBounds(0, 0, 300, 30);

		f.add(xif);

		f.setVisible(true);
	}

	@Override
	public void recievePacket(Packet p) {

		if (p.getIdentifier().equals("string_msg"))
			l.log(((StringPacket) p).getStringValue());
	}
}

class StringPacket implements Packet {

	private static final long serialVersionUID = 369662039919894568L;
	private String s = null;

	public StringPacket(String msg) {
		s = msg;
	}

	public String getStringValue() {
		return s;
	}

	@Override
	public String getIdentifier() {
		return "string_msg";
	}
}
