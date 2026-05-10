package CriacaoSalas;

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
        if(verificarDisponibilidade(sala, inicio, fim) == 1) {
            reservas.add(new Reserva(sala, inicio, fim));
            System.out.println("Reserva efetuada na sala: " + sala.getNome());
        } else {
            System.out.println("Reserva não efetuada, pois a sala não está disponível no horário solicitado");
        }
    }

    public void cancelarReserva(Sala sala, LocalDateTime inicio, LocalDateTime fim) {
        boolean removido = reservas.removeIf(r -> r.sala.equals(sala) && r.dataInicio.equals(inicio) && r.dataFim.equals(fim));
        
        if (removido) {
            System.out.println("Reserva cancelada na sala: " + sala.getNome());
        } else {
            System.out.println("Reserva não encontrada");
        }
    }

    public void alterarReserva(Sala salaAtual, LocalDateTime inicioAtual, LocalDateTime fimAtual, Sala salaNova, LocalDateTime inicioNovo, LocalDateTime fimNovo) {
        Reserva reservaEncontrada = null;
        for (Reserva reserva : reservas) {
            if (reserva.sala.equals(salaAtual) && reserva.dataInicio.equals(inicioAtual) && reserva.dataFim.equals(fimAtual)) {
                reservaEncontrada = reserva;
                break;
            }
        }
        
        if (reservaEncontrada != null) {
            // Removemos temporariamente para que a verificação não colida com a própria reserva
            reservas.remove(reservaEncontrada);
            
            if (verificarDisponibilidade(salaNova, inicioNovo, fimNovo) == 1) {
                reservaEncontrada.sala = salaNova;
                reservaEncontrada.dataInicio = inicioNovo;
                reservaEncontrada.dataFim = fimNovo;
                reservas.add(reservaEncontrada);
                System.out.println("Reserva alterada para a sala: " + salaNova.getNome());
            } else {
                // Se a nova sala não estiver disponível, voltamos a reserva original para a lista
                reservas.add(reservaEncontrada);
                System.out.println("Reserva não alterada, pois a nova sala não está disponível no novo horário");
            }
        } else {
            System.out.println("Reserva original não encontrada para alteração");
        }
    }

    public int verificarDisponibilidade(Sala sala, LocalDateTime inicio, LocalDateTime fim) {
        for (Reserva reserva : reservas) {
            if (reserva.sala.equals(sala) && reserva.dataInicio.isBefore(fim) && reserva.dataFim.isAfter(inicio)) {
                System.out.println("Sala indisponível");
                return 0;
            }
        }
        System.out.println("Sala disponível");
        return 1;
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
        Sala sala1 = new SalaEstudoIndividual("Sala de Estudo 1");
        Sala sala2 = new SalaTrabalhoEmGrupo("Sala de Grupo 1");
        Sala sala3 = new SalaLaboratorio("Laboratório 1");
        
        // Adicionando as salas no gerenciador
        gerenciador.adicionarSala(sala1);
        gerenciador.adicionarSala(sala2);
        gerenciador.adicionarSala(sala3);
        
        // Definindo um intervalo de tempo para testes
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusHours(2);
        
        System.out.println("--- 1. Efetuando Reserva na Sala 1 ---");
        gerenciador.criarReserva(sala1, inicio, fim);
        
        System.out.println("\n--- 2. Tentando Reservar a Sala 1 no mesmo horário ---");
        gerenciador.criarReserva(sala1, inicio, fim);
        
        System.out.println("\n--- 3. Alterando Reserva da Sala 1 para a Sala 2 ---");
        gerenciador.alterarReserva(sala1, inicio, fim, sala2, inicio, fim);

        System.out.println("\n--- 4. Tentando alterar com conflito ---");
        // Criamos uma reserva na sala 1 novamente
        gerenciador.criarReserva(sala1, inicio, fim); 
        // Tentamos alterar a da sala 1 para a sala 2, que já foi ocupada no passo 3!
        gerenciador.alterarReserva(sala1, inicio, fim, sala2, inicio, fim);
        
        System.out.println("\n--- 5. Cancelando Reserva da Sala 2 ---");
        gerenciador.cancelarReserva(sala2, inicio, fim);
        
        System.out.println("\n--- 6. Salas Disponíveis no Horário ---");
        List<Sala> disponiveis = gerenciador.listarSalasDisponiveis(inicio, fim);
        for (Sala s : disponiveis) {
            System.out.println("- " + s.getNome());
        }
    }
}
