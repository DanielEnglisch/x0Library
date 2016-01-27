package org.xeroserver.x0_Library.net.UDP.Packet;

public class FloatPacket implements Packet {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int type = Packet.FLOAT;
	private float f;
	private String id;


	public FloatPacket(float f) {
		this.f = f;
	}
	
	public FloatPacket(float f, String id) {
		this.f = f;
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

	public Float getFloat() {
		return f;
	}

}
