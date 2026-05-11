# Arquitetura e Padrões de Projeto

Este documento detalha as decisões arquiteturais e os padrões de projeto (Design Patterns) implementados no Sistema de Reserva de Salas de Estudo, visando atender aos requisitos funcionais (RF-01 a RF-05).

## 1. Singleton (Gerenciamento Centralizado e Logs)
**Onde:** `GerenciadorDeReservas` (Pacote `CriacaoSalas`) e `Relatorio` (Pacote `GeraçãoRelatorios`).
**Objetivo:** Garantir que exista apenas uma instância central do gerenciador de reservas em memória e uma única instância de acesso de gravação de logs (evitando concorrência em I/O).
**Detalhes:**
- O construtor é privado em ambas as classes.
- A instância é mantida na variável estática `instance`.
- A criação é garantida de forma "Thread-Safe" através do modificador `synchronized` no método `getInstance()`.

## 2. Factory Method (Criação de Salas)
**Onde:** Pacote `CriacaoSalas` (Interface `Sala` e classes concretas `SalaEstudoIndividual`, `SalaTrabalhoEmGrupo`, `SalaLaboratorio`).
**Objetivo:** Isolar a lógica de criação de diferentes tipos de salas, permitindo que o sistema seja escalável para novos tipos no futuro.
**Detalhes:** 
- Uma classe `FabricaDeSalas` abstrai a inicialização, retornando instâncias que respeitam o contrato da interface `Sala`.

## 3. Strategy (Políticas de Reserva e Conflito)
**Onde:** Pacote `GerenciamentoUsuarios` (Interface `PoliticaReserva`, classes concretas `PoliticaPrioridade` e `PoliticaPrimeiroChegado`).
**Objetivo:** Definir e trocar a política de desempate de colisões dinamicamente em tempo de execução.
**Detalhes:**
- **`PoliticaPrimeiroChegado`:** Política FCFS (First-Come, First-Served). Quem reservou primeiro mantém a sala, independentemente do tipo de usuário.
- **`PoliticaPrioridade`:** Professores têm prioridade sobre alunos e podem sobrepor reservas em conflito.
- A troca é feita em tempo de execução através do método `setPoliticaReserva()` no `GerenciadorDeReservas` (o Contexto do padrão Strategy), sem necessidade de recompilar ou alterar a lógica de conflito.

## 4. Observer (Sistema de Notificações - Push e Pull)
**Onde:** Pacote `Notificações` (Interface `Observer`, classes `Subject`, `CriacaoReserva`, `AlteracaoReserva`, `CancelamentoReserva`, `Notificador`).
**Objetivo:** Avisar todos os envolvidos sempre que o status de uma reserva muda.
**Detalhes:**
- **PULL:** O Subject envia a própria referência e o Observer "puxa" os dados que precisa.
- **PUSH:** O Subject já empurra as mensagens consolidadas diretamente no evento `update`.
- O `GerenciadorDeReservas` atua como o client que dispara esses eventos sempre que a lista de reservas sofre mutações.
