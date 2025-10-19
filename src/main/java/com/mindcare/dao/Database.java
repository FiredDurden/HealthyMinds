package com.mindcare.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:mindcare.db";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL);
    }

    public static void init() {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            // criar tabela diario
            st.execute("CREATE TABLE IF NOT EXISTS diario (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "titulo TEXT NOT NULL," +
                    "conteudo TEXT NOT NULL," +
                    "dataCriacao TEXT NOT NULL" +
                    ");");

            // criar tabela consulta
            st.execute("CREATE TABLE IF NOT EXISTS consulta (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "estadoMental TEXT," +
                    "dicasSobre TEXT," +
                    "relato TEXT," +
                    "tomConsulta TEXT," +
                    "respostaIA TEXT," +
                    "dataConsulta TEXT" +
                    ");");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
