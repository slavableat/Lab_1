package by.lab1.controller;


import by.lab1.creator.SerialPortCreator;
import by.lab1.event.SendEvent;
import by.lab1.model.PortAndHisTextArea;
import by.lab1.model.Parities;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import jssc.SerialPortException;

import java.util.Arrays;

public class Starter {
    private PortAndHisTextArea writer;
    private PortAndHisTextArea receiver;

    @FXML
    private TextArea input;

    @FXML
    private TextArea output;

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
                writer = new PortAndHisTextArea(SerialPortCreator.createSerialPort("COM1"), input);
                logger.appendText("COM1 initialized for writing ...\n");
                receiver = new PortAndHisTextArea(SerialPortCreator.createSerialPort("COM2"), output);
                logger.appendText("COM2 initialized for reading...\n");
            } catch (SerialPortException spe) {
                writer = new PortAndHisTextArea(SerialPortCreator.createSerialPort("COM3"), input);
                logger.appendText("COM3 initialized for writing ...\n");
                receiver = new PortAndHisTextArea(SerialPortCreator.createSerialPort("COM4"), output);
                logger.appendText("COM4 initialized for reading...\n");
            }
            SerialPortCreator.setLogger(logger);

            SerialPortCreator.setEventListenerForReceiver(receiver);
            SerialPortCreator.setPortParams(Arrays.asList(writer, receiver), Parities.valueOf(parity.getValue()));

            input.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    new SendEvent(input, logger, writer).mouseClickedEvent();
                }
            });

            parity.setOnAction(actionEvent -> SerialPortCreator.setPortParams(Arrays.asList(writer, receiver), Parities.valueOf(parity.getValue())));

        } catch (SerialPortException e) {
            logger.appendText("Port initialization error!\n");
            setDisable();
        }
    }

    public void setDisable() {
        parity.setDisable(true);
        output.setDisable(true);
        input.setDisable(true);
    }
}

