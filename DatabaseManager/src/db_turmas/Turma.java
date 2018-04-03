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

	public int getIdTurma() {
		return idTurma;
	}

	public void setIdTurma(int idTurma) {
		this.idTurma = idTurma;
	}

	public String getNomeTurma() {
		return nomeTurma;
	}

	public void setNomeTurma(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}
	
	
}
