import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import com.google.gson.Gson;

import db_alunos.Aluno;
import db_turmas.Turma;

public class TesteGit {

	public static void main(String[] args) throws Exception {
		BufferedReader leitorAlunos = new BufferedReader(new FileReader("student.data"));
		Aluno a;
		String linha = "";
		String gravar = "";
		Gson gson = new Gson();

		int id = 4;
		Integer n = 2;

		try {
			while (true) {
				linha = leitorAlunos.readLine();
				a = gson.fromJson(linha, Aluno.class);
				if (a.idAluno == id) {
					System.out.println("Entrei no if");
					if (a.listaDeTurmas.contains(n)) {
						System.out.println("Entrei no outro if");
						a.listaDeTurmas.remove(n);
						gravar = gson.toJson(a);
					}

					System.out.println("ID: " + a.idAluno);
					System.out.println("Nome: " + a.nomeAluno);
					System.out.print("Turmas: ");
					for (Integer i : a.listaDeTurmas) {
						System.out.print(i + " ");
					}
					System.out.println("\n");
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		// Passa as linhas dos registros para uma lista auxiliar, menos o registro a ser
		// excluído
		ArrayList<String> aux = new ArrayList<String>();
		BufferedReader leitor2 = new BufferedReader(new FileReader("student.data"));
		Aluno a2;
		try {
			linha = "";
			int c = 0;
			while (true) {
				linha = leitor2.readLine();
				if (linha == null)
					break;
				a2 = gson.fromJson(linha, Aluno.class);
				if (a2.idAluno != id) {
					System.out.println("Vou botar..." + ++c);
					aux.add(linha);
				}
			}
			leitor2.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		// Grava os registros de volta no arquivo, sem a linha a ser excluída
		FileWriter commit = new FileWriter("student.data");
		for (int i = 0; i < aux.size(); i++) {
			System.out.println("Gravando..." + i);
			commit.write(aux.get(i) + "\n");
		}
		System.out.println("Gravar: " + gravar);
		commit.append(gravar + "\n");

	}

}
