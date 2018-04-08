package config;

public class CodigosRetorno {

	// Requisição foi executada com sucesso (inclusão/busca/exclusão)
	public static Retorno requisicaoOK = new Retorno(0, "Requisicao OK");
	
	// Quando tenta-se cadastrar um aluno ou turma com o mesmo ID
	public static Retorno erroJaCadastrado = new Retorno(1, "Registro ja cadastrado");
	
	// Quando tenta-se cadastrar um aluno em uma turma que não existe
	public static Retorno erroRelacionamento = new Retorno(2, "Erro de Relacionamento");
	
	// Algum dos componentes dos quais o serviço necessita não está disponível no momento
	public static Retorno erroIndisponivel = new Retorno(3, "Servidor Indisponivel");
	
	// Quando se tenta consultar um registro que não existe
	public static Retorno erroNaoEncontrado = new Retorno(4, "Registro nao encontrado");

	// Quando é feita uma requisição que o servidor não entende
	public static Retorno erroReqInvalida = new Retorno(5, "Requisicao Invalida");

}
