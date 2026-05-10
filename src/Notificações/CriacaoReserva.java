package Notificações;

import java.time.LocalDateTime;
import CriacaoSalas.Sala;
import GerenciamentoUsuarios.Usuario;

public class CriacaoReserva extends Subject {
    private Sala sala;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Usuario usuario;

    public CriacaoReserva(Sala sala, LocalDateTime dataInicio, LocalDateTime dataFim, Usuario usuario) {
        this.sala = sala;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.usuario = usuario;
    }

    public void criar() {
        // Modelo Pull
        notifyObservers();
        // Modelo Push
        notifyObserversPush("Uma nova reserva foi CRIADA na sala " + sala.getNome() + ".", usuario);
    }

    public Sala getSala() { return sala; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public Usuario getUsuario() { return usuario; }

    public String getDetalhes() {
        return "Reserva criada na sala: " + sala.getNome() + " de " + dataInicio + " até " + dataFim;
    }
}
