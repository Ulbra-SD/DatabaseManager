package cache_server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Cache {

	public static void main(String[] args) {
		ArrayList<String> requisicoes = new ArrayList<String>();
		ArrayList<String> reqBusca = new ArrayList<String>();

		requisicoes.add("incluialuno");
		requisicoes.add("incluiturma");
		requisicoes.add("apagaaluno");
		requisicoes.add("apagaturma");

		reqBusca.add("aluno");
		reqBusca.add("alunos");
		reqBusca.add("turma");
		reqBusca.add("turmas");

		try {
			while (true) {
				ServerSocket server = new ServerSocket(1234);
				System.out.println("Servidor Cache aguardando conexão...");
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

						// try {
						Socket clientSGDB = new Socket("localhost", 1235);
						BufferedReader entradaSGDB = new BufferedReader(new InputStreamReader(clientSGDB.getInputStream()));
						PrintStream saidaSGDB = new PrintStream(clientSGDB.getOutputStream());

						if (requisicoes.contains(tipoRequisicao)) {
							saidaSGDB.println(requisicao);
							String resposta = entradaSGDB.readLine();
							
							saida.println(resposta);

							entradaSGDB.close();
							saidaSGDB.close();
							clientSGDB.close();
						} else {
							System.out.println("...REQUISICAO DE BUSCA...");
						}
						// } catch (Exception e) {
						// // TODO: handle exception
						// }

					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				server.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
