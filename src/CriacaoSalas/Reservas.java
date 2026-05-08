import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


class Reserva {
    Sala sala;
    LocalDateTime dataInicio;
    LocalDateTime dataFim;
    public Reserva(Sala sala, LocalDateTime dataInicio, LocalDateTime dataFim){
        this.sala = sala;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }
}

class GerenciadorDeReservas {
    private List<Reserva> reservas = new ArrayList<>();
    private List<Sala> salas = new ArrayList<>();

    public void adicionarSala(Sala sala) {
        this.salas.add(sala);
    }

    public void criarReserva(Sala sala, LocalDateTime inicio, LocalDateTime fim) {
        this.reservas.add(new Reserva(sala, inicio, fim));
        System.out.println("Reserva efetuada na sala: " + sala.getNome());
    }

    public void verificarDisponibilidade(Sala sala, LocalDateTime inicio, LocalDateTime fim) {
        for (Reserva reserva : reservas) {
            if (reserva.sala.equals(sala) && reserva.dataInicio.isBefore(fim) && reserva.dataFim.isAfter(inicio)) {
                System.out.println("Sala indisponível");
                return;
            }
        }
        System.out.println("Sala disponível");
    }

    public List<Sala> listarSalasDisponiveis(LocalDateTime inicio, LocalDateTime fim) {
        List<Sala> disponiveis = new ArrayList<>();
        for (Sala sala : salas) {
            boolean disponivel = true;
            for (Reserva reserva : reservas) {
                if (reserva.sala.equals(sala) && reserva.dataInicio.isBefore(fim) && reserva.dataFim.isAfter(inicio)) {
                    disponivel = false;
                    break;
                }
            }
            if (disponivel) {
                disponiveis.add(sala);
            }
        }
        return disponiveis;
    }
}




public class Reservas {
    public static void main(String[] args){
        GerenciadorDeReservas gerenciador = new GerenciadorDeReservas();
        
        // Criando salas hipotéticas
        Sala sala1 = new SalaEstudoIndividual();
        Sala sala2 = new SalaTrabalhoEmGrupo();
        Sala sala3 = new SalaLaboratorio();
        
        // Adicionando as salas no gerenciador
        gerenciador.adicionarSala(sala1);
        gerenciador.adicionarSala(sala2);
        gerenciador.adicionarSala(sala3);
        
        // Definindo um intervalo de tempo para testes
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusHours(2);
        
        System.out.println("--- Efetuando Reserva ---");
        gerenciador.criarReserva(sala1, inicio, fim);
        
        System.out.println("\n--- Verificando Disponibilidade ---");
        System.out.print("Verificando " + sala1.getNome() + ": ");
        gerenciador.verificarDisponibilidade(sala1, inicio, fim);
        
        System.out.println("\n--- Salas Disponíveis no Horário ---");
        List<Sala> disponiveis = gerenciador.listarSalasDisponiveis(inicio, fim);
        for (Sala s : disponiveis) {
            System.out.println("- " + s.getNome());
        }
    }
}
