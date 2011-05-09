package dev;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class Cliente extends Thread {

	private Socket conexao;
	private ServerSocket escuta;
	private static String msg = null;
	public static String client;
	// Exp Reg pra pegar o ip válido do usuario...
	private static final String ipvalido = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	protected static String ipServ = null;
	protected static int portServ;
	protected static int port = 9999; // passar por args[0]
	protected static String ip;
	protected static String nome = "felipe"; // passar por args[1]

	// construtor que recebe o socket do cliente
	public Cliente(Socket socket) {
		this.setConexao(socket);
	}

	public Cliente(ServerSocket socket) {
		this.setEscuta(socket);
	}

	public String getip() {
		return ip;
	}

	public void setip(String ip) {
		Cliente.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		Cliente.port = port;
	}

	public static void main(String[] args) {

		try {
			Enumeration<?> nis = null;
			try {
				nis = NetworkInterface.getNetworkInterfaces();
			} catch (SocketException e) {
				e.printStackTrace();
			}

			while (nis.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) nis.nextElement();
				Enumeration<?> ias = ni.getInetAddresses();

				while (ias.hasMoreElements()) {
					InetAddress ia = (InetAddress) ias.nextElement();
					if (!ni.getName().equals("lo"))
						if (ia.getHostAddress().matches(ipvalido)) {
							ip = ia.getHostAddress();
						}

				}
			}

			ler("config.txt");
			Socket sock = new Socket(ipServ, portServ); // cria socket com
														// NameServer

			// pega os fluxos out e in do socket TCP e encapusla em
			// ... fluxos de objeto
			ObjectOutputStream out = new ObjectOutputStream(
					sock.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(sock.getInputStream());

			// cria a estrutura de dados a ser enviada (Meus dados...)
			Estrutura str = new Estrutura(nome, port, ip);

			out.writeObject(str); // envia objeto para o servidor (Worker)

			String resp = (String) in.readObject(); // recebe objeto de resposta

			System.out.println("Status = " + resp);

			ServerSocket ss;
			ss = new ServerSocket();
			// System.out.println("Iniciando thread...");
			Thread thread = new Cliente(ss);
			thread.start();

			BufferedReader teclado = new BufferedReader(new InputStreamReader(
					System.in));
			// Escolhe com quem vai falar
			while (true) {

				System.out.println("Deseja se comunicar com: ");
				client = teclado.readLine();
				// Pega os dados do cliente... (ip + porta)
				// System.out.println("Cliente: "+client+" sock: "+sock);
				Estrutura papo = pegaCliente(client, sock);
				System.out.println("Retornou: " + papo.getIp()
						+ papo.getPorta() + papo.getNome());
				// Cria o socket
				Socket sockCliente = new Socket(papo.getIp(), papo.getPorta());
				System.out.println("Socket Criado: " + sockCliente);
				// out.close();
				ObjectOutputStream out1 = new ObjectOutputStream(sockCliente.getOutputStream());
				BufferedReader tecl = new BufferedReader(new InputStreamReader(System.in));
				while (true) {
					msg = tecl.readLine();
					if (msg == "@fim") {
						break;
					} else {
						out1.writeObject(msg);
						// TODO Envia msg pro cliente :)
					}
				}
				System.out.println("Fim de papo :)");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Estrutura pegaCliente(String nome, Socket sock)
			throws Exception {
		ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
		// envia busca ao servidor
		out.writeObject(nome);

		Estrutura resp = (Estrutura) in.readObject(); // recebe objeto de
														// resposta

		if (resp != null) {
			// TODO Avisa ao servidor que está OCUPADO
			return resp;
		} else {
			throw new Exception("Não foi possível localizar o contato!");
		}

	}

	public void run() {
		// TODO Escutando possíveis msgs a receber...
		try {
			ServerSocket ss;
			ss = new ServerSocket(port);
			while (true) {
				// TODO inicia a "escuta" na porta 'n' por possíveis mensagens!
				//System.out.println("chamooou...");

				Socket as = ss.accept();
				WorkerClient w = new WorkerClient(as);
				w.start(); // lancei a thread para conexão recebida
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void ler(String arquivo) throws IOException {
		/*
		 * Este método lê as configurações para o início da aplicação como IP e
		 * PORTA do Servidor de Nomes.
		 */
		FileReader reader = new FileReader(arquivo);
		BufferedReader buffReader = new BufferedReader(reader);
		String linha;
		String regex = "=";
		linha = buffReader.readLine();
		ipServ = linha.split(regex)[1];
		linha = buffReader.readLine();
		portServ = Integer.parseInt(linha.split(regex)[1]);
		reader.close();

	}

	//
	public void setConexao(Socket conexao) {
		this.conexao = conexao;
	}

	public Socket getConexao() {
		return conexao;
	}

	public void setEscuta(ServerSocket escuta) {
		this.escuta = escuta;
	}

	public ServerSocket getEscuta() {
		return escuta;
	}
}
