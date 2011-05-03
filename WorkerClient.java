package dev;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WorkerClient extends Thread  {

	private Socket sock;
	
	
	public WorkerClient(Socket s) { // recebe o socket ativo no construtor
		sock = s ;
	}

	public void run() {
		try {
			ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
			String msg = (String) in.readObject();
			out.writeObject("Recebido Ok! " +msg);			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
/*
BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
//Escolhe com quem vai falar
String client;
System.out.println("Deseja se comunicar com: ");
client = teclado.readLine();
//Pega os dados do cliente... (ip + porta)
System.out.println("Cliente: "+client+" sock: "+sock);
Cliente papo = pegaCliente(client, sock);
//Cria o socket
Socket sockCliente = new Socket(papo.getip(), papo.getPort());


//inicia a thread de bate-papo =)
ObjectOutputStream out = new ObjectOutputStream(sockCliente.getOutputStream());
ObjectInputStream in = new ObjectInputStream(sockCliente.getInputStream());
String msg;
while (true){
	msg = teclado.readLine();
	if (teclado.toString() == "") {
		break;	
	} else {
		//TODO Envia msg pro cliente :)
	}
	
}
//TODO Avisa ao servidor que está livre!
sockCliente.close();
*/