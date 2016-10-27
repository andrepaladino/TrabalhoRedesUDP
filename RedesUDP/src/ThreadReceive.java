import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.sun.org.apache.xerces.internal.dom.DeferredDocumentImpl;

public class ThreadReceive implements Runnable {
	
	private Router router ;
	private DatagramSocket inSocket;
	private DatagramPacket packet;
	

	public ThreadReceive(Router r) throws SocketException{
		byte[] buffer = new byte[1024];
		this.router = r;
		inSocket = new DatagramSocket(9875);
		packet = new DatagramPacket(buffer, buffer.length);
	}
	
	@Override
	public void run() {
		while(true){
			
			try {
				inSocket.receive(packet);
				Table table = new Table(packet.getData(), packet.getAddress().getHostAddress());
				router.setTable(table);
				System.out.println(router.getRouterTable().printTable());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
	}

}
