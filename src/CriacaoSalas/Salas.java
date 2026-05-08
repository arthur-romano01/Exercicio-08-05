interface Sala {
    void exibirDetalhes();
    String getNome();
}

class SalaEstudoIndividual implements Sala{
    private String nome = "Sala de Estudo Individual";
    public void exibirDetalhes() {
        System.out.println("Sala de Estudo Individual instanciada.");
    }
    public String getNome() {
        return this.nome;
    }
}

class SalaTrabalhoEmGrupo implements Sala{
    private String nome = "Sala de Trabalho em Grupo";
    public void exibirDetalhes() {
        System.out.println("Sala de Trabalho em Grupo instanciada.");
    }
    public String getNome() {
        return this.nome;
    }
}

class SalaLaboratorio implements Sala{
    private String nome = "Sala de Laboratório";
    public void exibirDetalhes() {
        System.out.println("Sala de Laboratório instanciada.");
    }
    public String getNome() {
        return this.nome;
    }
}

class FabricaDeSalas{
    public static Sala createSala(String tipo){
        if(tipo.equalsIgnoreCase("estudo individual")){
            return new SalaEstudoIndividual();
        }else if(tipo.equalsIgnoreCase("trabalho em grupo")){
            return new SalaTrabalhoEmGrupo();
        }else if(tipo.equalsIgnoreCase("laboratorio")){
            return new SalaLaboratorio();
        }else{
            throw new IllegalArgumentException("Tipo de sala desconhecido: " + tipo);
        }
    }
}

public class Salas{
    public static void main(String[] args){

        Sala sala1 = FabricaDeSalas.createSala("estudo individual");
        Sala sala2 = FabricaDeSalas.createSala("trabalho em grupo");
        Sala sala3 = FabricaDeSalas.createSala("laboratorio");
        
        sala1.exibirDetalhes();
        sala2.exibirDetalhes();
        sala3.exibirDetalhes();
    }
}