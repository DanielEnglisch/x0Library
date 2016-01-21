package org.xeroserver.x0_Library.net.SimpleUDP.Packet;

public class DoublePacket extends Packet{
	

	private static final long serialVersionUID = 8967438183609337181L;
	/**
	 * 
	 */
	private int type = Packet.DOUBLE;
	private double d;

	public DoublePacket(double d)
	{
		this.d = d;
	}
	
	@Override
	public int getPacketType()
	{
		return type;
	}
	
	@Override
	public Double getDouble()
	{
		return d;
	}
	
	
	
	

}
