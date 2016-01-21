package org.xeroserver.x0_Library.net.SimpleUDP.Packet;

public class FloatPacket extends Packet{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4360778014750058026L;
	private int type = Packet.FLOAT;
	private float f;

	public FloatPacket(float f)
	{
		this.f = f;
	}
	
	@Override
	public int getPacketType()
	{
		return type;
	}
	
	@Override
	public Float getFloat()
	{
		return f;
	}
	
	
	
	

}
