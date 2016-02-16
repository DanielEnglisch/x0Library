package org.xeroserver.x0library.net.packets;

public class IntPacket implements Packet {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	public int getInt() {
		return integer;
	}

}
