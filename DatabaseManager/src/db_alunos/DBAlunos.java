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
				// System.out.println("Criei essa bosta!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ServerSocket server = new ServerSocket(6543);
			System.out.println("DBAlunos aguardando conexão...");
			try {
				Socket client = server.accept();
				BufferedReader entrada = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintStream saida = new PrintStream(client.getOutputStream());
				String requisicao;

				while (true) {
					requisicao = entrada.readLine();
					String[] arrayReq = requisicao.split("/");
					String tipoRequisicao = arrayReq[1].toLowerCase();

					switch (tipoRequisicao) {

					case "incluialuno": // Inclusão de aluno
						int cod = Integer.parseInt(arrayReq[2]);
						String nome = arrayReq[3];
						String[] arrayReqTurmas = arrayReq[4].split(",");
						ArrayList<Integer> lista = new ArrayList<Integer>();
						for (String i : arrayReqTurmas) {
							lista.add(Integer.parseInt(i));
						}

						try {
							System.out.println("Vou mandar incluir...");
							String resposta = incluiAluno(cod, nome, lista);
							saida.println(resposta);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if ("FIM".equals(requisicao)) {
						break;
					}
					// saida.println("Req. recbida: " + requisicao);
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
		// JFrame msgRetorno = new JFrame();
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
				System.out.println("Vou verificar se o aluno existe...");
				// a = (Aluno)leitor.readObject();
				linha = leitor.readLine();
				a = gsonReader.fromJson(linha, Aluno.class);
				System.out.println("id recebida por param.: " + id);
				System.out.println("id obj do arquivo: " + a.idAluno);
				if (a.idAluno == id) {
					System.out.println("Ei! Aluno existe!");
					alunoExiste = true;
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("FIM DO ARQUIVO!");
		}

		if (!alunoExiste) {
			System.out.println("De boa. Aluno não existe.");
			Aluno aluno = new Aluno(id, nome, lista);
			// commit.writeObject(aluno);
			String gravar = gsonWriter.toJson(aluno);
			commit.append(gravar + "\n");
			commit.close();

			// JOptionPane.showMessageDialog(msgRetorno, "{codRetorno: 0, descricaoRetorno:
			// Requisicao OK}");
			resposta = ("{codRetorno: 0, descricaoRetorno: Requisicao OK}");
		} else {
			resposta = ("{codRetorno: 1, descricaoRetorno: Registro já cadastrado}");
		}

		leitor.close();
		return resposta;

	}

}
