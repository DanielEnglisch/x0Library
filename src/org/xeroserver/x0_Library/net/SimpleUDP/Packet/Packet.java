package org.xeroserver.x0_Library.net.SimpleUDP.Packet;

import java.io.Serializable;

public class Packet implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 9077824379821359447L;
	public static final int UNKNOWN = 0, STRING = 1, INT = 2, DOUBLE = 3, FLOAT = 4;

	
	
	public int getPacketType() {
		return UNKNOWN;
	}
	
	public String getIdentifier() {
		return "";
	}

	//Type specific methods: 
	
	public String getString() {
		return "";
	}

	public int getInt() {
		return -1;
	}

	public Double getDouble() {
		return -1.0d;
	}

	public Float getFloat() {
		return -1.0f;
	}

}
