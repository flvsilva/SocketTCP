package dev;

//TODO Tem q definir coisas aki ainda...

@SuppressWarnings("serial")
public class Estrutura implements java.io.Serializable, Padrao {
	
	protected byte codOp;
	protected String nome;
	protected String ip;
	protected int porta;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPorta() {
		return porta;
	}

	public void setPorta(int porta) {
		this.porta = porta;
	}

	
	public Estrutura(String nome, int porta, String ip) { // apenas para facilitar
		this.nome = nome;
		this.porta = porta;
		this.ip = ip;
	}/*

	public String toString() { // idem, apenas para facilitar
		return "\r\n Usuário = " + nome + " - porta = " +  porta + " - IP = " + ip;
	}*/

	@Override
	public byte getCodOp() {
		// TODO Auto-generated method stub
		return codOp;
	}

	@Override
	public void setCodOp(byte codOp) {
		// TODO Auto-generated method stub
		this.codOp = codOp;
	}
}
