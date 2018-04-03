package db_alunos;

import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.Gson;

public class DBAlunos {

	public static void main(String[] args) {
		File arqAlunos = new File("student.data");
		try {
			if (!arqAlunos.exists()) {
				arqAlunos.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ServerSocket server = new ServerSocket(1236);
			System.out.println("DBAlunos aguardando conexão...");
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

					case "incluialuno": // Inclusão de aluno
						int codInclui = Integer.parseInt(arrayReq[2]);
						String nome = arrayReq[3];
						String[] arrayReqTurmas = arrayReq[4].split(",");
						ArrayList<Integer> lista = new ArrayList<Integer>();
						for (String i : arrayReqTurmas) {
							lista.add(Integer.parseInt(i));
						}

						try {
							String resposta = incluiAluno(codInclui, nome, lista);
							saida.println(resposta);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
						
					case "aluno": // Busca de aluno
						int codBusca = Integer.parseInt(arrayReq[2]);
						
						try {
							String resposta = buscaAluno(codBusca);
							saida.println(resposta);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
						
					case "alunos": // Listagem de alunos
						try {
							String resposta = listaAlunos();
							saida.println(resposta);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
						
					case "apagaaluno": // Exclusão de aluno
						int codExclui = Integer.parseInt(arrayReq[2]);
						
						try {
							String resposta = apagaAluno(codExclui);
							saida.println(resposta);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
						
					default: System.out.println("Comando Invalido!"); break;
					}

					if ("FIM".equals(requisicao)) {
						break;
					}
				}
				entrada.close();
				saida.close();
				client.close();
				server.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// Método para cadastro de aluno
	public static String incluiAluno(int id, String nome, ArrayList<Integer> lista) throws Exception {
		BufferedWriter commit = new BufferedWriter(new FileWriter("student.data", true));
		BufferedReader leitor = new BufferedReader(new FileReader("student.data"));
		Aluno a;
		boolean alunoExiste = false;
		String linha = "";
		String resposta;
		Gson gsonReader = new Gson();
		Gson gsonWriter = new Gson();

		try {
			while (true) {
				linha = leitor.readLine();
				a = gsonReader.fromJson(linha, Aluno.class);
				if (a.idAluno == id) {
					System.out.println("Ei! Aluno existe!");
					alunoExiste = true;
					break;
				}
			}
		} catch (Exception e) {
			//System.out.println("FIM DO ARQUIVO!");
		}

		if (!alunoExiste) {
			System.out.println("De boa. Aluno não existe.");
			Aluno aluno = new Aluno(id, nome, lista);
			String gravar = gsonWriter.toJson(aluno);
			commit.append(gravar + "\n");
			commit.close();

			resposta = ("{codRetorno: 0, descricaoRetorno: Requisicao OK}");
		} else {
			resposta = ("{codRetorno: 1, descricaoRetorno: Registro já cadastrado}");
		}

		leitor.close();
		return resposta;

	}

	// Método para busca de aluno
	public static String buscaAluno(int id) throws Exception {
		BufferedReader leitor = new BufferedReader(new FileReader("student.data"));
		Aluno a = null;
		boolean alunoExiste = false;
		String linha = "";
		String resposta;
		Gson gsonReader = new Gson();
		
		try {
			while (true) {
				linha = leitor.readLine();
				a = gsonReader.fromJson(linha, Aluno.class);
				if (a.idAluno == id) {
					alunoExiste = true;
					break;
				}
			}
		} catch (Exception e) {
			//System.out.println("FIM DO ARQUIVO!");
		}
		
		if (alunoExiste) {
			resposta = ("{idAluno: " + a.idAluno + ", nomeAluno: " + a.nomeAluno + ", turmas: " + a.listaDeTurmas + "}");
		} else {
			resposta = ("Aluno NÃO cadastrado!");
		}

		leitor.close();
		return resposta;

	}

	// Método para listagem de TODOS os aluno
	public static String listaAlunos() throws Exception {
		BufferedReader leitor = new BufferedReader(new FileReader("student.data"));
		String linha = "";
		String resposta = ("{   \"alunos\": [   ");
		
		try {
			while (true) {
				linha = leitor.readLine();
				if (linha == null)
					break;
				resposta += (linha + ",  ");
			}
		} catch (Exception e) {
			//System.out.println("FIM DO ARQUIVO!");
		}
		
		resposta += ("   ]   }");
		
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
		Gson gsonReader = new Gson();
		
		try {
			while (true) {
				linha = leitor.readLine();
				a = gsonReader.fromJson(linha, Aluno.class);
				if (a.idAluno == id) {
					alunoExiste = true;
					break;
				}
			}
		} catch (Exception e) {
			//System.out.println("FIM DO ARQUIVO!");
		}
		
		if(alunoExiste) {
			// Passa as linhas dos registros para uma lista auxiliar, menos o registro a ser excluído
			BufferedReader leitor2 = new BufferedReader(new FileReader("student.data"));
			try {
				while(true) {
					linha = leitor2.readLine();
					if (linha == null)
						break;
					System.out.println(linha);
					a = gsonReader.fromJson(linha, Aluno.class);
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
			for(int i=0; i<aux.size(); i++) {
				commit.write(aux.get(i) + "\n");
			}
			
			commit.close();
			resposta = ("{codRetorno: 0, descricaoRetorno: Requisicao OK}");
		} else {
			resposta = ("Aluno NÃO cadastrado!");
		}
		
		leitor.close();
		return resposta;
	}
}
