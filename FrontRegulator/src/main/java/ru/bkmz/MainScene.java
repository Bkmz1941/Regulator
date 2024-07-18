package ru.bkmz;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.JSONArray;
import ru.bkmz.services.repositories.RegulatorRepository;

import java.math.BigDecimal;
import java.util.List;

public class MainScene extends Application {
    RegulatorRepository regulatorRepository;
    private GridPane innerGridPane0;
    private Scene scene;
    ObservableList<BigDecimal> historyList;

    @Override
    public void start(Stage stage) throws Exception {
        try {
            MainScene mainScene = new MainScene();
            mainScene.show(stage);
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public MainScene() {
        regulatorRepository = new RegulatorRepository();
    }

    public void show(Stage stage) throws Exception {
        GridPane gridPane = createPane();
        addUI(gridPane);
        scene = new Scene(gridPane, 500, 800);
        stage.setScene(scene);
        stage.show();

        Integer temperature = regulatorRepository.getTemperature();
        TextField tf = (TextField) scene.lookup("#temperature_value");
        tf.setText(String.valueOf(temperature));

        updateHistoryView();
    }

    private GridPane createPane() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setPadding(new Insets(16, 16, 16, 16));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200, 200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);
        return gridPane;
    }

    private void addUI(GridPane gridPane) {
        // Current temperature
        innerGridPane0 = new GridPane();
        innerGridPane0.setHgap(10);
        innerGridPane0.setVgap(4);
        Button b0 = new Button("Обновить");
        innerGridPane0.add(generateGridInputIntValue("Текущая температура", "temperature_label", 0, "temperature_value", true), 0, 0, 2, 1);
        innerGridPane0.add(b0, 0, 1);
        gridPane.add(innerGridPane0, 0, 0, 2, 1);

        // New temperature
        GridPane innerGridPane1 = new GridPane();
        innerGridPane1.setHgap(10);
        innerGridPane1.setVgap(4);
        Button b1 = new Button("Установить");
        innerGridPane1.add(generateGridInputIntValue("Новая температура", "new_temperature_label", 0, "new_temperature_value", false), 0, 1);
        innerGridPane1.add(b1, 0, 2);
        gridPane.add(innerGridPane1, 0, 1, 2, 1);

        // History config
        GridPane innerGridPane2 = new GridPane();
        innerGridPane2.setHgap(10);
        innerGridPane2.setVgap(10);
        Label l2 = new Label("История изменений");
        l2.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Button b2 = new Button("Обновить");
        Button b2_2 = new Button("Очистить историю");
        innerGridPane2.add(l2, 0, 1);
        innerGridPane2.add(generateGridInputIntValue("Кол-во записей", "conf_history_limit_label", 5, "conf_history_limit_value", false), 0, 2);
        innerGridPane2.add(generateGridInputIntValue("Отступ", "conf_history_offset_label", 0, "conf_history_offset_value", false), 1, 2);
        innerGridPane2.add(b2, 0, 3);
        innerGridPane2.add(b2_2, 0, 4);
        gridPane.add(innerGridPane2, 0, 2, 2, 1);

        // History
        GridPane innerGridPane3 = new GridPane();
        innerGridPane3.setHgap(10);
        innerGridPane3.setVgap(4);
        Label l3 = new Label("История");
        historyList = FXCollections.observableArrayList();
        ListView<BigDecimal> langsListView = new ListView<>(historyList);
        langsListView.setId("#history_list");
        innerGridPane3.add(l3, 0, 0, 2, 1);
        innerGridPane3.add(langsListView, 0, 1, 2, 1);
        gridPane.add(innerGridPane3, 0, 3, 2, 1);


        b0.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer temperature = regulatorRepository.getTemperature();
                TextField tf = (TextField) scene.lookup("#temperature_value");
                tf.setText(String.valueOf(temperature));
            }
        });

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextField tf1 = (TextField) scene.lookup("#new_temperature_value");
                float temp = Float.parseFloat(tf1.getText());
                if (temp < -200 || temp > 1000) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("Указанное значение невалидно");
                    a.show();
                    return;
                }

                Integer temperature = regulatorRepository.setTemperature(temp);
                TextField tf2 = (TextField) scene.lookup("#temperature_value");
                tf2.setText(String.valueOf(temperature));

                updateHistoryView();
            }
        });

        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateHistoryView();
            }
        });

        b2_2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                regulatorRepository.clearHistory();
                updateHistoryView();
            }
        });
    }

    private void updateHistoryView() {
        TextField tf1 = (TextField) scene.lookup("#conf_history_limit_value");
        TextField tf2 = (TextField) scene.lookup("#conf_history_offset_value");
        JSONArray history = regulatorRepository.getTemperatureHistory(Integer.parseInt(tf1.getText()), Integer.parseInt(tf2.getText()));

        List<Object> listHistory = history.toList();

        historyList.clear();
        for (Object o : listHistory) {
            historyList.add((BigDecimal) o);
        }
    }


    private GridPane generateGridInputIntValue(String label, String labelId, int value, String valueId, boolean disabled) {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(4);
        Label l2 = new Label(label);
        l2.setId(labelId);
        TextField tf2 = new TextField(String.valueOf(value));
        tf2.setDisable(disabled);
        tf2.setId(valueId);
        grid.add(l2, 0, 1);
        grid.add(tf2, 0, 2);
        return grid;
    }
}
