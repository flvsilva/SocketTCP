package dev;

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

			ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(
					sock.getOutputStream());

			Estrutura str = (Estrutura) in.readObject();
			
			adicionaCliente(str);
			out.writeObject("Recebido Ok! ");
			
			ObjectInputStream in1 = new ObjectInputStream(sock.getInputStream());
			ObjectOutputStream out1 = new ObjectOutputStream(
					sock.getOutputStream());
			String nome = (String) in1.readObject();
			
			Estrutura cli = devolveCliente(nome);

			out1.writeObject(cli);

			sock.close();
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
