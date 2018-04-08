import java.util.*;
import java.net.*;
import java.io.*;
//import db_alunos.*;
//import db_turmas.*;

public class Cliente {

	public static void main(String[] args) {
		//DBAlunos dbAlunos = new DBAlunos();
		//DBTurmas dbTurmas = new DBTurmas();
		try {
			Socket client = new Socket("localhost", 1236);
			BufferedReader entrada = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintStream saida = new PrintStream(client.getOutputStream());
			Scanner teclado = new Scanner(System.in);
			System.out.println("===== Digite as requisições conforme o protocolo: =====");
			while (true) {
				System.out.print(">> ");
				String requisicao = teclado.nextLine();
				String[] arrayReq = requisicao.split("/");
				String tipoRequisicao = arrayReq[1].toLowerCase();

				if (tipoRequisicao.equals("incluialuno") || tipoRequisicao.equals("apagaaluno") || tipoRequisicao.equals("aluno")
						|| tipoRequisicao.equals("alunos")) {
					System.out.println("A enviar: " + requisicao);
					saida.println(requisicao);
				}

				/*if (tipoRequisicao.equals("incluiturma") || tipoRequisicao.equals("apagaturma") || tipoRequisicao.equals("turma")
						|| tipoRequisicao.equals("turmas")) {
					System.out.println("A enviar: " + requisicao);
					saida.println(requisicao);
				}*/
				
				/*switch (tipoRequisicao) {

				case "incluialuno": // Inclusão de aluno
					int cod = Integer.parseInt(arrayReq[2]);
					String nome = arrayReq[3];
					String[] arrayReqTurmas = arrayReq[4].split(",");
					for (String i : arrayReqTurmas) {
						lista.add(Integer.parseInt(i));
					}

					try {
						dbAlunos.incluiAluno(cod, nome, lista);
					} catch (Exception e) {
						e.printStackTrace();
					}

				case "apagaaluno": // Exclusão de aluno

				case "aluno": // Busca de aluno

				case "alunos": // Busca de TODOS os alunos

				case "incluiturma": // Inclusão de turma

				case "apagaturma": // Exclusão de turma

				case "turma": // Busca de turma

				case "turmas": // Busca de TODAS as turmas

				}*/

				if ("FIM".equals(requisicao)) {
					break;
				}

				String resposta = entrada.readLine();

				System.out.println(resposta);
			}
			entrada.close();
			saida.close();
			client.close();
			teclado.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

		// Quebra os parâmetros da requisição

		// requisicao.close();

		/*
		 * System.out.println(tipoRequisicao); System.out.println(cod);
		 * System.out.println(nome); for (String s : arrayReqTurmas) {
		 * System.out.println(s); }
		 */

	}

}
