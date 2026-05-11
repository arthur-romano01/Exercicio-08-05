# Sistema de Reserva de Salas
## Objetivo
Esse projeto consiste em um sistema de reserva de salas que deve permitir que estudantes e professores consultem a disponibilidade de salas, façam reservas e recebam notificações sobre alterações ou conflitos.

## Requisitos
- [x] RF-01 Listar salas disponíveis em um intervalo de datas.
- [x] RF-02 Permitir que um usuário crie, modifique ou cancele uma reserva.
- [x] RF-03 Detectar e impedir colisões de horário.
- [x] RF-04 Enviar notificação imediata a todos os envolvidos quando uma reserva for alterada ou cancelada.
- [x] RF-05 Disponibilizar relatório diário com as reservas confirmadas de cada sala.

## Padrões de Projeto Aplicados
- **Factory Method**: Criação de diferentes tipos de salas (`SalaEstudoIndividual`, `SalaTrabalhoEmGrupo`, `SalaLaboratorio`) através da `FabricaDeSalas`, sem acoplamento às classes concretas.
- **Observer**: Sistema de notificações com modelos **Push** e **Pull** para alertar usuários sobre criações, alterações ou cancelamentos em suas reservas.
- **Strategy**: Duas políticas de reserva intercambiáveis em tempo de execução: `PoliticaPrimeiroChegado` (quem chegou primeiro mantém a sala) e `PoliticaPrioridade` (professores têm prioridade sobre alunos).
- **Singleton**: Gerenciamento centralizado do sistema (`GerenciadorDeReservas`) e do módulo de relatórios (`Relatorio`), garantindo instâncias únicas para controle e gravação em arquivo.

## Instruções de Uso

### Pré-requisitos
- **Java Development Kit (JDK) 17** ou superior.
- Um terminal (Prompt de Comando, PowerShell, ou terminal do Linux/Mac).

### Como compilar e executar (Via Terminal)
Esta é a forma recomendada e oficial para a avaliação do projeto.

1. Clone o repositório ou baixe o código fonte.
2. Navegue até o diretório raiz do projeto pelo terminal:
   ```bash
   cd caminho/para/o/projeto
   ```
3. Compile os arquivos Java:
   ```bash
   javac -d bin src/CriacaoSalas/*.java src/GerenciamentoUsuarios/*.java src/Notificações/*.java src/GeraçãoRelatorios/*.java
   ```
4. Execute a aplicação:
   ```bash
   java -cp bin CriacaoSalas.Reservas
   ```

## Equipe
<table border="1">
  <tr>
    <th>Nome</th>
    <th>RA</th>
  </tr>
  <tr>
    <td>Arthur Romano da Luz</td>
    <td>168498</td>
  </tr>
  <tr>
    <td>Ana Clara Lopes Louzada</td>
    <td>178036</td>
  </tr>
</table>