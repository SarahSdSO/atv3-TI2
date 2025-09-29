package dao;

import model.Livro;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO extends DAO {
    
    public LivroDAO() {
        super();
        conectar();
    }
    
    @Override
    public void finalize() {
        close();
    }
    
    // inserir
    public boolean insert(Livro livro) {
    	
        boolean status = false;
        try {
            String sql = "INSERT INTO livro (nome, autor, ano) VALUES (?, ?, ?);";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, livro.getNome());
            st.setString(2, livro.getAutor());
            st.setInt(3, livro.getAno());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return status;
    }

    // get um livro especifico
    public Livro get(int id) {
        
    	Livro livro = null;
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                  ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM livro WHERE id_livro = " + id);
            if (rs.next()) {
                livro = new Livro(
                    rs.getString("nome"),
                    rs.getString("autor"),
                    rs.getInt("ano")
                );
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        return livro;
    }

    // get todos os livros
    public List<Livro> get() {
        return get("");
    }

    //ordena de um jeito especifico
    public List<Livro> getOrderById() {
        return get("id_livro");
    }

    public List<Livro> getOrderByNome() {
        return get("nome");
    }

    public List<Livro> getOrderByAutor() {
        return get("autor");
    }

    public List<Livro> getOrderByAno() {
        return get("ano");
    }

    private List<Livro> get(String orderBy) {
        
    	List<Livro> livros = new ArrayList<>();
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                  ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM livro" + (orderBy.trim().isEmpty() ? "" : " ORDER BY " + orderBy);
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Livro l = new Livro(
                    rs.getString("nome"),
                    rs.getString("autor"),
                    rs.getInt("ano")
                );
                livros.add(l);
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        return livros;
    }

    // atualizar
    public boolean update(Livro livro) {
        
    	boolean status = false;
        try {
            String sql = "UPDATE livro SET nome = ?, autor = ?, ano = ? WHERE id_livro = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, livro.getNome());
            st.setString(2, livro.getAutor());
            st.setInt(3, livro.getAno());
            st.setInt(4, livro.getId_livro());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return status;
    }

    // deletar
    public boolean delete(int id) {
        
    	boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM livro WHERE id_livro = " + id);
            st.close();
            status = true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return status;
    }
}
