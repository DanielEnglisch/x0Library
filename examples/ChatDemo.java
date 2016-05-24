package org.xeroserver.x0library.examples;

import javax.swing.JFrame;

import org.xeroserver.x0library.gui.X0InputField;
import org.xeroserver.x0library.log.Logger;
import org.xeroserver.x0library.net.packets.StringPacket;
import org.xeroserver.x0library.net.udp.SingleUDP;
import org.xeroserver.x0library.net.udp.SingleUDPReceiver;

/*
 * This simple program demonstrates how quickly you can create something rather complex as a network chat application with under 60 lines of code by using x0_Library.
 * Used Classes: SingleUDP (Networking), SingleUDPReceiver (Networking), Logger (Displaying received messaged), X0InputField (Processing input)
 */

public class ChatDemo extends SingleUDPReceiver {

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

		@SuppressWarnings("serial")
		X0InputField xif = new X0InputField(new int[] { X0InputField.CLEAR_ON_ENTER, X0InputField.NO_COLOR }, false) {
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
	public void receiveString(String s, String id) {
		l.log("Received: " + s);
	}

}
