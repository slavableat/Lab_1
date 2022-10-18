package by.lab1.event;

import by.lab1.model.CustomPort;
import javafx.scene.control.TextArea;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.nio.charset.StandardCharsets;

public class SerialPortReader implements SerialPortEventListener {
    private CustomPort port;
    private TextArea output;
    private TextArea logger;
    private static final String CARRY_OVER = "\n";

    public SerialPortReader(CustomPort port, TextArea output, TextArea logger) {
        this.port = port;
        this.output = output;
        this.logger = logger;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() > 0) {
            try {
                byte[] dataByteFormat = port.getSerialPort().readBytes(serialPortEvent.getEventValue());
                String outputData = new String(dataByteFormat, StandardCharsets.UTF_8);
                logger.appendText(outputData.length() + " bytes received" + CARRY_OVER);
                output.appendText(outputData + CARRY_OVER);
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
