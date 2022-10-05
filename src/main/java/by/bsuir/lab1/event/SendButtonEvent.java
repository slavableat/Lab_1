package by.bsuir.lab1.event;

import by.bsuir.lab1.initializer.SerialPortInitializer;
import by.bsuir.lab1.model.MySerialPort;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SendButtonEvent {
    private ComboBox<String> parityButton;
    private TextArea input;
    private TextArea debug;

    public SendButtonEvent(ComboBox<String> parityButton, TextArea input, TextArea debug, MySerialPort port) {
        this.parityButton = parityButton;
        this.input = input;
        this.debug = debug;
        this.port = port;
    }

    private MySerialPort port;

    private static final String CARRY_OVER = "\n";


    public void mouseClickedEvent() {
        try {
            if (isPortAvailable()) {

                byte[] message = (input.getText()).getBytes(StandardCharsets.UTF_8);
                port.getSerialPort().writeBytes(message);
                debug.appendText(message.length + "sent" + CARRY_OVER);
                input.clear();
            } else {
                debug.appendText("Unable to send data to port!" + CARRY_OVER);
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    private boolean isPortAvailable() {
        String[] portNames = SerialPortList.getPortNames();
        return Arrays.asList(portNames).contains(port.getPortName());
    }
}
