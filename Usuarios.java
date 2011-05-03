package dev;

import java.util.LinkedList;
import java.util.List;

public class Usuarios {

	private static List<Estrutura> usuariosLogados = new LinkedList<Estrutura>();
	public static int tamanho;
	
	public synchronized static List<Estrutura> getUsuariosLogados() {
		return usuariosLogados;
	}
	public synchronized static void addUsuariosLogados(Estrutura usuario) {
		usuariosLogados.add(usuario);
	}


}
