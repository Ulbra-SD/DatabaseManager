package db_alunos;

import java.io.*;
import java.net.Socket;
import java.util.*;

import db_alunos.DBAlunos;
import com.google.gson.Gson;

import config.CodigosRetorno;

public class AtendenteAlunos extends Thread {

	public Socket client;
	public DBAlunos server;
	public Scanner entrada;
	public PrintWriter saida;
	public boolean shouldRun = true;
	public Gson gson = new Gson();

	public AtendenteAlunos(Socket socket, DBAlunos server) throws Exception {
		super("AlunoConnectionThread");
		this.client = socket;
		this.server = server;
	}

	@SuppressWarnings("static-access")
	public void run() {
		try {
			entrada = new Scanner(client.getInputStream());
			saida = new PrintWriter(client.getOutputStream());

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

				case "incluialuno": // Inclusão de aluno
					int codInclui = Integer.parseInt(arrayReq[2]);
					String nome = arrayReq[3];
					String[] arrayReqTurmas = arrayReq[4].split(",");
					ArrayList<Integer> lista = new ArrayList<Integer>();
					for (String i : arrayReqTurmas) {
						lista.add(Integer.parseInt(i));
					}

					try {
						String resposta = server.incluiAluno(codInclui, nome, lista);
						saida.println(resposta);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				case "aluno": // Busca de aluno
					int codBusca = Integer.parseInt(arrayReq[2]);

					try {
						String resposta = server.buscaAluno(codBusca);
						saida.println(resposta);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				case "alunos": // Listagem de alunos
					try {
						String resposta = server.listaAlunos();
						saida.println(resposta);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				case "apagaaluno": // Exclusão de aluno
					int codExclui = Integer.parseInt(arrayReq[2]);

					try {
						String resposta = server.apagaAluno(codExclui);
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
