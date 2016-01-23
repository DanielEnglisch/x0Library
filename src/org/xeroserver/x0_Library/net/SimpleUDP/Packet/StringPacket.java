package org.xeroserver.x0_Library.net.SimpleUDP.Packet;

public class StringPacket extends Packet {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1803794729489597330L;
	private int type = Packet.STRING;
	private String string;
	private String id;


	public StringPacket(String string) {
		this.string = string;
	}
	
	public StringPacket(String string, String id) {
		this.string = string;
		this.id = id;
	}
	
	@Override
	public String getIdentifier() {
		return id;
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
