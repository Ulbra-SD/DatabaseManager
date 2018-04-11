package sgbd;

import java.io.*;
import java.util.*;
import java.net.*;
import com.google.gson.Gson;

import db_alunos.*;
import db_turmas.*;
import config.*;

public class Manager {

	public static void main(String[] args) {
		ArrayList<String> reqAlunos = new ArrayList<String>();
		ArrayList<String> reqTurmas = new ArrayList<String>();
		Gson gson = new Gson();

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
						Aluno a;
						//Turma t;

						if (reqAlunos.contains(tipoRequisicao)) { // Banco Alunos
							try {
								Socket clientDBA = new Socket("localhost", 1236);
								BufferedReader entradaDBA = new BufferedReader(new InputStreamReader(clientDBA.getInputStream()));
								PrintStream saidaDBA = new PrintStream(clientDBA.getOutputStream());

								saidaDBA.println(requisicao);
								String resposta = entradaDBA.readLine();

								a = gson.fromJson(resposta, Aluno.class);

								if (tipoRequisicao.equals("aluno")) {	// Busca os dados de cada turma do aluno
									try {
										Socket clientDBT = new Socket("localhost", 1237);
										BufferedReader entradaDBT = new BufferedReader(new InputStreamReader(clientDBT.getInputStream()));
										PrintStream saidaDBT = new PrintStream(clientDBT.getOutputStream());
										String reqT = "";
										String respT = "";
										
										for (Integer i : a.listaDeTurmas) {
											reqT = ("/turma/" + i);
											System.out.println("Buscando..." + reqT);
											saidaDBT.println(reqT);
											respT = entradaDBT.readLine();
											
										}

									} catch (Exception e) {
										// TODO: handle exception
									}
								}
								saida.println(resposta);

								entradaDBA.close();
								saidaDBA.close();
								clientDBA.close();
							} catch (Exception e) {
								// TODO: handle exception
							}

						} else if (reqTurmas.contains(tipoRequisicao)) { // Banco Turmas
							try {
								Socket clientDBT = new Socket("localhost", 1237);
								BufferedReader entradaDBT = new BufferedReader(new InputStreamReader(clientDBT.getInputStream()));
								PrintStream saidaDBT = new PrintStream(clientDBT.getOutputStream());

								saidaDBT.println(requisicao);
								String resposta = entradaDBT.readLine();

								// saida.println(resposta);

								entradaDBT.close();
								saidaDBT.close();
								clientDBT.close();
							} catch (Exception e) {
								// TODO: handle exception
							}
						} else {
							String resposta = gson.toJson(CodigosRetorno.erroReqInvalida);
							saida.println(resposta);
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
