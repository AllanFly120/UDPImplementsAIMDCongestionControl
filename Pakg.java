package udpprog3;

import java.util.Arrays;

public class Pakg {
	private static final int PACKAGE_SIZE = 1400;
	private static final int DATA_PACKAGE = 1;
	private static final int ACK_PACKAGE = 2;
	private static int CURWIN = 1;
	
//	public Byte[] dummyFile = new Byte[Integer.MAX_VALUE];
	private byte pakg[];
	//Packages with dummy data
	public Pakg(int packSize, int packNum) {
//		pakg = new byte[PACKAGE_SIZE];
//		byte[] tmp = (new Integer(packNum).toString()+"aaa").getBytes();
//		Arrays.copyOf(tmp, PACKAGE_SIZE);
//		pakg = tmp;
		pakg = new byte[PACKAGE_SIZE+1];
		pakg[0] = (new Integer(packNum)).byteValue();
		for(int i=1; i<PACKAGE_SIZE+1; i++) {
			pakg[i] = Byte.MAX_VALUE;
		}
	}
	//A ACK Package
	public Pakg(int i) throws Exception{
		if(i == ACK_PACKAGE) {
			pakg = new byte[1];
			pakg = new String("ACK").getBytes();
		}else {
			throw new Exception("setting ACK package fial.");
		}
	}
	//Packages with specified data
	public Pakg(int packSize, int packNum, byte[] content) {
	//dummy code	
	}
	
	public byte[] getPakg() {
		return pakg.clone();
	}
	
	public byte getPackNum() {
		return new Byte(pakg[0]);
	}
}