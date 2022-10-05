package by.bsuir.lab1.controller;


import by.bsuir.lab1.creator.SerialPortCreator;
import by.bsuir.lab1.event.ClearButtonEvent;
import by.bsuir.lab1.event.SendButtonEvent;
import by.bsuir.lab1.model.MySerialPort;
import by.bsuir.lab1.model.Parities;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import jssc.SerialPortException;


import java.util.Arrays;

public class Controller {
    private MySerialPort port;
    private MySerialPort port1;

    @FXML
    private TextArea input;

    @FXML
    private GridPane gridPane;


    @FXML
    private Button clearInputButton;

    @FXML
    private TextArea output;

    @FXML
    private Button clearOutputButton;

    @FXML
    private TextArea logger;

    @FXML
    private ComboBox<String> parity;


    @FXML
    void initialize() {
        try {
            parity.setValue("PARITY_NONE");
            for (Parities var : Parities.values())
                parity.getItems().add(String.valueOf(var));
            try {
                port = new MySerialPort(SerialPortCreator.createSerialPort("COM1"), output);
                port1 = new MySerialPort(SerialPortCreator.createSerialPort("COM4"), output);
            } catch (SerialPortException spe) {
                port = new MySerialPort(SerialPortCreator.createSerialPort("COM2"), output);
                port1 = new MySerialPort(SerialPortCreator.createSerialPort("COM3"), output);
            }
            SerialPortCreator.configurePorts(
                    Arrays.asList(port, port1),
                    Parities.valueOf(parity.getValue()));


            input.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    new SendButtonEvent(parity, input, logger, port).mouseClickedEvent();
                }
            });

            clearInputButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> new ClearButtonEvent(input)
                    .mouseClickedEvent());
            clearOutputButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> new ClearButtonEvent(output)
                    .mouseClickedEvent());
            parity.setOnAction(actionEvent -> SerialPortCreator.setPortParams(Arrays.asList(port, port1), Parities.valueOf(parity.getValue())));
            if (port.getPortName().compareTo("COM1") == 0) {
                logger.appendText("COM1 initialized for writing ...\n");
                logger.appendText("COM2 initialized for reading...\n");
            } else {
                logger.appendText("COM3 initialized for reading ...\n");
                logger.appendText("COM4 initialized for writing...\n");
            }
        } catch (SerialPortException e) {
            logger.appendText("Port initialization error!\n");
            setDisable();
        }
    }

    public void setDisable() {
        parity.setDisable(true);
        output.setDisable(true);
        input.setDisable(true);
        clearOutputButton.setDisable(true);
        clearInputButton.setDisable(true);
    }

//    private int choosePair() {
//        return Math.random() >= 0.5 ? 1 : 0;
//    }
}

