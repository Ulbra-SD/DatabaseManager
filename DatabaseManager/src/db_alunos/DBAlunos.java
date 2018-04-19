package db_alunos;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import com.google.gson.Gson;

import db_turmas.*;
import config.*;

public class DBAlunos {

	ServerSocket server;
	boolean shouldRun = true;

	public static void main(String[] args) {

		File arqAlunos = new File("student.data");
		if (!arqAlunos.exists()) {
			try {
				arqAlunos.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		new DBAlunos();
	}

	public DBAlunos() {
		try {
			server = new ServerSocket(1236);
			System.out.println("DBAlunos aguardando conexão...");
			while (shouldRun) {
				Socket client = server.accept();
				System.out.println("Conexão estabelecida com " + client.getInetAddress().getHostName() + " na porta "
						+ client.getPort());
				AtendenteAlunos atendente = new AtendenteAlunos(client, this);
				atendente.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Método para cadastro de aluno
	public static String incluiAluno(int id, String nome, ArrayList<Integer> lista) throws Exception {
		BufferedWriter commit = new BufferedWriter(new FileWriter("student.data", true));
		BufferedReader leitorAlunos = new BufferedReader(new FileReader("student.data"));
		BufferedReader leitorTurmas = new BufferedReader(new FileReader("class.data"));
		Aluno a;
		Turma t;
		boolean alunoExiste = false;
		boolean turmaExiste = false;
		String linha = "";
		String resposta;
		Gson gson = new Gson();

		try {
			while (true) {
				linha = leitorAlunos.readLine();
				a = gson.fromJson(linha, Aluno.class);
				if (a.idAluno == id) {
					alunoExiste = true;
					break;
				}
			}
		} catch (Exception e) {
			// System.out.println("FIM DO ARQUIVO!");
		}

		try {
			linha = "";
			for (Integer i : lista) {
				turmaExiste = false;
				while (true) {
					linha = leitorTurmas.readLine();
					t = gson.fromJson(linha, Turma.class);
					if (t.idTurma == i) {
						turmaExiste = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		// --------------------------------------
		if (!alunoExiste && turmaExiste) {
			Aluno aluno = new Aluno(id, nome, lista);
			String gravar = gson.toJson(aluno);
			commit.append(gravar + "\n");
			commit.close();

			resposta = gson.toJson(CodigosRetorno.requisicaoOK);
		} else if (!alunoExiste && !turmaExiste) {
			resposta = gson.toJson(CodigosRetorno.erroRelacionamento);
		} else {
			resposta = gson.toJson(CodigosRetorno.erroJaCadastrado);
		}

		leitorAlunos.close();
		leitorTurmas.close();
		return resposta;

	}

	// Método para busca de aluno
	public static String buscaAluno(int id) throws Exception {
		BufferedReader leitor = new BufferedReader(new FileReader("student.data"));
		Aluno a = null;
		boolean alunoExiste = false;
		String linha = "";
		String resposta;
		Gson gson = new Gson();

		try {
			while (true) {
				linha = leitor.readLine();
				a = gson.fromJson(linha, Aluno.class);
				if (a.idAluno == id) {
					alunoExiste = true;
					break;
				}
			}
		} catch (Exception e) {
			// System.out.println("FIM DO ARQUIVO!");
		}

		if (alunoExiste) {
			resposta = gson.toJson(a);
		} else {
			resposta = gson.toJson(CodigosRetorno.erroNaoEncontrado);
		}

		leitor.close();
		return resposta;

	}

	// Método para listagem de TODOS os alunos
	public static String listaAlunos() throws Exception {
		BufferedReader leitor = new BufferedReader(new FileReader("student.data"));
		Aluno a;
		ArrayList<Aluno> alunos = new ArrayList<Aluno>();
		Gson gson = new Gson();
		String linha = "";

		while (true) {
			try {
				linha = leitor.readLine();
			} catch (EOFException e) {
				// FIM DO ARQUIVO!
			}
			if (linha == null)
				break;
			a = gson.fromJson(linha, Aluno.class);
			alunos.add(a);
		}

		String resposta = gson.toJson(alunos);

		leitor.close();
		return resposta;
	}

	// Método para exclusão de aluno
	public static String apagaAluno(int id) throws Exception {
		ArrayList<String> aux = new ArrayList<String>();
		BufferedReader leitor = new BufferedReader(new FileReader("student.data"));
		Aluno a = null;
		boolean alunoExiste = false;
		String linha = "";
		String resposta;
		Gson gson = new Gson();

		try {
			while (true) {
				linha = leitor.readLine();
				a = gson.fromJson(linha, Aluno.class);
				if (a.idAluno == id) {
					alunoExiste = true;
					break;
				}
			}
		} catch (Exception e) {
			// System.out.println("FIM DO ARQUIVO!");
		}

		if (alunoExiste) {
			// Passa as linhas dos registros para uma lista auxiliar, menos o registro a ser
			// excluído
			BufferedReader leitor2 = new BufferedReader(new FileReader("student.data"));
			try {
				while (true) {
					linha = leitor2.readLine();
					if (linha == null)
						break;
					a = gson.fromJson(linha, Aluno.class);
					if (a.idAluno != id) {
						aux.add(linha);
					}
				}
				leitor2.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}

			// Grava os registros de volta no arquivo, sem a linha a ser excluída
			FileWriter commit = new FileWriter("student.data");
			for (int i = 0; i < aux.size(); i++) {
				commit.write(aux.get(i) + "\n");
			}

			commit.close();
			resposta = gson.toJson(CodigosRetorno.requisicaoOK);
		} else {
			resposta = gson.toJson(CodigosRetorno.erroNaoEncontrado);
		}

		leitor.close();
		return resposta;
	}

}
