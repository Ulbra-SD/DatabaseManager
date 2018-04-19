package sgbd;

import java.util.ArrayList; 
import java.net.*;

public class Manager {

	ServerSocket server;
	ArrayList<String> reqAlunos = new ArrayList<String>();
	ArrayList<String> reqTurmas = new ArrayList<String>();
	boolean shouldRun = true;

	public static void main(String[] args) {

		new Manager();

	}

	public Manager() {
		defineRequisicoes();	// Preenche array com as requisições válidas
		try {
			server = new ServerSocket(1235);
			System.out.println("SGBD aguardando conexão...");
			while (shouldRun) {
				Socket client = server.accept();
				System.out.println("Conexão estabelecida com " + client.getInetAddress().getHostName() + " na porta " + client.getPort());
				AtendenteSGBD atendente = new AtendenteSGBD(client, this);
				atendente.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void defineRequisicoes() {
		// Array de Requisições de Aluno
		reqAlunos.add("incluialuno");
		reqAlunos.add("apagaaluno");
		reqAlunos.add("aluno");
		reqAlunos.add("alunos");
		// Array de Requisições de Turma
		reqTurmas.add("incluiturma");
		reqTurmas.add("apagaturma");
		reqTurmas.add("turma");
		reqTurmas.add("turmas");
	}
}
