package org.xeroserver.x0_Library;

/**
 * This class shows some examples to user some classes of this library.
 * 
 * @author Daniel 'Xer0' Englisch
 * @since 2015-07-12
 * @website http://xeroserver.org/
 * @source http://github.com/DanielEnglisch/x0_Library
 * 
 **/

import java.io.File;

import javax.swing.JFrame;

import org.xeroserver.x0_Library.Config.ConfigFile;
import org.xeroserver.x0_Library.GUI.X0InputField;
import org.xeroserver.x0_Library.Log.Logger;
import org.xeroserver.x0_Library.ObjectTools.StringTools;
import org.xeroserver.x0_Library.Parser.ArgumentParser;
import org.xeroserver.x0_Library.net.SimpleUDP.SimpleUDP;
import org.xeroserver.x0_Library.net.SimpleUDP.UDPReceiver;
import org.xeroserver.x0_Library.net.SimpleUDP.Packet.DoublePacket;
import org.xeroserver.x0_Library.net.SimpleUDP.Packet.FloatPacket;
import org.xeroserver.x0_Library.net.SimpleUDP.Packet.IntPacket;
import org.xeroserver.x0_Library.net.SimpleUDP.Packet.StringPacket;

public class MainClass extends UDPReceiver {

	private static Logger l = new Logger("MainLogger");

	public MainClass() throws Exception {
		testSimpleUDP();

		// l.showGUI();

		// l.info("-----Logger-----"); testLogger();

		// l.info("-----X0InputField-----"); testInpuField();

		// l.info("-----ConfigFile-----"); testConfigFile();

		// l.info("-----StringTools-----"); testStringTools();

		// l.info("-----ArgumentParser-----"); testArgumentParser();

		// l.write(new File(".", "log.log"));

	}

	public static void main(String[] args) throws Exception {

		new MainClass();

	}

	// Methods extended to receive Packets:
	@Override
	public void receiveString(String s) {
		System.out.println("EVENT: Received String: " + s);
	}

	@Override
	public void receiveInt(int i) {
		System.out.println("EVENT: Received Int: " + i);
	}

	@Override
	public void receiveDouble(double d) {
		System.out.println("EVENT: Received Double: " + d);
	}

	@Override
	public void receiveFloat(float f) {
		System.out.println("EVENT: Received Float: " + f);
	}
	// #######################################################

	public void testSimpleUDP() throws InterruptedException {
		SimpleUDP server = new SimpleUDP(5599);
		SimpleUDP client = new SimpleUDP(6000);
		client.registerReceiver(this);

		server.setConnection("localhost", 6000);

		while (true) {
			server.send(new StringPacket("Test"));
			Thread.sleep(100);
			server.send(new IntPacket(6969));
			Thread.sleep(100);
			server.send(new DoublePacket(0.99999999995));
			Thread.sleep(100);
			server.send(new FloatPacket(0.99999999995f));
			Thread.sleep(1000);
		}
	}

	public void testLogger() {
		Logger l1 = new Logger();
		Logger l2 = new Logger("Yolo");
		Logger l3 = new Logger("XER0", Logger.ERRORS_ONLY);

		l1.fatal("Something went horribly wrong...");
		l2.info("Ther is an update available!");

		l3.info("Yea this is not important...");
		l3.error("This on the other hand is!");

	}

	public void testInpuField() {
		JFrame f = new JFrame("GUI TEST");
		f.setLayout(null);

		@SuppressWarnings("serial")
		X0InputField inp = new X0InputField(
				new int[] { X0InputField.DOUBLE, X0InputField.NOT_ZERO, X0InputField.POSITIVE,

		}) {
			@Override
			public void update() {
				l.info("##########");
				l.info("String: " + this.getStringValue());
				l.info("Integer: " + this.getIntegerValue());
				l.info("Double: " + this.getDoubleValue());

			}

		}

		;
		inp.setDisplayErrors(true);

		inp.setBounds(50, 50, 200, 40);
		f.add(inp);

		f.setSize(500, 300);
		f.setLocationRelativeTo(null);

		f.setResizable(false);
		f.setVisible(true);

	}

	public void testConfigFile() {

		ConfigFile conf = new ConfigFile(new File(".", "test.conf"));
		conf.getLogger().setMode(Logger.NORMAL);

		conf.listProperties();
		conf.save();

	}

	public void testStringTools() {
		String s = "Xer0";

		l.log(s);
		l.info(StringTools.getFirstChar(s));
		l.info(StringTools.getLastChar(s));
		l.info(StringTools.removeFirstChar(s));
		l.info(StringTools.removeLastChar(s));

	}

	public void testArgumentParser() {
		String args = "-x -y -z -verbose -name Daniel";

		ArgumentParser p = new ArgumentParser(args);

		l.info(p.getValue("-name"));

		if (p.exists("-y")) {
			l.info("YES");
		}
	}

}
