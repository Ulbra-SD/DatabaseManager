package db_turmas;

import java.util.ArrayList;

import sgbd.AlunoAuxT;

public class TurmaAux {
	public int idTurma;
	public String nomeTurma;
	public ArrayList<AlunoAuxT> alunos;
	
	public TurmaAux(int id, String nome, ArrayList<AlunoAuxT> alunos) {
		this.idTurma = id;
		this.nomeTurma = nome;
		this.alunos = alunos;
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
	
	public ArrayList<AlunoAuxT> getAlunos() {
		return alunos;
	}
	
	public void setAlunos(ArrayList<AlunoAuxT> alunos) {
		this.alunos = alunos;
	}
	
	public void addAluno(AlunoAuxT a) {
		try {
			alunos.add(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
