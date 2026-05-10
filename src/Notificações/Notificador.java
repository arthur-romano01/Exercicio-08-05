package Notificações;

import GerenciamentoUsuarios.Usuario;

public class Notificador implements Observer {
    // Implementação do modelo PULL
    @Override
    public void update(Subject s) {
        if (s instanceof CriacaoReserva) {
            CriacaoReserva cr = (CriacaoReserva) s;
            System.out.println("[PULL] Notificação: " + cr.getDetalhes() + " - Responsável: " + cr.getUsuario().getNome());
        } else if (s instanceof AlteracaoReserva) {
            AlteracaoReserva ar = (AlteracaoReserva) s;
            System.out.println("[PULL] Notificação: " + ar.getDetalhes() + " - Responsável: " + ar.getUsuario().getNome());
        } else if (s instanceof CancelamentoReserva) {
            CancelamentoReserva cr = (CancelamentoReserva) s;
            System.out.println("[PULL] Notificação: " + cr.getDetalhes() + " - Responsável: " + cr.getUsuario().getNome());
        } else {
            System.out.println("[PULL] Notificação recebida: Atualização desconhecida.");
        }
    }

    // Implementação do modelo PUSH
    @Override
    public void update(String mensagem, Usuario envolvido) {
        System.out.println("[PUSH] Notificação: " + mensagem + " - Responsável: " + envolvido.getNome());
    }
}
