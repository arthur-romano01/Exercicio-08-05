package Notificações;

import GerenciamentoUsuarios.Usuario;

public interface Observer {
    // Modelo Pull (já existente)
    void update(Subject s);
    
    // Modelo Push (novo - envia os dados diretamente)
    void update(String mensagem, Usuario envolvido);
}
