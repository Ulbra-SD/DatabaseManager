package config;

public class CodigosRetorno {

	// Requisi��o foi executada com sucesso (inclus�o/busca/exclus�o)
	public static Retorno requisicaoOK = new Retorno(0, "Requisicao OK");
	
	// Quando tenta-se cadastrar um aluno ou turma com o mesmo ID
	public static Retorno erroJaCadastrado = new Retorno(1, "Registro ja cadastrado");
	
	// Quando tenta-se cadastrar um aluno em uma turma que n�o existe
	public static Retorno erroRelacionamento = new Retorno(2, "Erro de Relacionamento");
	
	// Algum dos componentes dos quais o servi�o necessita n�o est� dispon�vel no momento
	public static Retorno erroIndisponivel = new Retorno(3, "Servidor Indisponivel");
	
	// Quando se tenta consultar um registro que n�o existe
	public static Retorno erroNaoEncontrado = new Retorno(4, "Registro nao encontrado");

	// Quando � feita uma requisi��o que o servidor n�o entende
	public static Retorno erroReqInvalida = new Retorno(5, "Requisicao Invalida");

}
