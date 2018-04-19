package db_turmas;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

import config.CodigosRetorno;
import db_turmas.DBTurmas;

public class AtendenteTurmas extends Thread {

	public Socket client;
	public DBTurmas server;
	public Scanner entrada;
	public PrintStream saida;
	public boolean shouldRun = true;
	public Gson gson = new Gson();

	public AtendenteTurmas(Socket socket, DBTurmas server) throws Exception {
		super("TurmaConnectionThread");
		this.client = socket;
		this.server = server;
	}

	@SuppressWarnings("static-access")
	public void run() {
		try {
			entrada = new Scanner(client.getInputStream());
			saida = new PrintStream(client.getOutputStream());

			while (shouldRun) {
				while (!entrada.hasNextLine()) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				String requisicao = entrada.nextLine();
				String[] arrayReq = requisicao.split("/");
				String tipoRequisicao = arrayReq[1].toLowerCase();

				switch (tipoRequisicao) {

				case "incluiturma": // Inclusão de turma
					int codInclui = Integer.parseInt(arrayReq[2]);
					String nome = arrayReq[3];

					try {
						String resposta = server.incluiTurma(codInclui, nome);
						saida.println(resposta);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				case "turma": // Busca de turma
					int codBusca = Integer.parseInt(arrayReq[2]);

					try {
						String resposta = server.buscaTurma(codBusca);
						saida.println(resposta);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				case "turmas": // Listagem de alunos
					try {
						String resposta = server.listaTurmas();
						saida.println(resposta);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				case "apagaturma": // Exclusão de turma
					int codExclui = Integer.parseInt(arrayReq[2]);

					try {
						String resposta = server.apagaTurma(codExclui);
						saida.println(resposta);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				default:
					String resposta = gson.toJson(CodigosRetorno.erroReqInvalida);
					saida.println(resposta);
					break;
				}
				
				saida.flush();
			}
			
			entrada.close();
			saida.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
