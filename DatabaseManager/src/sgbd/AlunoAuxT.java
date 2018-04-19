package sgbd;

public class AlunoAuxT {
	public int idAluno;
	public String nomeAluno;
	
	public AlunoAuxT(int id, String nome) {
		this.idAluno = id;
		this.nomeAluno = nome;
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

}
