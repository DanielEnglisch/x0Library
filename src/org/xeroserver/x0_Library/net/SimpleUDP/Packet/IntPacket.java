package org.xeroserver.x0_Library.net.SimpleUDP.Packet;

public class IntPacket extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1107555468190427268L;
	private int type = Packet.INT;
	private int integer;
	private String id;


	public IntPacket(int integer) {
		this.integer = integer;
	}
	
	public IntPacket(int integer, String id) {
		this.integer = integer;
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
	public int getInt() {
		return integer;
	}

}
