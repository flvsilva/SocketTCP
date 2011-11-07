
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Escrevendo extends Thread {
	private Socket sock;
	private Estrutura p;
	public Escrevendo(Socket s, Estrutura papo) {
		sock = s;
		p = papo;
	}

	public void run() {
		try {
			//ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
			System.out.println(sock);
			
			//Estrutura objRecebido = (Estrutura) in.readObject();
			Cliente.Ocupado = 1;
			System.out.println("-----------------------------------------");
			System.out.println("----- Sessão de BATE-PAPO iniciada -----");
			System.out.println("----> " + sock);
			
			InetAddress e = sock.getInetAddress();
			int pt = sock.getPort();
			//sock.close();
			while (true) {

				Socket xgh = new Socket(e, pt);
				ObjectOutputStream out = new ObjectOutputStream(xgh.getOutputStream());
				BufferedReader tecl = new BufferedReader(new InputStreamReader(System.in));
				
				System.out.println("Digite uma mensagem: ");
				
				String msg = tecl.readLine();				
				if (msg == "") {
					break;
				} else {
					Estrutura mensagem = new Estrutura((byte) 0,
							p.getNome(), p.getPorta(),
							p.getIp(), msg);	// envio a msg com MEUS dados (ip, nome e porta).
					System.out.println("Enviando: " + msg);
					out.writeObject(mensagem);
					out.flush();
				}
			}
			
			System.out.println("Fim de papo :)");
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
