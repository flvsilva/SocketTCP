
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

	

public class WorkerNS extends Thread {

	private Socket sock;

	public WorkerNS(Socket s) { // recebe o socket ativo no construtor
		sock = s;
	}


	public void run() {
		try {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>Nova chamada");
			ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
			Estrutura str = (Estrutura) in.readObject();
			
			System.out.println("Codigo da operacao: "+str.codOp);
			System.out.println("CHEGOU req de: "+str.nome);
			
			if (str.codOp == 1) { // registrar novo cliente no nameserver
				adicionaCliente(str);
				Estrutura resp = new Estrutura((byte) 2, str.nome, str.porta, str.ip);
				out.writeObject(resp);	
			} else if (str.codOp == 4) { // obter dados de host alvo
				String nome = str.nome;
				Estrutura cli = devolveCliente(nome);
				System.out.println(">>>>>>>>>>>>>>>>>> "+cli.getNome()+" - "+cli.getPorta());
				cli.setCodOp((byte) 2);
				out.writeObject(cli);
			} else if (str.codOp == 3) { // apagar registro
				
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void adicionaCliente(Estrutura str) {
		// TODO Bolar uma forma de guardar todos os usuarios ativos, em uma área
		// que todas as threads acessem.
		// TODO Com arquivo txt e uma merda...

		/*
		 * FileOutputStream fos = new FileOutputStream("usuariosLogados.txt");
		 * fos.write(dados.getBytes()); fos.close();
		 */
		Usuarios.addUsuariosLogados(str);
		Usuarios.tamanho++;
		System.out.println("Usuário "+str.nome+" online!");
		System.out.println(Usuarios.todos().toArray());
		
		for (Object n : Usuarios.todos().toArray()) {
			n.toString();
			
		}
		
	}

	public Estrutura devolveCliente(String nome) {
		Estrutura host;
		String regAtual;			
		for (int n = 0; n < Usuarios.tamanho; n++) {
			regAtual = Usuarios.getUsuariosLogados().get(n).nome;
			if (regAtual.equals(nome)) {
				host = Usuarios.getUsuariosLogados().get(n);
				System.out.println("encontrado, retornando...");
				return host;
			}
		}
		//Estrutura resp = new Estrutura(nome, porta, ip);
		return null;
		// TODO Código para devover dados de dado hostname (ip e porta), tratar
		// erros (caso não exista)
	}

}
