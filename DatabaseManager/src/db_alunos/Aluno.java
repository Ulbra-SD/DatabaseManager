package db_alunos;

import java.io.Serializable;
import java.util.ArrayList;
import db_turmas.Turma;

public class Aluno implements Serializable {
	private static final long serialVersionUID = 1L;

	public int idAluno;
	public String nomeAluno;
	public ArrayList<Integer> listaDeTurmas;

	public Aluno(int id, String nome, ArrayList<Integer> lista) {
		this.idAluno = id;
		this.nomeAluno = nome;
		this.listaDeTurmas = lista;
	}
}
