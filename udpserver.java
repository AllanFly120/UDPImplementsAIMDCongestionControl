package udpprog3;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class udpserver {
	private static final int PACKAGE_SIZE = 1400;
	
	public static DatagramPacket dp = null;
	private static DatagramSocket ds = null;
	private static byte[] buf = new byte[PACKAGE_SIZE];
	private static String orgIp; 
	
	public static String receive() throws IOException {    
        dp = new DatagramPacket(buf, buf.length);    
        ds.receive(dp);    
        orgIp = dp.getAddress().getHostAddress();    
        String info = new String(dp.getData(), 0, dp.getLength());    
        System.out.println("package info：" + orgIp + "  [length]: "+dp.getLength()); 
        byte rtn = buf[0];
        return info;    
    }	
	 public static final void response(byte[] packNum) throws IOException {    
	        DatagramPacket ackPack = new DatagramPacket(buf, 1, dp    
	                .getAddress(), dp.getPort());    
	        dp.setData(packNum);    
	        ds.send(dp); 
	        System.out.println("send ack: " + packNum[0]);
	    }    

	public static void main(String[] args) {		
		
		
		//new a socket for udp
		try {
			ds = new DatagramSocket(15678);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		try {
			ds.setSoTimeout(45);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		int prevIndex = -1;
		int currIndex = 0;
		boolean newSeri = true;//here comes a new serious of packs
		int count = 0;
		while(true)
		{
						
			try {
				
				String str = udpserver.receive();
				count ++;
				AckThread AT = new AckThread(ds,dp,buf[0]);
				
				System.out.println("Received number: "+buf[0]);
				currIndex = buf[0]; 
				if(newSeri){
					newSeri = false;
					Thread th = new Thread(AT, "#"+Integer.toString(count));
					th.start();
//					udpserver.response(buf);
				}
				
				else if(currIndex==prevIndex+1||currIndex==0){
					Thread th = new Thread(AT, "#"+Integer.toString(count));
					th.start();
//					udpserver.response(buf);					
				}
				else if(prevIndex==127&&currIndex==0){
					Thread th = new Thread(AT, "#"+Integer.toString(count));
					th.start();
//					udpserver.response(buf);
				}
				
				prevIndex = currIndex;
			} catch(SocketTimeoutException e){
				newSeri = true;
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
		}
	}
	
}
class AckThread implements Runnable {
	private static DatagramPacket dp = null;
	private static DatagramSocket ds = null;
	byte[] receiveData = new byte[1];
	Random rand = null;
	public AckThread(DatagramSocket sock, DatagramPacket dp, byte receiveData) {
		this.ds = sock;
		this.dp = dp;
		this.receiveData[0] = receiveData;
		rand = new Random();
	}

	public void run() {
		try {
			Thread.sleep(rand.nextInt(10));
			System.out.println("10ms for pack: " + receiveData[0]);

			DatagramPacket sendPacket =new DatagramPacket(receiveData, receiveData.length, dp.getAddress(), dp.getPort());
			ds.send(sendPacket);
			System.out.println("send ack: "+(int)receiveData[0]);			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println(Thread.currentThread().getName() + " is over" );
		}
	}
		
}
//
//public class udpserver {
//	public static int PACK_SIZE = 8192;
//	public static void main(String args[]) throws Exception
//	{
//		int PORT_NUM = 15678;
//
//		System.out.println("SERVER RUNNING.");
//		DatagramSocket serverSocket = new DatagramSocket(PORT_NUM);
//		byte[] receiveData = new byte[PACK_SIZE];
//
//		int count = 0;
//		while(true)
//		{
//			count++;
//			DatagramPacket receivePacket = 
//					new DatagramPacket(receiveData, receiveData.length);
//			serverSocket.receive(receivePacket);
//			System.out.println("receive a packete: "+receiveData[0]);
//			RespThread rt = new RespThread(
//					serverSocket, receivePacket, receiveData[0]);
//
//			Thread th = new Thread(rt, "#"+Integer.toString(count));
//			th.start();			
//		}
//	}
//}
//
//
//class RespThread implements Runnable
//{
//	int SERVER_SLEEP = 10; // ms
//	Thread th;
//	DatagramSocket serverSocket;
//	DatagramPacket receivePacket;
//	byte[] sendData = new byte[1];
//	byte[] receiveData = new byte[1];
//	byte lastID = (byte) (Byte.MIN_VALUE - 1);
//	RespThread(DatagramSocket serverSocket, DatagramPacket receivePacket, byte receiveData) 
//	{ 
//		this.serverSocket = serverSocket;
//		this.receivePacket = receivePacket;
//		this.receiveData[0] = receiveData;
//	}
//
//	public void run() 
//	{
//		try
//		{
//			Thread.sleep(SERVER_SLEEP);
//			System.out.println("10ms for " + Thread.currentThread().getName());
//
//			//String sentence = new String(receivePacket.getData());
//			InetAddress IPAddress = receivePacket.getAddress();
//			int port = receivePacket.getPort();
//
//			System.out.println("Recv: " + (int) receiveData[0]);
//
//			if (receiveData[0] == lastID + 1)
//			{
//				sendData[0] = receiveData[0]; // this is the (ACK) ID byte
//				lastID = receiveData[0];
//				// 
//				System.out.println("ACK: " + (int) sendData[0]);
//				DatagramPacket sendPacket =
//						new DatagramPacket(sendData, sendData.length, IPAddress, port);
//				serverSocket.send(sendPacket);
//			}
//		}
//		catch(InterruptedException e)
//		{
//			System.out.println("my thread interrupted");
//		}
//		catch(IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println(Thread.currentThread().getName() + " is over" );
//	}
//}
//
