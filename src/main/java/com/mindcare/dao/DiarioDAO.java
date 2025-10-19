package com.mindcare.dao;

import com.mindcare.model.Diario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiarioDAO {

    public void salvar(Diario d) {
        String sql = "INSERT INTO diario(titulo, conteudo, dataCriacao) VALUES(?,?,?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, d.getTitulo());
            ps.setString(2, d.getConteudo());
            ps.setString(3, d.getDataCriacao());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Diario> listar() {
        String sql = "SELECT id, titulo, conteudo, dataCriacao FROM diario ORDER BY id DESC";
        List<Diario> lista = new ArrayList<>();
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Diario d = new Diario();
                d.setId(rs.getInt("id"));
                d.setTitulo(rs.getString("titulo"));
                d.setConteudo(rs.getString("conteudo"));
                d.setDataCriacao(rs.getString("dataCriacao"));
                lista.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void deletar(int id) {
        String sql = "DELETE FROM diario WHERE id = ?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
