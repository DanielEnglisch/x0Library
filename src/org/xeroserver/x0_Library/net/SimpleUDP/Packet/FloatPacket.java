package org.xeroserver.x0_Library.net.SimpleUDP.Packet;

public class FloatPacket extends Packet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1280841144532564266L;
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

	@Override
	public Float getFloat() {
		return f;
	}

}
