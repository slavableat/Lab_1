package by.lab1.controller;


import by.lab1.creator.SerialPortCreator;
import by.lab1.event.SendEvent;
import by.lab1.model.CustomPort;
import by.lab1.model.Parities;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import jssc.SerialPortException;

import java.util.Arrays;

public class Starter {
    private CustomPort port;
    private CustomPort port1;

    @FXML
    private TextArea input;

//    @FXML
//    private Button clearInputButton;

    @FXML
    private TextArea output;

//    @FXML
//    private Button clearOutputButton;

    @FXML
    private TextArea logger;

    @FXML
    private ComboBox<String> parity;


    @FXML
    void initialize() {
        try {
            for (Parities var : Parities.values())
                parity.getItems().add(String.valueOf(var));
            parity.setValue("PARITY_NONE");
            try {
                port = new CustomPort(SerialPortCreator.createSerialPort("COM1"), output);
                port1 = new CustomPort(SerialPortCreator.createSerialPort("COM4"), output);
            } catch (SerialPortException spe) {
                port = new CustomPort(SerialPortCreator.createSerialPort("COM2"), output);
                port1 = new CustomPort(SerialPortCreator.createSerialPort("COM3"), output);
            }
            SerialPortCreator.configurePorts(
                    Arrays.asList(port, port1),
                    Parities.valueOf(parity.getValue()));


            input.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    new SendEvent(input, logger, port).mouseClickedEvent();
                }
            });

//            clearInputButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> new ClearButtonEvent(input)
//                    .mouseClickedEvent());
//            clearOutputButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> new ClearButtonEvent(output)
//                    .mouseClickedEvent());
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
//        clearOutputButton.setDisable(true);
//        clearInputButton.setDisable(true);
    }

//    private int choosePair() {
//        return Math.random() >= 0.5 ? 1 : 0;
//    }
}

