package com.mindcare;

import com.mindcare.dao.ConsultaDAO;
import com.mindcare.dao.Database;
import com.mindcare.dao.DiarioDAO;
import com.mindcare.model.Consulta;
import com.mindcare.model.Diario;
import com.mindcare.service.MockIAService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainApp extends Application {

    private DiarioDAO diarioDAO;
    private ConsultaDAO     consultaDAO;
    private MockIAService iaService;

    private ListView<Diario> listaDiarios;
    private ListView<Consulta> listaConsultas;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Database.init(); // cria db e tabelas
        diarioDAO = new DiarioDAO();
        consultaDAO = new ConsultaDAO();
        iaService = new MockIAService();

        TabPane tabPane = new TabPane();

        Tab tabDiario = new Tab("Diário", diarioPane());
        Tab tabConsulta = new Tab("Consulta IA", consultaPane());
        Tab tabHistorico = new Tab("Histórico", historicoPane());

        tabDiario.setClosable(false);
        tabConsulta.setClosable(false);
        tabHistorico.setClosable(false);

        tabPane.getTabs().addAll(tabDiario, tabConsulta, tabHistorico);

        Scene scene = new Scene(tabPane, 900, 600);
        primaryStage.setTitle("Healthy Minds - MindCare (Protótipo)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 🔹 Aba Diário – apenas criar e salvar
    private Pane diarioPane() {
        BorderPane root = new BorderPane();

        VBox form = new VBox(8);
        form.setPadding(new Insets(10));

        Label lblTitle = new Label("Título:");
        TextField tfTitle = new TextField();
        Label lblConteudo = new Label("Conteúdo:");
        TextArea taConteudo = new TextArea();
        taConteudo.setPrefHeight(300);

        Button btnSalvar = new Button("Salvar Diário");
        btnSalvar.setOnAction(e -> {
            String titulo = tfTitle.getText().trim();
            String conteudo = taConteudo.getText().trim();

            if (titulo.isEmpty() || conteudo.isEmpty()) {
                showAlert("Erro", "Título e conteúdo são obrigatórios.");
                return;
            }

            Diario d = new Diario();
            d.setTitulo(titulo);
            d.setConteudo(conteudo);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            d.setDataCriacao(LocalDateTime.now().format(formatter));

            diarioDAO.salvar(d);
            tfTitle.clear();
            taConteudo.clear();
            showAlert("Sucesso", "Entrada do diário salva com sucesso!");
            refreshDiarios();
        });

        form.getChildren().addAll(lblTitle, tfTitle, lblConteudo, taConteudo, btnSalvar);
        root.setCenter(form);

        return root;
    }



    private Pane consultaPane() {
        BorderPane root = new BorderPane();
        VBox form = new VBox(8);
        form.setPadding(new Insets(10));

        Label lblEstado = new Label("Estado Mental:");
        ComboBox<String> cbEstado = new ComboBox<>();
        cbEstado.getItems().addAll("Ansioso", "Triste", "Neutro", "Estressado", "Calmo");
        cbEstado.getSelectionModel().selectFirst();

        Label lblDicas = new Label("Quero dicas sobre:");
        TextField tfDicas = new TextField();

        Label lblRelato = new Label("Diga mais sobre como você se sente:");
        TextArea taRelato = new TextArea();
        taRelato.setPrefHeight(200);

        Label lblTom = new Label("Tom da consulta:");
        ComboBox<String> cbTom = new ComboBox<>();
        cbTom.getItems().addAll("Acolhedor", "Motivacional", "Informativo");
        cbTom.getSelectionModel().selectFirst();

        TextArea taResposta = new TextArea();
        taResposta.setEditable(false);
        taResposta.setPrefHeight(200);

        Button btnEnviar = new Button("Enviar para IA");
        btnEnviar.setOnAction(e -> {
            String estado = cbEstado.getValue();
            String dicas = tfDicas.getText().trim();
            String relato = taRelato.getText().trim();
            String tom = cbTom.getValue();

            if (relato.isEmpty()) {
                showAlert("Erro", "Por favor, escreva pelo menos um pequeno relato.");
                return;
            }

            // chamada à IA (mock)
            String resposta = iaService.gerarResposta(estado, dicas, relato, tom);
            taResposta.setText(resposta);

            // salvar consulta
            Consulta c = new Consulta();
            c.setEstadoMental(estado);
            c.setDicasSobre(dicas);
            c.setRelato(relato);
            c.setTomConsulta(tom);
            c.setRespostaIA(resposta);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            c.setDataConsulta(LocalDateTime.now().format(formatter));

            consultaDAO.salvar(c);

            refreshConsultas();
        });

        form.getChildren().addAll(lblEstado, cbEstado, lblDicas, tfDicas, lblRelato, taRelato, lblTom, cbTom, btnEnviar, new Label("Resposta da IA:"), taResposta);
        root.setCenter(form);
        return root;
    }

    // 🔹 Aba Histórico – visualizar e excluir tanto Diários quanto Consultas
    private Pane historicoPane() {
        BorderPane root = new BorderPane();

        TabPane tabs = new TabPane();
        Tab tDiarios = new Tab("Diários");
        Tab tConsultas = new Tab("Consultas");

        // ======== ABA DIÁRIOS ========
        BorderPane diariosPane = new BorderPane();
        listaDiarios = new ListView<>();
        refreshDiarios();
        listaDiarios.setPrefWidth(400);

        // Ao clicar, mostra detalhes
        listaDiarios.setOnMouseClicked(e -> {
            Diario sel = listaDiarios.getSelectionModel().getSelectedItem();
            if (sel != null && e.getClickCount() == 2) {
                showAlert("Detalhes do Diário",
                        "Título: " + sel.getTitulo() +
                                "\nData: " + sel.getDataCriacao() +
                                "\n\n" + sel.getConteudo());
            }
        });

        // Botão excluir
        Button btnExcluirDiario = new Button("Excluir Selecionado");
        btnExcluirDiario.setOnAction(e -> {
            Diario sel = listaDiarios.getSelectionModel().getSelectedItem();
            if (sel != null) {
                diarioDAO.deletar(sel.getId());
                refreshDiarios();
                showAlert("Sucesso", "Diário excluído com sucesso!");
            } else {
                showAlert("Aviso", "Selecione um diário para excluir.");
            }
        });

        Button btnAtualizarDiario = new Button("Atualizar");
        btnAtualizarDiario.setOnAction(e -> refreshDiarios());

        VBox diarioBox = new VBox(8, listaDiarios, btnExcluirDiario, btnAtualizarDiario);
        diarioBox.setPadding(new Insets(10));
        diariosPane.setCenter(diarioBox);
        tDiarios.setContent(diariosPane);
        tDiarios.setClosable(false);

        // ======== ABA CONSULTAS ========
        BorderPane consultasPane = new BorderPane();
        listaConsultas = new ListView<>();
        refreshConsultas();
        listaConsultas.setPrefWidth(400);

        listaConsultas.setOnMouseClicked(e -> {
            Consulta sel = listaConsultas.getSelectionModel().getSelectedItem();
            if (sel != null && e.getClickCount() == 2) {
                showAlert("Detalhes da Consulta",
                        "Estado Mental: " + sel.getEstadoMental() +
                                "\nTom: " + sel.getTomConsulta() +
                                "\nData: " + sel.getDataConsulta() +
                                "\n\nRelato:\n" + sel.getRelato() +
                                "\n\nResposta IA:\n" + sel.getRespostaIA());
            }
        });

        // Botão excluir consulta
        Button btnExcluirConsulta = new Button("Excluir Selecionado");
        btnExcluirConsulta.setOnAction(e -> {
            Consulta sel = listaConsultas.getSelectionModel().getSelectedItem();
            if (sel != null) {
                consultaDAO.deletar(sel.getId());
                refreshConsultas();
                showAlert("Sucesso", "Consulta excluída com sucesso!");
            } else {
                showAlert("Aviso", "Selecione uma consulta para excluir.");
            }
        });

        Button btnAtualizarConsulta = new Button("Atualizar");
        btnAtualizarConsulta.setOnAction(e -> refreshConsultas());

        VBox consultaBox = new VBox(8, listaConsultas, btnExcluirConsulta, btnAtualizarConsulta);
        consultaBox.setPadding(new Insets(10));
        consultasPane.setCenter(consultaBox);
        tConsultas.setContent(consultasPane);
        tConsultas.setClosable(false);

        tabs.getTabs().addAll(tDiarios, tConsultas);
        root.setCenter(tabs);

        return root;
    }


    private void refreshDiarios() {
        List<Diario> all = diarioDAO.listar();
        if (listaDiarios != null) {
            listaDiarios.getItems().setAll(all);
        }
    }

    private void refreshConsultas() {
        List<Consulta> all = consultaDAO.listar();
        if (listaConsultas != null) {
            listaConsultas.getItems().setAll(all);
        }
    }

    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
