
import java.net.ServerSocket;
import java.net.Socket;

public class NameServer {
		
	// para deixer mais claro, estamos nos "livrando" das excecoes
	public static void main(String[] args) throws Exception {
		
		int porta = Integer.parseInt(args[0]); //TODO porta passada em linha de comando args[0]
		// criando o socket passivo, para receber conexoes
		// e ja coloca o mesmo para "listen" (poderia fazer isso explicitmente)
		
		ServerSocket ss;
		ss = new ServerSocket(porta);

		System.out.println("Server is listening ..." + porta);
		// loop: aceita conexao...
		// cria Worker pasasndo o socket ativo
		// inicia a thread do Worker
		// volta para aguardar nova conexao
		while (true) {			
			Socket as = ss.accept(); // socket ativo e criado
			System.out.println("Nova conexao estabelecida ...");
			WorkerNS w = new WorkerNS(as);
			w.start();
			
			System.out.println("Server aguardando nova conexao ....");
		}
	}


	
}
	