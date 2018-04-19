package sgbd;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import config.CodigosRetorno;
import db_alunos.Aluno;
import db_turmas.Turma;
import db_turmas.TurmaAux;

public class AtendenteSGBD extends Thread {

	public Socket client;
	public Manager server;
	public Scanner entrada;
	public PrintStream saida;
	public boolean shouldRun = true;
	public Gson gson = new Gson();

	public AtendenteSGBD(Socket socket, Manager server) throws Exception {
		super("SGBDConnectionThread");
		this.client = socket;
		this.server = server;
	}

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
				Aluno a;
				// AlunoAux aux;
				Turma t;

				/*
				 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				 */
				if (server.reqAlunos.contains(tipoRequisicao)) { // CONECTAR NO BANCO DE ALUNOS
					try {
						Socket clientDBA = new Socket("localhost", 1236);
						Scanner entradaDBA = new Scanner(clientDBA.getInputStream());
						PrintStream saidaDBA = new PrintStream(clientDBA.getOutputStream());

						saidaDBA.println(requisicao);
						String resposta = entradaDBA.nextLine();

						if (tipoRequisicao.equals("aluno")) { // Requisição de BUSCA UNITÁRIA

							a = gson.fromJson(resposta, Aluno.class);
							AlunoAux aux = buscaTurmasAluno(a); // Busca os dados de cada turma do aluno
							resposta = gson.toJson(aux); // Resultado da requisição

						} else if (tipoRequisicao.equals("alunos")) { // Requisição de BUSCA GERAL

							ArrayList<Aluno> alunos = new ArrayList<Aluno>();
							ArrayList<AlunoAux> alunosAux = new ArrayList<AlunoAux>();
							Type tipo = new TypeToken<ArrayList<Aluno>>() {}.getType();
							alunos = gson.fromJson(resposta, tipo);
							for (Aluno aluno : alunos) {
								AlunoAux aux = buscaTurmasAluno(aluno); // Busca os dados de cada turma do aluno
								alunosAux.add(aux);
							}
							resposta = gson.toJson(alunosAux); // Resultado da requisição
						}
						saida.println(resposta);

						saidaDBA.flush();
						entradaDBA.close();
						saidaDBA.close();
						clientDBA.close(); // Fecha conexão com banco de ALUNOS
					} catch (Exception e) {
						e.printStackTrace();
					}

					/*
					 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
					 */
				} else if (server.reqTurmas.contains(tipoRequisicao)) { // CONECTAR NO BANCO DE TURMAS
					try {
						Socket clientDBT = new Socket("localhost", 1237);
						Scanner entradaDBT = new Scanner(clientDBT.getInputStream());
						PrintStream saidaDBT = new PrintStream(clientDBT.getOutputStream());

						saidaDBT.println(requisicao);
						String resposta = entradaDBT.nextLine();

						if (tipoRequisicao.equals("turma")) { // Requisição de BUSCA UNITÁRIA

							t = gson.fromJson(resposta, Turma.class);
							TurmaAux auxT = buscaAlunosTurma(t); // Busca os dados de cada aluno da turma
							resposta = gson.toJson(auxT); // Resultado da requisição

						} else if (tipoRequisicao.equals("turmas")) { // Requisição de BUSCA GERAL

							ArrayList<Turma> turmas = new ArrayList<Turma>();
							ArrayList<TurmaAux> turmasAux = new ArrayList<TurmaAux>();
							Type tipo = new TypeToken<ArrayList<Turma>>() {}.getType();
							turmas = gson.fromJson(resposta, tipo);
							for (Turma turma : turmas) {
								TurmaAux auxT = buscaAlunosTurma(turma); // Busca os dados de cada aluno da turma
								turmasAux.add(auxT);
							}
							resposta = gson.toJson(turmasAux); // Resultado da requisição
						}

						saida.println(resposta);

						saidaDBT.flush();
						entradaDBT.close();
						saidaDBT.close();
						clientDBT.close();	// Fecha conexão com banco de TURMAS
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					/*
					 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
					 */
				} else {
					String resposta = gson.toJson(CodigosRetorno.erroReqInvalida);
					saida.println(resposta);
				}

				saida.flush();
			}

			entrada.close();
			saida.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public AlunoAux buscaTurmasAluno(Aluno a) {
		AlunoAux aux;
		Turma t;
		String reqT = "";
		String respT = "";
		String nomeAluno = a.nomeAluno;
		int idAluno = a.idAluno;
		ArrayList<Turma> listTurmas = null;
		// Nova classe de aluno, agora contendo os dados das turmas
		aux = new AlunoAux(idAluno, nomeAluno, listTurmas);

		for (Integer i : a.listaDeTurmas) {
			try {
				Socket clientDBT = new Socket("localhost", 1237);
				Scanner entradaDBT = new Scanner(clientDBT.getInputStream());
				PrintStream saidaDBT = new PrintStream(clientDBT.getOutputStream());

				reqT = ("/turma/" + i);
				saidaDBT.println(reqT);
				respT = entradaDBT.nextLine();
				t = gson.fromJson(respT, Turma.class);
				aux.addTurma(t);

				saidaDBT.flush();
				clientDBT.close();
				entradaDBT.close();
				saidaDBT.close(); // Fecha conexão com banco de TURMAS
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return (aux);
	}

	public TurmaAux buscaAlunosTurma(Turma t) {
		ArrayList<Aluno> alunos = new ArrayList<Aluno>();
		ArrayList<AlunoAuxT> listAlunos = new ArrayList<AlunoAuxT>();
		Integer idTurma = t.idTurma;
		String nomeTurma = t.nomeTurma;
		AlunoAuxT aux;
		TurmaAux auxT = new TurmaAux(idTurma, nomeTurma, listAlunos);
		String reqA = "";
		String respA = "";

		try {
			Socket clientDBA = new Socket("localhost", 1236);
			Scanner entradaDBA = new Scanner(clientDBA.getInputStream());
			PrintStream saidaDBA = new PrintStream(clientDBA.getOutputStream());

			reqA = ("/alunos");
			saidaDBA.println(reqA);
			respA = entradaDBA.nextLine();
			Type tipo = new TypeToken<ArrayList<Aluno>>() {
			}.getType();
			alunos = gson.fromJson(respA, tipo);

			clientDBA.close();
			entradaDBA.close();
			saidaDBA.close(); // Fecha conexão com banco de ALUNOS
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Aluno a : alunos) {
			if (a.listaDeTurmas.contains(idTurma)) {
				aux = new AlunoAuxT(a.idAluno, a.nomeAluno);
				auxT.addAluno(aux);
			}
		}

		return (auxT);
	}

}
