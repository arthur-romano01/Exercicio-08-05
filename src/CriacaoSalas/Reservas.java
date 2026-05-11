package CriacaoSalas;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import GerenciamentoUsuarios.Usuario;
import Notificações.*;
import GeraçãoRelatorios.Relatorio;

interface ReservaBase {
   void exibirDetalhes();
}

class Reserva implements ReservaBase {
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

    @Override
    public void exibirDetalhes() {
        System.out.println("Reserva | Sala: " + sala.getNome()
            + " | Usuário: " + dono.getNome()
            + " | Das " + dataInicio.toLocalTime()
            + " às " + dataFim.toLocalTime());
    }
}

class GerenciadorDeReservas {
    private static GerenciadorDeReservas instance;
    private List<Reserva> reservas = new ArrayList<>();
    private List<Sala> salas = new ArrayList<>();
    private GerenciamentoUsuarios.PoliticaReserva politicaReserva; 
    private List<Observer> observers = new ArrayList<>();

    private GerenciadorDeReservas() {};
    public static synchronized GerenciadorDeReservas getInstance() {
        if (instance == null)
            instance = new GerenciadorDeReservas();
        return instance;
    }

    public void registrarObserver(Observer o) {
        this.observers.add(o);
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
            
            // Disparando Observer (Criação)
            CriacaoReserva notificacao = new CriacaoReserva(sala, inicio, fim, dono);
            for(Observer o : observers) notificacao.registerObserver(o);
            notificacao.criar();

        } else {
            System.out.println("Conflito detectado na sala " + sala.getNome() + "!");
            // Aplica a política de reserva injetada dinamicamente
            if (this.politicaReserva != null && this.politicaReserva.verificarPermissao(dono, conflito.dono)) {
                
                // Antes de remover a reserva antiga, notifica que ela foi cancelada por conflito de prioridade
                CancelamentoReserva notifCancelamento = new CancelamentoReserva(conflito.sala, conflito.dataInicio, conflito.dataFim, conflito.dono);
                for(Observer o : observers) notifCancelamento.registerObserver(o);
                notifCancelamento.cancelar();

                reservas.remove(conflito);
                reservas.add(new Reserva(sala, inicio, fim, dono));
                System.out.println("-> Reserva sobreposta por prioridade! O usuário " + dono.getNome() + " tomou a sala de " + conflito.dono.getNome());
                
                // Dispara a criação da nova reserva
                CriacaoReserva notificacao = new CriacaoReserva(sala, inicio, fim, dono);
                for(Observer o : observers) notificacao.registerObserver(o);
                notificacao.criar();

            } else {
                System.out.println("-> Reserva negada, a sala já está ocupada por " + conflito.dono.getNome() + " e você não possui prioridade.");
            }
        }
    }

    public void cancelarReserva(Sala sala, LocalDateTime inicio, LocalDateTime fim) {
        Reserva reservaParaRemover = null;
        for (Reserva r : reservas) {
            if (r.sala.equals(sala) && r.dataInicio.equals(inicio) && r.dataFim.equals(fim)) {
                reservaParaRemover = r;
                break;
            }
        }
        
        if (reservaParaRemover != null) {
            reservas.remove(reservaParaRemover);
            System.out.println("Reserva cancelada na sala: " + sala.getNome());
            
            // Disparando Observer (Cancelamento)
            CancelamentoReserva notificacao = new CancelamentoReserva(sala, inicio, fim, reservaParaRemover.dono);
            for(Observer o : observers) notificacao.registerObserver(o);
            notificacao.cancelar();
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
                
                // Disparando Observer (Alteracao)
                AlteracaoReserva notificacao = new AlteracaoReserva(salaNova, inicioNovo, fimNovo, dono);
                for(Observer o : observers) notificacao.registerObserver(o);
                notificacao.alterar();

            } else {
                reservas.add(reservaEncontrada);
                System.out.println("Reserva não alterada, pois a nova sala não está disponível no novo horário");
            }
        } else {
            System.out.println("Reserva original não encontrada para alteração");
        }
    }

    public void gerarRelatorioDiario() {
        StringBuilder conteudo = new StringBuilder();
        conteudo.append("Reservas confirmadas hoje:\n\n");
        
        if (reservas.isEmpty()) {
            conteudo.append("Nenhuma reserva registrada.\n");
        } else {
            for (Reserva r : reservas) {
                conteudo.append("- Sala: ").append(r.sala.getNome())
                        .append(" | Das ").append(r.dataInicio.toLocalTime())
                        .append(" às ").append(r.dataFim.toLocalTime())
                        .append(" | Usuário: ").append(r.dono.getNome())
                        .append("\n");
            }
        }
        
        // Chamando o Singleton de Relatorio para gravar os dados
        Relatorio.getInstance().gerarRelatorio("Relatório Diário de Ocupação", conteudo.toString());
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

abstract class ReservaDecorator implements ReservaBase {
    protected ReservaBase reserva;

    public ReservaDecorator(ReservaBase reserva) {
        this.reserva = reserva;
    }

    @Override
    public void exibirDetalhes() {
        reserva.exibirDetalhes();
    }
}


class ReservaComMultimidia extends ReservaDecorator {
    public ReservaComMultimidia(ReservaBase reserva) {
        super(reserva);
    }

    @Override
    public void exibirDetalhes() {
        super.exibirDetalhes();                              
        System.out.println("  [+] Equipamento Multimídia incluído");
    }
}


class ReservaComLimpeza extends ReservaDecorator {
    public ReservaComLimpeza(ReservaBase reserva) {
        super(reserva);
    }

    @Override
    public void exibirDetalhes() {
        super.exibirDetalhes();                              
        System.out.println("  [+] Serviço de Limpeza incluído");
    }
}


public class Reservas {
    public static void main(String[] args){
        GerenciadorDeReservas gerenciador = GerenciadorDeReservas.getInstance();

        // Registrando o Notificador (Observer)
        gerenciador.registrarObserver(new Notificador());

        // Criando salas via FabricaDeSalas (Factory Method) — RF-01
        Sala sala1 = FabricaDeSalas.createSala("estudo individual", "Sala de Estudo 101");
        Sala sala2 = FabricaDeSalas.createSala("trabalho em grupo", "Sala de Grupo 201");
        Sala sala3 = FabricaDeSalas.createSala("laboratorio", "Laboratório 301");

        gerenciador.adicionarSala(sala1);
        gerenciador.adicionarSala(sala2);
        gerenciador.adicionarSala(sala3);

        // Criando usuários
        Usuario aluno1    = new Usuario("João (Aluno)",          "joao@email.com",  "123", "aluno");
        Usuario aluno2    = new Usuario("Maria (Aluno)",          "maria@email.com", "123", "aluno");
        Usuario professor = new Usuario("Dr. Silva (Professor)",  "silva@email.com", "123", "professor");

        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim    = inicio.plusHours(2);

        // ── RF-01: Listar salas disponíveis antes de qualquer reserva ──
        System.out.println("=== RF-01: Salas disponíveis no horário " + inicio.toLocalTime() + " - " + fim.toLocalTime() + " ===");
        for (Sala s : gerenciador.listarSalasDisponiveis(inicio, fim)) {
            System.out.println("  Disponível: " + s.getNome());
        }

        // ── ESTRATÉGIA 1: PoliticaPrimeiroChegado ──
        System.out.println("\n=== ESTRATÉGIA: Primeiro a Reservar tem Prioridade ===");
        gerenciador.setPoliticaReserva(new GerenciamentoUsuarios.PoliticaPrimeiroChegado());

        System.out.println("\n--- 1. Aluno 1 reservando Sala de Estudo 101 ---");
        gerenciador.criarReserva(sala1, inicio, fim, aluno1);

        System.out.println("\n--- 2. Aluno 2 tenta tomar a Sala 101 do Aluno 1 (deve ser negado) ---");
        gerenciador.criarReserva(sala1, inicio, fim, aluno2);

        System.out.println("\n--- 3. Professor tenta tomar a Sala 101 do Aluno 1 (deve ser negado) ---");
        gerenciador.criarReserva(sala1, inicio, fim, professor);

        // ── ESTRATÉGIA 2: PoliticaPrioridade ──
        System.out.println("\n=== ESTRATÉGIA: Prioridade por Papel (Professor > Aluno) ===");
        gerenciador.setPoliticaReserva(new GerenciamentoUsuarios.PoliticaPrioridade());

        System.out.println("\n--- 4. Aluno 2 reservando Sala de Grupo 201 ---");
        gerenciador.criarReserva(sala2, inicio, fim, aluno2);

        System.out.println("\n--- 5. Professor tenta tomar a Sala 201 do Aluno 2 (deve ter prioridade) ---");
        gerenciador.criarReserva(sala2, inicio, fim, professor);

        System.out.println("\n--- 6. Aluno 1 tenta tomar a Sala 201 do Professor (deve ser negado) ---");
        gerenciador.criarReserva(sala2, inicio, fim, aluno1);

        // ── RF-02: Alteração de reserva ──
        System.out.println("\n--- 7. Professor altera reserva da Sala 201 para o Laboratório 301 ---");
        gerenciador.alterarReserva(sala2, inicio, fim, sala3, inicio, fim, professor);

        // ── RF-02: Cancelamento ──
        System.out.println("\n--- 8. Aluno 1 cancela a reserva da Sala de Estudo 101 ---");
        gerenciador.cancelarReserva(sala1, inicio, fim);

        // ── RF-01: Listar salas disponíveis após operações ──
        System.out.println("\n=== RF-01: Salas disponíveis após todas as operações ===");
        for (Sala s : gerenciador.listarSalasDisponiveis(inicio, fim)) {
            System.out.println("  Disponível: " + s.getNome());
        }

        // ── RF-05: Relatório diário ──
        System.out.println("\n=== RF-05: Encerrando o dia — Gerando Relatório ===");
        gerenciador.gerarRelatorioDiario();

        // ── BÔNUS: Decorator — adicionando extras a uma reserva ──
        System.out.println("\n=== BÔNUS — Decorator: Reservas com Funcionalidades Extras ===");

        // Reserva simples (componente base)
        ReservaBase r1 = new Reserva(sala3, inicio, fim, professor);
        System.out.println("\n-- Reserva simples --");
        r1.exibirDetalhes();

        // Reserva com equipamento multimídia
        ReservaBase r2 = new ReservaComMultimidia(new Reserva(sala3, inicio, fim, professor));
        System.out.println("\n-- Reserva + Multimídia --");
        r2.exibirDetalhes();

        // Reserva com multimídia E limpeza (decorators empilhados)
        ReservaBase r3 = new ReservaComLimpeza(new ReservaComMultimidia(new Reserva(sala3, inicio, fim, professor)));
        System.out.println("\n-- Reserva + Multimídia + Limpeza (empilhados) --");
        r3.exibirDetalhes();
    }
}
