package CriacaoSalas;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import GerenciamentoUsuarios.Usuario;


class Reserva {
    Sala sala;
    LocalDateTime dataInicio;
    LocalDateTime dataFim;
    Usuario dono;
    public Reserva(Sala sala, LocalDateTime dataInicio, LocalDateTime dataFim, Usuario dono){
        this.sala = sala;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.dono = dono;
    }
}

class GerenciadorDeReservas {
    private static GerenciadorDeReservas instance;
    private List<Reserva> reservas = new ArrayList<>();
    private List<Sala> salas = new ArrayList<>();
    private GerenciamentoUsuarios.PoliticaReserva politicaReserva; 

    private GerenciadorDeReservas() {};
    public static synchronized GerenciadorDeReservas getInstance() {
        if (instance == null)
            instance = new GerenciadorDeReservas();
        return instance;
    }

    public void setPoliticaReserva(GerenciamentoUsuarios.PoliticaReserva politica) {
        this.politicaReserva = politica;
    }

    public void adicionarSala(Sala sala) {
        this.salas.add(sala);
    }

    public Reserva buscarConflito(Sala sala, LocalDateTime inicio, LocalDateTime fim) {
        for (Reserva reserva : reservas) {
            if (reserva.sala.equals(sala) && reserva.dataInicio.isBefore(fim) && reserva.dataFim.isAfter(inicio)) {
                return reserva;
            }
        }
        return null;
    }

    public void criarReserva(Sala sala, LocalDateTime inicio, LocalDateTime fim, Usuario dono) {
        Reserva conflito = buscarConflito(sala, inicio, fim);
        if(conflito == null) {
            reservas.add(new Reserva(sala, inicio, fim, dono));
            System.out.println("Reserva efetuada na sala " + sala.getNome() + " pelo usuário: " + dono.getNome());
        } else {
            System.out.println("Conflito detectado na sala " + sala.getNome() + "!");
            // Aplica a política de reserva injetada dinamicamente
            if (this.politicaReserva != null && this.politicaReserva.verificarPermissao(dono, conflito.dono)) {
                reservas.remove(conflito);
                reservas.add(new Reserva(sala, inicio, fim, dono));
                System.out.println("-> Reserva sobreposta por prioridade! O usuário " + dono.getNome() + " tomou a sala de " + conflito.dono.getNome());
            } else {
                System.out.println("-> Reserva negada, a sala já está ocupada por " + conflito.dono.getNome() + " e você não possui prioridade.");
            }
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

    public void alterarReserva(Sala salaAtual, LocalDateTime inicioAtual, LocalDateTime fimAtual, Sala salaNova, LocalDateTime inicioNovo, LocalDateTime fimNovo, Usuario dono) {
        Reserva reservaEncontrada = null;
        for (Reserva reserva : reservas) {
            if (reserva.sala.equals(salaAtual) && reserva.dataInicio.equals(inicioAtual) && reserva.dataFim.equals(fimAtual) && reserva.dono.equals(dono)) {
                reservaEncontrada = reserva;
                break;
            }
        }
        
        if (reservaEncontrada != null) {
            reservas.remove(reservaEncontrada);
            
            Reserva conflito = buscarConflito(salaNova, inicioNovo, fimNovo);
            if (conflito == null) {
                reservaEncontrada.sala = salaNova;
                reservaEncontrada.dataInicio = inicioNovo;
                reservaEncontrada.dataFim = fimNovo;
                reservas.add(reservaEncontrada);
                System.out.println("Reserva de " + dono.getNome() + " alterada para a sala: " + salaNova.getNome());
            } else {
                reservas.add(reservaEncontrada);
                System.out.println("Reserva não alterada, pois a nova sala não está disponível no novo horário");
            }
        } else {
            System.out.println("Reserva original não encontrada para alteração");
        }
    }

    public List<Sala> listarSalasDisponiveis(LocalDateTime inicio, LocalDateTime fim) {
        List<Sala> disponiveis = new ArrayList<>();
        for (Sala sala : salas) {
            if (buscarConflito(sala, inicio, fim) == null) {
                disponiveis.add(sala);
            }
        }
        return disponiveis;
    }
}




public class Reservas {
    public static void main(String[] args){
        GerenciadorDeReservas gerenciador = GerenciadorDeReservas.getInstance();
        
        // Configurando a Política de Reserva (Strategy) no Gerenciador (Context)
        gerenciador.setPoliticaReserva(new GerenciamentoUsuarios.PoliticaPrioridade());
        
        // Criando salas hipotéticas
        Sala sala1 = new SalaEstudoIndividual("Sala de Estudo 1");
        Sala sala2 = new SalaTrabalhoEmGrupo("Sala de Grupo 1");
        
        gerenciador.adicionarSala(sala1);
        gerenciador.adicionarSala(sala2);
        
        // Criando usuários para o teste de política
        Usuario aluno1 = new Usuario("João (Aluno)", "joao@email.com", "123", "aluno");
        Usuario aluno2 = new Usuario("Maria (Aluno)", "maria@email.com", "123", "aluno");
        Usuario professor = new Usuario("Dr. Silva (Professor)", "silva@email.com", "123", "professor");

        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusHours(2);
        
        System.out.println("--- 1. Aluno 1 reservando Sala 1 ---");
        gerenciador.criarReserva(sala1, inicio, fim, aluno1);
        
        System.out.println("\n--- 2. Aluno 2 tentando roubar a Sala 1 do Aluno 1 ---");
        gerenciador.criarReserva(sala1, inicio, fim, aluno2);
        
        System.out.println("\n--- 3. Professor tentando roubar a Sala 1 do Aluno 1 ---");
        gerenciador.criarReserva(sala1, inicio, fim, professor);

        System.out.println("\n--- 4. Aluno 2 tentando reservar Sala 1 do Professor ---");
        gerenciador.criarReserva(sala1, inicio, fim, aluno2);
    }
}
