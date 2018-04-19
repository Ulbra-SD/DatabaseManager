package db_turmas;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import com.google.gson.Gson;

import config.*;
import db_alunos.Aluno;
import db_alunos.DBAlunos;

public class DBTurmas {
	
	ServerSocket server;
	boolean shouldRun = true;

	public static void main(String[] args) {
		
		File arqAlunos = new File("class.data");
		if (!arqAlunos.exists()) {
			try {
				arqAlunos.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		new DBTurmas();
		
	}
	
	public DBTurmas() {
		try {
			server = new ServerSocket(1237);
			System.out.println("DBTurmas aguardando conexão...");
			while (shouldRun) {
				Socket client = server.accept();
				System.out.println("Conexão estabelecida com " + client.getInetAddress().getHostName() + " na porta " + client.getPort());
				AtendenteTurmas atendente = new AtendenteTurmas(client, this);
				atendente.start();
			}
		} catch (Exception e) {
		}
	}

	// Método para cadastro de turma
	public static String incluiTurma(int id, String nome) throws Exception {
		BufferedWriter commit = new BufferedWriter(new FileWriter("class.data", true));
		BufferedReader leitor = new BufferedReader(new FileReader("class.data"));
		Turma t;
		boolean turmaExiste = false;
		String linha = "";
		String resposta;
		Gson gson = new Gson();

		try {
			while (true) {
				linha = leitor.readLine();
				t = gson.fromJson(linha, Turma.class);
				if (t.idTurma == id) {
					turmaExiste = true;
					break;
				}
			}
		} catch (Exception e) {
			// System.out.println("FIM DO ARQUIVO!");
		}

		if (!turmaExiste) {
			Turma turma = new Turma(id, nome);
			String gravar = gson.toJson(turma);
			commit.append(gravar + "\n");
			commit.close();

			resposta = gson.toJson(CodigosRetorno.requisicaoOK);
		} else {
			resposta = gson.toJson(CodigosRetorno.erroJaCadastrado);
		}

		leitor.close();
		return resposta;

	}

	// Método para busca de turma
	public static String buscaTurma(int id) throws Exception {
		BufferedReader leitor = new BufferedReader(new FileReader("class.data"));
		Turma t = null;
		boolean turmaExiste = false;
		String linha = "";
		String resposta;
		Gson gson = new Gson();

		try {
			while (true) {
				linha = leitor.readLine();
				t = gson.fromJson(linha, Turma.class);
				if (t.idTurma == id) {
					turmaExiste = true;
					break;
				}
			}
		} catch (Exception e) {
			// System.out.println("FIM DO ARQUIVO!");
		}

		if (turmaExiste) {
			resposta = gson.toJson(t);
		} else {
			resposta = gson.toJson(CodigosRetorno.erroNaoEncontrado);
		}

		leitor.close();
		return resposta;

	}

	// Método para listagem de TODOS as turmas
	public static String listaTurmas() throws Exception {
		BufferedReader leitor = new BufferedReader(new FileReader("class.data"));
		Turma t;
		ArrayList<Turma> turmas = new ArrayList<Turma>();
		Gson gson = new Gson();
		String linha = "";

		while (true) {
			try {
				linha = leitor.readLine();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (linha == null)
				break;
			t = gson.fromJson(linha, Turma.class);
			turmas.add(t);
		}

		String resposta = gson.toJson(turmas);

		leitor.close();
		return resposta;
	}

	// Método para exclusão de turma
	public static String apagaTurma(int id) throws Exception {
		ArrayList<String> aux = new ArrayList<String>();
		BufferedReader leitor = new BufferedReader(new FileReader("class.data"));
		Turma t = null;
		boolean turmaExiste = false;
		String linha = "";
		String resposta;
		Gson gson = new Gson();

		try {
			while (true) {
				linha = leitor.readLine();
				t = gson.fromJson(linha, Turma.class);
				if (t.idTurma == id) {
					turmaExiste = true;
					break;
				}
			}
		} catch (Exception e) {
			// System.out.println("FIM DO ARQUIVO!");
		}

		if (turmaExiste) {
			// Passa as linhas dos registros para uma lista auxiliar, menos o registro a ser
			// excluído
			BufferedReader leitor2 = new BufferedReader(new FileReader("class.data"));
			try {
				while (true) {
					linha = leitor2.readLine();
					if (linha == null)
						break;
					t = gson.fromJson(linha, Turma.class);
					if (t.idTurma != id) {
						aux.add(linha);
					}
				}
				leitor2.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}

			// Grava os registros de volta no arquivo, sem a linha a ser excluída
			FileWriter commit = new FileWriter("class.data");
			for (int i = 0; i < aux.size(); i++) {
				commit.write(aux.get(i) + "\n");
			}

			commit.close();

			// Apaga a turma excluída do registro de todos os alunos que estão matriculados
			// nela
			apagaTurmaAlunos(id);

			resposta = gson.toJson(CodigosRetorno.requisicaoOK);
		} else {
			resposta = gson.toJson(CodigosRetorno.erroNaoEncontrado);
		}

		leitor.close();
		return resposta;
	}

	// Apaga a turma excluída do registro de todos os alunos que estão matriculados
	// nela
	public static void apagaTurmaAlunos(int id) throws Exception {
		BufferedReader leitorAlunos = new BufferedReader(new FileReader("student.data"));
		Aluno a = null;
		String linha = "";
		Gson gson = new Gson();
		Integer idT = id;

		try {
			while (true) {
				linha = leitorAlunos.readLine();
				a = gson.fromJson(linha, Aluno.class);
				if (a.listaDeTurmas.contains(idT)) {
					a.listaDeTurmas.remove(idT);

					DBAlunos.apagaAluno(a.idAluno);
					DBAlunos.incluiAluno(a.idAluno, a.nomeAluno, a.listaDeTurmas);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		leitorAlunos.close();
	}

}
