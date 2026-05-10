package GerenciamentoUsuarios;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String tipo;
    
    public Usuario(String nome, String email, String senha, String tipo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public String getTipo() {
        return tipo;
    }
}
