import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Controller {

	private static Router router;

	public static void main(String[] args) throws SocketException, IOException {
		Scanner in = new Scanner(System.in);
		
		System.out.println("Digite seu endere�o de IP");
		String meuIP = in.next();
		
		DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		serverSocket.receive(receivePacket);
		serverSocket.close();
		
		
		
		Map<String, String> info = solve(new String(receiveData));
		
		
		if(info.containsKey(meuIP)){
			String linha = info.get(meuIP);
			String[] vizinhosIPs = linha.split(";");
			List<String> vizinhosIPList = Arrays.asList(vizinhosIPs);
			
			router = new Router(vizinhosIPList, meuIP);
			System.out.println(router.getRouterTable().printTable());
			
			//Thread para receber
			Thread receive = new Thread(new ThreadReceive(router));
			Thread send = new Thread(new ThreadSend(router));
			receive.start();
			send.start();
		}
		
		

	}
	
	public static Map<String, String> solve(String info){
		String[] lines = info.split(",");
		
		Map<String, String> information = new HashMap<String, String>();
		
		for (int i = 1; i < lines.length; i++) {
			String[] line = lines[i].split(":");
			information.put(line[0].trim(), line[1].trim());
		}
		
		return information;
	}

}
