package com.example.javafxdeber;

import com.example.javafxdeber.ingresar;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

public class Main extends Application {
    static final String DB_URL = "jdbc:mysql://localhost/person";
    static final String USER = "root";
    static final String PASS = "Kamekameha1";
    private TableView<ingresar> tableView;
    private ObservableList<ingresar> dataList;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JavaFX MySQL Example");

        // Crear tabla y columnas
        tableView = new TableView<>();
        TableColumn<ingresar, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(param -> param.getValue().idProperty());
        TableColumn<ingresar, String> nameCol = new TableColumn<>("name");
        nameCol.setCellValueFactory(param -> param.getValue().nameProperty());
        tableView.getColumns().addAll(idCol, nameCol);

        // Crear campos de texto y botones
        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField nameField = new TextField();
        nameField.setPromptText("name");
        Button agregar = new Button("Agregar");
        Button actualizar = new Button("Actualizar");
        Button borrar = new Button("Borrar");

        // Asignar eventos a los botones
        agregar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addData(idField.getText(), nameField.getText());
            }
        });

        actualizar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateData(idField.getText(), nameField.getText());
            }
        });

        borrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteData(idField.getText());
            }
        });

        // Crear grid pane para organizar los componentes
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.add(tableView, 0, 0, 2, 1);
        gridPane.add(idField, 0, 1);
        gridPane.add(nameField, 1, 1);
        gridPane.add(agregar, 0, 2);
        gridPane.add(actualizar, 1, 2);
        gridPane.add(borrar, 0, 3);

        // Obtener los datos de la base de datos y actualizar la tabla
        dataList = getDataFromDB();
        tableView.setItems(dataList);

        // Crear y mostrar la escena
        Scene scene = new Scene(gridPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para obtener los datos de la base de datos
    private ObservableList<ingresar> getDataFromDB() {
        ObservableList<ingresar> dataList = FXCollections.observableArrayList();

        try {
            // Conectarse a la base de datos
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // Realizar la consulta SQL
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ingresar");

            // Procesar los resultados
            while (rs.next()) {
                String id = rs.getString("ID");
                String name = rs.getString("name");

                dataList.add(new ingresar(id, name));
            }

            // Cerrar la conexión
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    // Método para agregar un nuevo registro a la base de datos
    private void addData(String id, String name) {
        try {
            // Conectarse a la base de datos
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // Realizar la consulta SQL
            String sql = "INSERT INTO ingresar VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.executeUpdate();

            // Cerrar la conexión
            conn.close();

            // Actualizar la tabla
            dataList.add(new ingresar(id, name));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar un registro en la base de datos
    private void updateData(String id, String name) {
        try {
            // Conectarse a la base de datos
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ person", "root", "Kamekameha1");

            // Realizar la consulta SQL
            String sql = "UPDATE ingresar SET name = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, id);
            stmt.executeUpdate();

            // Cerrar la conexión
            conn.close();

            // Actualizar la tabla
            for (ingresar ingresar : dataList) {
                if (ingresar.getId().equals(id)) {
                    ingresar.setName(name);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para borrar un registro de la base de datos
    private void deleteData(String id) {
        try {
            // Conectarse a la base de datos
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // Realizar la consulta SQL
            String sql = "DELETE FROM ingresar WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.executeUpdate();

            // Cerrar la conexión
            conn.close();

            // Actualizar la tabla
            for (ingresar ingresar : dataList) {
                if (ingresar.getId().equals(id)) {
                    dataList.remove(ingresar);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}