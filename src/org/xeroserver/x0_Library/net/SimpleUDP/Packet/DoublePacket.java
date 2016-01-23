package org.xeroserver.x0_Library.net.SimpleUDP.Packet;

public class DoublePacket extends Packet {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8749358722707548096L;
	private int type = Packet.DOUBLE;
	private double d;
	private String id;

	public DoublePacket(double d) {
		this.d = d;
	}
	
	public DoublePacket(double d, String id) {
		this.d = d;
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
	public Double getDouble() {
		return d;
	}

}
