package GeraçãoRelatorios;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class Relatorio {
    private static Relatorio instance;
    private int contador;

    private Relatorio() {}

    public static synchronized Relatorio getInstance() {
        if (instance == null) {
            instance = new Relatorio();
        }
        return instance;
    }

    public void gerarRelatorio(String titulo, String conteudo){
        String nomeArquivo = "relatorio_" + LocalDate.now() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            writer.write("Título: " + titulo);
            writer.newLine();
            writer.write("Data: " + LocalDate.now());
            writer.newLine();
            writer.write("Conteúdo: " + conteudo);
            writer.newLine();
            this.contador++;
            System.out.println("Relatório gerado número: " + this.contador);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}