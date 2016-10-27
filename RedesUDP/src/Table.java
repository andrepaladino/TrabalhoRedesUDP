
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

class Table {

	String IPHost;
	private List<String> IP;
	private List<Byte> metrica;
	private List<String> IPDestino;
	private List<Boolean> houveSinalDeVida;

	public Table(byte[] bytes, String IPSender) {
		this(IPSender);
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		byte quantidadeDeIPs = byteBuffer.get();
		byte[] bufferIP = new byte[15];
		for (int i = 0; i < quantidadeDeIPs; i++) {
			byteBuffer.get(bufferIP);
			String IP = new String(bufferIP).replaceAll("\0", "");
			this.IP.add(IP);
			this.metrica.add(byteBuffer.get());

			byteBuffer.get(bufferIP);
			String IPDestino = new String(bufferIP).replaceAll("\0", "");
			this.IPDestino.add(IPDestino);
		}
	}

	public Table(String IPHost) {
		this.IP = new ArrayList<String>();
		this.metrica = new ArrayList<Byte>();
		this.IPDestino = new ArrayList<String>();
		this.houveSinalDeVida = new ArrayList<Boolean>();
		this.IPHost = IPHost;
		System.out.println("Esta tabela pertence a: " + this.IPHost);
	}

	public String getHost() {
		return this.IPHost;
	}

	public void updateIP(String IP, byte metrica, String IPDestino) {
		if (!IP.equals(this.IPHost)) {
			int indexOfIp = this.IP.indexOf(IP);
			if (indexOfIp == -1) { // Se nao ta na tabela
				// Adiciona
				this.IP.add(IP);
				this.metrica.add(metrica);
				this.IPDestino.add(IPDestino);
				this.houveSinalDeVida.add(Boolean.TRUE);
			} else if (!this.metrica.get(indexOfIp).equals((byte) 1)) { // Se
																		// nao e
																		// meu
																		// vizinho
				// Atualiza a metrica
				this.metrica.set(indexOfIp, metrica);
				// Atualiza quem enviou
				this.IPDestino.set(indexOfIp, IPDestino);
				this.houveSinalDeVida.set(indexOfIp, Boolean.TRUE);
			}
		}
	}

	public void updateIP(String IP, byte metrica, String IPDestino, String IPDestinoTabela) {
		if (!IPDestinoTabela.equals(this.IPHost)) {
			updateIP(IP, metrica, IPDestino);
		}
	}

	public String printTable() {
		String tabela = "      IP      | M |   IPDESTINO   \n";
		for (int index = 0; index < this.IP.size(); index++) {
			tabela = tabela.concat(this.IP.get(index));
			tabela = tabela.concat(" | ");
			tabela = tabela.concat(String.valueOf(this.metrica.get(index)));
			tabela = tabela.concat(" | ");
			tabela = tabela.concat(this.IPDestino.get(index));
			tabela = tabela.concat(" \n");
		}
		return tabela;
	}

	public byte[] getBytes() {
		byte quantidadeDeIPs = (byte) this.IP.size();
		byte[] tabelaBytes = new byte[31 * quantidadeDeIPs + 1];
		ByteBuffer controlador = ByteBuffer.wrap(tabelaBytes);
		controlador.put(quantidadeDeIPs);
		for (int index = 0; index < this.IP.size(); index++) {
			String IP = this.IP.get(index);
			int padding = 15 - IP.length();
			controlador.put(new byte[padding]);
			controlador.put(IP.getBytes());
			controlador.put(this.metrica.get(index));

			String IPDestino = this.IPDestino.get(index);
			int padding2 = 15 - IPDestino.length();
			controlador.put(new byte[padding2]);
			controlador.put(IPDestino.getBytes());
		}

		return tabelaBytes;
	}

	public void updateTable(Table novaTabela) {
		int indiceVizinho = IP.indexOf(novaTabela.getHost());
		if (indiceVizinho == -1) {
			IP.add(novaTabela.getHost());
			metrica.add((byte) 1);
			IPDestino.add("0.0.0.0");
			houveSinalDeVida.add(Boolean.TRUE);
		} else {
			houveSinalDeVida.set(IP.indexOf(novaTabela.getHost()), Boolean.TRUE);
		}
		List<String> novosIP = novaTabela.getIP();
		List<Byte> novasMetricas = novaTabela.getMetricas();
		List<String> IPsDestino = novaTabela.getIPDestino();
		for (int i = 0; i < novosIP.size(); i++) {
			byte metricaIncrementada = novasMetricas.get(i);
			metricaIncrementada += 1;
			this.updateIP(novosIP.get(i), metricaIncrementada, novaTabela.getHost(), IPsDestino.get(i));
		}
	}

	private List<String> getIP() {
		return this.IP;
	}

	private List<Byte> getMetricas() {
		return this.metrica;
	}

	private List<String> getIPDestino() {
		return this.IPDestino;
	}

	public void clear() {
		for (int i = houveSinalDeVida.size() - 1; i >= 0; i--) {
			if (!houveSinalDeVida.get(i)) {
				System.out.println(IP.remove(i) + " removido");
				metrica.remove(i);
				IPDestino.remove(i);
				houveSinalDeVida.remove(i);
			} else {
				houveSinalDeVida.set(i, Boolean.FALSE);
			}
		}
	}
}
