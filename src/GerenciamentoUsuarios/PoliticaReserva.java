package GerenciamentoUsuarios;

public interface PoliticaReserva {
    // Retorna true se o 'solicitante' tem permissão para roubar a sala do 'donoAtual'
    boolean verificarPermissao(Usuario solicitante, Usuario donoAtual);
}
