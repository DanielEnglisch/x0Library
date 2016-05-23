package org.xeroserver.x0library.net.packets;

public class DoublePacket implements Packet {

	private static final long serialVersionUID = 1L;
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

	public Double getDouble() {
		return d;
	}

}
