package by.lab1.event;

import by.lab1.model.CustomPort;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SendEvent {
    private TextArea input;
    private TextArea debug;

    public SendEvent(TextArea input, TextArea debug, CustomPort port) {
        this.input = input;
        this.debug = debug;
        this.port = port;
    }

    private CustomPort port;

    private static final String CARRY_OVER = "\n";


    public void mouseClickedEvent() {
        try {
            if (isPortAvailable()) {
                byte[] message = (input.getText()).getBytes(StandardCharsets.UTF_8);
                byte[] finalMessage = Arrays.copyOf(message, message.length-1);
                port.getSerialPort().writeBytes(finalMessage);
                debug.appendText(finalMessage.length + " bytes sent" + CARRY_OVER);
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
