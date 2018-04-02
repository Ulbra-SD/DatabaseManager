package db_turmas;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class DBTurmas {

	public static void main(String[] args) {

	}

	// Método para cadastro de turma
	public void incluiTurma(int id, String nome) {
		try {

			Turma turma = new Turma(id, nome);
			ObjectOutputStream commit = new ObjectOutputStream(new FileOutputStream("turmas.dat"));
			commit.writeObject(turma);
			commit.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
