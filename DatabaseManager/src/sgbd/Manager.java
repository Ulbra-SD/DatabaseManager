package sgbd;

import java.io.*;
import java.util.*;
import java.net.*;

import config.*;

public class Manager {

	public static void main(String[] args) {
		ArrayList<String> reqAlunos = new ArrayList<String>();
		ArrayList<String> reqTurmas = new ArrayList<String>();

		reqAlunos.add("incluialuno");
		reqAlunos.add("apagaaluno");
		reqAlunos.add("aluno");
		reqAlunos.add("alunos");

		reqTurmas.add("incluiturma");
		reqTurmas.add("apagaturma");
		reqTurmas.add("turma");
		reqTurmas.add("turmas");

		try {
			while (true) {
				ServerSocket server = new ServerSocket(1235);
				System.out.println("SGBD aguardando conexão...");
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

						if (reqAlunos.contains(tipoRequisicao)) {
							try {
								Socket clientDB = new Socket("localhost", 1236);
								BufferedReader entradaDB = new BufferedReader(new InputStreamReader(clientDB.getInputStream()));
								PrintStream saidaDB = new PrintStream(clientDB.getOutputStream());
								
								saidaDB.println(requisicao);
								String resposta = entradaDB.readLine();
								
								saida.println(resposta);
								
								entradaDB.close();
								saidaDB.close();
								clientDB.close();
							} catch (Exception e) {
								// TODO: handle exception
							}
							
						} else if (reqTurmas.contains(tipoRequisicao)) {
							try {
								Socket clientDB = new Socket("localhost", 1237);
								BufferedReader entradaDB = new BufferedReader(new InputStreamReader(clientDB.getInputStream()));
								PrintStream saidaDB = new PrintStream(clientDB.getOutputStream());
								
								saidaDB.println(requisicao);
								String resposta = entradaDB.readLine();
								
								saida.println(resposta);
								
								entradaDB.close();
								saidaDB.close();
								clientDB.close();
							} catch (Exception e) {
								// TODO: handle exception
							}
						} else {
							saida.println(CodigosRetorno.erroReqInvalida);
						}

						if ("FIM".equals(requisicao)) {
							break;
						}
					}
					client.close();
					entrada.close();
					saida.close();
					
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
