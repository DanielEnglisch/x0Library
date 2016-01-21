package org.xeroserver.x0_Library.net.SimpleUDP.Packet;

public class IntPacket extends Packet{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8466611780450518564L;
	private int type = Packet.INT;
	private int integer;

	public IntPacket(int integer)
	{
		this.integer = integer;
	}
	
	@Override
	public int getPacketType()
	{
		return type;
	}
	
	@Override
	public int getInt()
	{
		return integer;
	}
	
	
	
	

}
