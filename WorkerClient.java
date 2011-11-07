
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WorkerClient extends Thread {

	private Socket sock;

	public WorkerClient(Socket s) { // recebe o socket ativo no construtor
		sock = s;
	}

	/*
	 * 0 - mensagem
	 * 
	 * 1 - Registrar no servidor 2 - Registro de usuario Ok 3 - Registro de
	 * usuario falhado
	 * 
	 * 4 - pegar info usuario 5 - Receber info usuario 6 - Usuario não existe
	 * 
	 * 7 - Inicia chat usuario 8 - Usuario ocupado 9 - Chat OK (aceito)
	 * 
	 * 10- Fechar chat com usuario 11 - Sair do servidor de nomes
	 * 
	 * 
	 * 20 - Refresh servidor de nomes 21 - ACK (Estou vivo)
	 */

	public void run() {
		try {

			while (true) {

				ObjectOutputStream out = new ObjectOutputStream(
						sock.getOutputStream());
				
				ObjectInputStream in = new ObjectInputStream(
						sock.getInputStream());
				
				Estrutura objRecebido = (Estrutura) in.readObject();

				if (objRecebido.getCodOp() == 0) {
					System.out.println(" -------> "+ " disse: " + objRecebido.getMsg());
				}

				if (objRecebido.getCodOp() == 7) {

					if (Cliente.Ocupado != 1) {
						Socket sock2 = new Socket(objRecebido.getIp(),
								objRecebido.getPorta());
						Estrutura mensagem = new Estrutura((byte) 9,
								objRecebido.getNome(), objRecebido.getPorta(),
								objRecebido.getIp());

						out.writeObject(mensagem);
						out.flush();
						Escrevendo esc = new Escrevendo(sock2, objRecebido);
						esc.start();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
