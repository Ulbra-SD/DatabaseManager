package sgbd;

import java.util.ArrayList;
import db_turmas.Turma;

public class AlunoAux {
	public int idAluno;
	public String nomeAluno;
	public ArrayList<Turma> turmas;

	public AlunoAux(int id, String nome, ArrayList<Turma> turmas) {
		this.idAluno = id;
		this.nomeAluno = nome;
		this.turmas = new ArrayList<Turma>();
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
	
	public ArrayList<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(ArrayList<Turma> turmas) {
		this.turmas = turmas;
	}
	
	public void addTurma(Turma t) {
		try {
			turmas.add(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
