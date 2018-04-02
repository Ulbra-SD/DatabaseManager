package db_turmas;

import java.io.Serializable;

public class Turma implements Serializable {
	private static final long serialVersionUID = 1L;

	public int idTurma;
	public String nomeTurma;

	public Turma(int id, String nome) {
		this.idTurma = id;
		this.nomeTurma = nome;
	}
}
