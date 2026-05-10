package GerenciamentoUsuarios;

public class PoliticaPrioridade implements PoliticaReserva {
    @Override
    public boolean verificarPermissao(Usuario solicitante, Usuario donoAtual) {
        // Professor tem prioridade sobre aluno
        if (solicitante.getTipo().equalsIgnoreCase("professor") && donoAtual.getTipo().equalsIgnoreCase("aluno")) {
            return true; 
        }
        // Quem reservou primeiro continua com a reserva nos demais casos
        return false; 
    }
}
