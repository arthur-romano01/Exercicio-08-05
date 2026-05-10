interface Sala {
    void exibirDetalhes();
    String getNome();
}

class SalaEstudoIndividual implements Sala{
    private String nome;
    public SalaEstudoIndividual(String nome) {
        this.nome = nome;
    }
    public void exibirDetalhes() {
        System.out.println("Sala de Estudo Individual instanciada: " + this.nome);
    }
    public String getNome() {
        return this.nome;
    }
}

class SalaTrabalhoEmGrupo implements Sala{
    private String nome;
    public SalaTrabalhoEmGrupo(String nome) {
        this.nome = nome;
    }
    public void exibirDetalhes() {
        System.out.println("Sala de Trabalho em Grupo instanciada: " + this.nome);
    }
    public String getNome() {
        return this.nome;
    }
}

class SalaLaboratorio implements Sala{
    private String nome;
    public SalaLaboratorio(String nome) {
        this.nome = nome;
    }
    public void exibirDetalhes() {
        System.out.println("Sala de Laboratório instanciada: " + this.nome);
    }
    public String getNome() {
        return this.nome;
    }
}

class FabricaDeSalas{
    public static Sala createSala(String tipo, String nome){
        if(tipo.equalsIgnoreCase("estudo individual")){
            return new SalaEstudoIndividual(nome);
        }else if(tipo.equalsIgnoreCase("trabalho em grupo")){
            return new SalaTrabalhoEmGrupo(nome);
        }else if(tipo.equalsIgnoreCase("laboratorio")){
            return new SalaLaboratorio(nome);
        }else{
            throw new IllegalArgumentException("Tipo de sala desconhecido: " + tipo);
        }
    }
}

public class Salas{
    public static void main(String[] args){

        Sala sala1 = FabricaDeSalas.createSala("estudo individual", "Sala 101");
        Sala sala2 = FabricaDeSalas.createSala("trabalho em grupo", "Sala 201");
        Sala sala3 = FabricaDeSalas.createSala("laboratorio", "Lab 1");
        
        sala1.exibirDetalhes();
        sala2.exibirDetalhes();
        sala3.exibirDetalhes();
    }
}