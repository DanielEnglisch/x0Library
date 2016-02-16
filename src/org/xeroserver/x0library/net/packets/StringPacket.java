package org.xeroserver.x0library.net.packets;

public class StringPacket implements Packet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	public String getString() {
		return string;
	}

}
