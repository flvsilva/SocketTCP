
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
	private Cliente() {
		// construtor privado :).....
	}

	private Socket conexao;
	private ServerSocket escuta;
	public static String client;
	// Exp Regular pra pegar o ip válido do usuario...
	private static final String ipvalido = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	protected static String ipServ;
	protected static int portServ;
	protected static int port; // passar por args[0]
	protected static String ip;
	protected static String nome; // passar por args[1]

	public static byte Ocupado = 0;

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

			// System.out.println("Iniciando thread...");
			
			System.out.println(ipServ);
			
			
			Socket conexao = new Socket(ipServ, portServ); // cria socket com
															// NameServer

			port = Integer.parseInt(args[0]);
			nome = args[1];
			
			// cria a estrutura de dados a ser enviada (Meus dados...)
			byte codOp = 1;
			Estrutura str = new Estrutura(codOp, nome, port, ip);
			ObjectOutputStream out = new ObjectOutputStream(
					conexao.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(
					conexao.getInputStream());

			out.writeObject(str); // envia objeto para o servidor (Worker)

			Thread thread = new Cliente();
			thread.start();

			Estrutura resp = (Estrutura) in.readObject(); // recebe objeto de resposta
			out.close();
			in.close();
			conexao.close();
			if (resp.getCodOp() != 2) {
				throw new Exception("Servidor não encontrado.");
			}

			BufferedReader teclado = new BufferedReader(new InputStreamReader(
					System.in));

			// Escolhe com quem vai falar
			System.out.println("******** Menu Inicial ********");
			System.out.println("*** 1 - Iniciar bate-papo  ***");
			System.out.println("*** 2 - Sair do NS         ***");
			System.out.println("Digite sua opção: ");

			String op = teclado.readLine();
			if (op.equals("1")) { // 4!! :)
				System.out.println("Deseja se comunicar com: ");
				client = teclado.readLine();
				// Pega os dados do cliente... (ip + porta)
				conexao = new Socket(ipServ, portServ);
				Estrutura papo = pegaCliente(client, conexao);
				System.out.println("Retornou: " + papo.getIp()
						+ papo.getPorta() + papo.getNome());
				// Cria o socket
				Socket sockCliente = new Socket(papo.getIp(), papo.getPorta());
				papo.setIp(ip);
				papo.setNome(nome);
				papo.setPorta(port);
				System.out.println("Socket Criado: " + sockCliente);
				ObjectOutputStream outCliente = new ObjectOutputStream(
						sockCliente.getOutputStream());
				System.out
						.println("Tentando conexão com o usuário, por favor, aguarde...");
				Estrutura tentaConexao = new Estrutura((byte) 7, nome, port,
						ip, "Usuario livre?");
				outCliente.writeObject(tentaConexao);
				outCliente.flush();
				ObjectInputStream inCliente = new ObjectInputStream(
						sockCliente.getInputStream());
				tentaConexao = (Estrutura) inCliente.readObject();
				byte resposta = tentaConexao.codOp;
				if (resposta == 9) {
					System.out.println("Conexão aceita, iniciando BATE-PAPO");
					Escrevendo e = new Escrevendo(sockCliente, papo);
					System.out.println("BATE-PAPO iniciado...");
					e.start();

				}
			} else if (op.equals("2")) { // 7!

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * 0 - mensagem
	 * 
	 * 1 - Registrar no servidor 2 - Registro de usuario Ok 3 - Registro de
	 * usuario falhado
	 * 
	 * 4 - Get required user info 5 - Receive required user info 6 - User *
	 * doesn't exist
	 * 
	 * 7 - Inicia chat usuario 8 - Usuario ocupado 9 - Chat OK
	 * 
	 * 10- Fechar chat com usuario 11 - Sair do servidor de nomes
	 * 
	 * 20 - Refresh servidor de nomes 21 - ACK (Estou vivo)
	 */
	public static Estrutura pegaCliente(String nome, Socket sock)
			throws Exception {
		ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
		System.out.println("socket atual: " + sock);
		// envia busca ao servidor
		Estrutura str = new Estrutura((byte) 4, nome, 0, "");
		System.out.println(str.codOp);
		out.writeObject(str);
		System.out.println("Enviado pedido, aguardando resposta...");

		Estrutura resp = (Estrutura) in.readObject(); // recebe objeto de
														// resposta

		sock.close();

		if (resp.codOp == 2) {
			System.out.println("Porta dest: " + resp.porta);
			return resp;
		} else {
			System.out.println("ERRO!");
		}
		throw new Exception("Não foi possível localizar o contato!");
	}

	public void run() {
		// TODO Escutando possíveis msgs a receber...
		try {

			ServerSocket ss;
			ss = new ServerSocket(port);

			while (true) {
				Socket as = ss.accept();
				WorkerClient w = new WorkerClient(as);
				/*
				 * System.out.println("espera :)"); this.wait();
				 */

				w.start(); // lancei a thread para conexão recebida

				// System.out.println("Fim da espera :)");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
