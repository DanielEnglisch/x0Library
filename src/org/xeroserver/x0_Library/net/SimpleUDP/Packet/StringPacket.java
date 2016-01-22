package org.xeroserver.x0_Library.net.SimpleUDP.Packet;

public class StringPacket extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5537458544692785168L;
	private int type = Packet.STRING;
	private String string;

	public StringPacket(String string) {
		this.string = string;
	}

	@Override
	public int getPacketType() {
		return type;
	}

	@Override
	public String getString() {
		return string;
	}

}
