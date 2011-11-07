
@SuppressWarnings("serial")
public class Estrutura implements java.io.Serializable, Padrao {
	
	protected byte codOp;
	protected String nome;
	protected String ip;
	protected int porta;
	protected String msg;
	@Override
	public byte getCodOp() {
		return codOp;
	}
	@Override
	public void setCodOp(byte codOp) {
		this.codOp = codOp;
	}
	
	@Override
	public String getNome() {
		return nome;
	}

	@Override
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String getIp() {
		return ip;
	}

	@Override
	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public int getPorta() {
		return porta;
	}

	@Override
	public void setPorta(int porta) {
		this.porta = porta;
	}

	@Override
	public String getMsg() {
		return msg;
	}
	
	@Override
	public void setMsg(String msg) {
		this.msg = msg;		
	}
	
	public Estrutura(byte codOp,String nome, int porta, String ip, String msg) { // Construtor.
		this.codOp = codOp;
		this.nome = nome;
		this.porta = porta;
		this.ip = ip;
		this.msg = msg;
	}
	public Estrutura(byte codOp,String nome, int porta, String ip) { // Construtor.
		this.codOp = codOp;
		this.nome = nome;
		this.porta = porta;
		this.ip = ip;
	}/*

	public String toString() { // idem, apenas para facilitar
		return "\r\n Usuário = " + nome + " - porta = " +  porta + " - IP = " + ip;
	}*/
	

}
