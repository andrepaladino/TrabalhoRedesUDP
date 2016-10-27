import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ThreadSend implements Runnable {
	private Router router;
	private DatagramSocket outSocket;
	
	public ThreadSend(Router router) throws SocketException{
		this.router = router;
		outSocket = new DatagramSocket(9876);
	}

	@Override
	public void run() {
		int flag = 1;
		while(true){
			Table table = router.getRouterTable();
			
			byte[] send = table.getBytes();
			
			if(flag == 3){
				flag = 1;
				table.clear();
			}else{
				flag++;
			}
			
			for (String vizinho : router.getNeighbors()) {
				DatagramPacket packet;
				try {
					packet = new DatagramPacket(send, send.length, InetAddress.getByName(vizinho), 9875);
					outSocket.send(packet);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
