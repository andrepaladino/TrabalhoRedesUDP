import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

public class Router {
	private List<String> neighbors;
	private String IP;
	
	private Table routerTable;
	
	public Router(List<String> neighbors, String IP){
		this.neighbors = neighbors;
		this.IP = IP;
        this.routerTable = new Table(IP);
        for (String vizinho : neighbors) {
			routerTable.updateIP(vizinho, (byte)1, "0.0.0.0");
		}
	}
	
	public Table getRouterTable(){
		return this.routerTable;
	}
	
	public String receivePackage() throws SocketException{
		
		DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		return receiveData.toString();
	}
	
	public void sendPackage(byte[] data) throws UnknownHostException, SocketException, IOException {
		String destinateIP = neighbors.get(0);
                DatagramSocket socket = new DatagramSocket(9876);
		DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getByName(destinateIP), 9876);
                socket.send(sendPacket);
	}

	public void setTable(Table table) {
		routerTable.updateTable(table);
		
	}
	
	public List<String> getNeighbors(){
		return neighbors;
	}
	
	
	
	

}
