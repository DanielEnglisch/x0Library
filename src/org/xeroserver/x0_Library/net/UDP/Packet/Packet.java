package org.xeroserver.x0_Library.net.UDP.Packet;

import java.io.Serializable;

public interface Packet extends Serializable {


	public static final int CUSTOM = 0, STRING = 1, INT = 2, DOUBLE = 3, FLOAT = 4;

	public int getPacketType();
	public String getIdentifier();


}
