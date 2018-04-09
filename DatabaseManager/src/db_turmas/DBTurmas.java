package db_turmas;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import com.google.gson.Gson;

import config.*;
import db_alunos.Aluno;
import db_alunos.DBAlunos;

public class DBTurmas {

	public static void main(String[] args) {
		File arqAlunos = new File("class.data");
		try {
			if (!arqAlunos.exists()) {
				arqAlunos.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			while (true) {
				ServerSocket server = new ServerSocket(1237);
				System.out.println("DBTurmas aguardando conexão...");
				try {
					Socket client = server.accept();
					System.out.println("Conexão estabelecida!");
					BufferedReader entrada = new BufferedReader(new InputStreamReader(client.getInputStream()));
					PrintStream saida = new PrintStream(client.getOutputStream());
					String requisicao;

					while (true) {
						requisicao = entrada.readLine();
						String[] arrayReq = requisicao.split("/");
						String tipoRequisicao = arrayReq[1].toLowerCase();

						switch (tipoRequisicao) {

						case "incluiturma": // Inclusão de turma
							int codInclui = Integer.parseInt(arrayReq[2]);
							String nome = arrayReq[3];

							try {
								String resposta = incluiTurma(codInclui, nome);
								saida.println(resposta);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;

						case "turma": // Busca de turma
							int codBusca = Integer.parseInt(arrayReq[2]);

							try {
								String resposta = buscaTurma(codBusca);
								saida.println(resposta);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;

						case "turmas": // Listagem de alunos
							try {
								String resposta = listaTurmas();
								saida.println(resposta);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;

						case "apagaturma": // Exclusão de turma
							int codExclui = Integer.parseInt(arrayReq[2]);

							try {
								String resposta = apagaTurma(codExclui);
								saida.println(resposta);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;

						default:
							System.out.println("Comando Invalido!");
							break;
						}

						if ("FIM".equals(requisicao)) {
							break;
						}
					}
					entrada.close();
					saida.close();
					client.close();

				} catch (Exception e) {
					// TODO: handle exception
				}
				server.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
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
			// resposta = ("{codRetorno: 0, descricaoRetorno: Requisicao OK}");
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
		String linha = "";
		String resposta = ("{   \"turmas\": [   ");

		try {
			while (true) {
				linha = leitor.readLine();
				if (linha == null)
					break;
				resposta += (linha + ",  ");
			}
		} catch (Exception e) {
			// System.out.println("FIM DO ARQUIVO!");
		}

		resposta += ("   ]   }");

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
			// TODO: handle exception
		}
		leitorAlunos.close();
	}

}
