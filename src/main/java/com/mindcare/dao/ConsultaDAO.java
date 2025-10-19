package com.mindcare.dao;

import com.mindcare.model.Consulta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    public void salvar(Consulta c) {
        String sql = "INSERT INTO consulta(estadoMental, dicasSobre, relato, tomConsulta, respostaIA, dataConsulta) VALUES(?,?,?,?,?,?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getEstadoMental());
            ps.setString(2, c.getDicasSobre());
            ps.setString(3, c.getRelato());
            ps.setString(4, c.getTomConsulta());
            ps.setString(5, c.getRespostaIA());
            ps.setString(6, c.getDataConsulta());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Consulta> listar() {
        String sql = "SELECT id, estadoMental, dicasSobre, relato, tomConsulta, respostaIA, dataConsulta FROM consulta ORDER BY id DESC";
        List<Consulta> lista = new ArrayList<>();
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Consulta c1 = new Consulta();
                c1.setId(rs.getInt("id"));
                c1.setEstadoMental(rs.getString("estadoMental"));
                c1.setDicasSobre(rs.getString("dicasSobre"));
                c1.setRelato(rs.getString("relato"));
                c1.setTomConsulta(rs.getString("tomConsulta"));
                c1.setRespostaIA(rs.getString("respostaIA"));
                c1.setDataConsulta(rs.getString("dataConsulta"));
                lista.add(c1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }


    public void deletar(int id) {
        String sql = "DELETE FROM consulta WHERE id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
