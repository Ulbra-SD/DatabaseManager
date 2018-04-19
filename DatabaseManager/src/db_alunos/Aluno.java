package db_alunos;

import java.util.ArrayList;

public class Aluno {

	public int idAluno;
	public String nomeAluno;
	public ArrayList<Integer> listaDeTurmas;

	public Aluno(int id, String nome, ArrayList<Integer> lista) {
		this.idAluno = id;
		this.nomeAluno = nome;
		this.listaDeTurmas = lista;
	}

	public int getIdAluno() {
		return idAluno;
	}

	public void setIdAluno(int idAluno) {
		this.idAluno = idAluno;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public ArrayList<Integer> getListaDeTurmas() {
		return listaDeTurmas;
	}

	public void setListaDeTurmas(ArrayList<Integer> listaDeTurmas) {
		this.listaDeTurmas = listaDeTurmas;
	}
	
}
