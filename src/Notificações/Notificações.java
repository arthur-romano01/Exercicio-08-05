package Notificações;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import CriacaoSalas.Sala;

interface Observer {
    void update(Subject s);
}

class Subject {
    private List<Observer> observers = new ArrayList<>();

    public void registerObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

}

class CancelamentoReserva extends Subject {
    private Sala sala;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    public CancelamentoReserva(Sala sala, LocalDateTime dataInicio, LocalDateTime dataFim) {
        this.sala = sala;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public void cancelar() {
        notifyObservers();
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public String getDetalhes() {
        return "Reserva cancelada na sala: " + sala.getNome() + " de " + dataInicio + " até " + dataFim;
    }
}

class AlteracaoReserva extends Subject {
    private Sala sala;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    public AlteracaoReserva(Sala sala, LocalDateTime dataInicio, LocalDateTime dataFim) {
        this.sala = sala;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public void alterar() {
        notifyObservers();
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public String getDetalhes() {
        return "Reserva alterada na sala: " + sala.getNome() + " de " + dataInicio + " até " + dataFim;
    }
}

class CriacaoReserva extends Subject {
    private Sala sala;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    public CriacaoReserva(Sala sala, LocalDateTime dataInicio, LocalDateTime dataFim) {
        this.sala = sala;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public void criar() {
        notifyObservers();
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public String getDetalhes() {
        return "Reserva criada na sala: " + sala.getNome() + " de " + dataInicio + " até " + dataFim;
    }
}

class Notificador implements Observer {
    public void update(Subject s) {
        if (s instanceof CriacaoReserva) {
            CriacaoReserva cr = (CriacaoReserva) s;
            System.out.println("Notificação recebida: " + cr.getDetalhes());
        } else if (s instanceof AlteracaoReserva) {
            AlteracaoReserva ar = (AlteracaoReserva) s;
            System.out.println("Notificação recebida: " + ar.getDetalhes());
        } else if (s instanceof CancelamentoReserva) {
            CancelamentoReserva cr = (CancelamentoReserva) s;
            System.out.println("Notificação recebida: " + cr.getDetalhes());
        } else {
            System.out.println("Notificação recebida: Atualização desconhecida.");
        }
    }
}

public class Notificações {
    public static void main(String[] args) {
        // Criando um mock de Sala para o teste
        Sala sala = new Sala() {
            @Override
            public void exibirDetalhes() {
                System.out.println("Sala de Reunião A");
            }

            @Override
            public String getNome() {
                return "Sala de Reunião A";
            }
        };

        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusHours(2);

        // 1. Criando o Notificador (Observer)
        Notificador notificador = new Notificador();

        // 2. Simulando uma Criação de Reserva
        System.out.println("--- Disparando evento de Criação ---");
        CriacaoReserva criacao = new CriacaoReserva(sala, inicio, fim);
        criacao.registerObserver(notificador); // Inscreve o notificador
        criacao.criar(); // Aciona o evento e notifica

        // 3. Simulando uma Alteração de Reserva
        System.out.println("\n--- Disparando evento de Alteração ---");
        AlteracaoReserva alteracao = new AlteracaoReserva(sala, inicio.plusDays(1), fim.plusDays(1));
        alteracao.registerObserver(notificador); // Inscreve o notificador
        alteracao.alterar(); // Aciona o evento e notifica

        // 4. Simulando um Cancelamento de Reserva
        System.out.println("\n--- Disparando evento de Cancelamento ---");
        CancelamentoReserva cancelamento = new CancelamentoReserva(sala, inicio, fim);
        cancelamento.registerObserver(notificador); // Inscreve o notificador
        cancelamento.cancelar(); // Aciona o evento e notifica
    }
}
