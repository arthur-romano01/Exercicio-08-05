package GerenciamentoUsuarios;

/**
 * Política de reserva "Primeiro a Chegar, Primeiro a Ser Atendido" (FCFS).
 * Independentemente do tipo de usuário (aluno ou professor), quem reservou
 * primeiro sempre mantém a sala. Nenhum solicitante possui prioridade sobre
 * o dono atual da reserva.
 */
public class PoliticaPrimeiroChegado implements PoliticaReserva {

    @Override
    public boolean verificarPermissao(Usuario solicitante, Usuario donoAtual) {
        // Ninguém pode sobrepor a reserva de quem chegou primeiro.
        return false;
    }
}
