package model;

public class Livro {

	private int id_livro;
	private String nome;
	private String autor;
	private int ano;
	
	public Livro() {
		this.nome = "";
		this.autor = "";
		this.ano = 0;
	}
	
	public Livro(String nome, String autor, int ano) {
		this.nome = nome;
		this.autor = autor;
		this.ano = ano;
	}

	public int getId_livro() {
		return id_livro;
	}

	public void setId_livro(int id_livro) {
		this.id_livro = id_livro;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	@Override
	public String toString() {
		return "Id do livro: " + id_livro + ", Titulo: " + nome + ", Autor: " + autor + ", Ano de lancamento: " + ano + " ";
	}
	
}
