package by.lab1.event;

import by.lab1.BitStuffer;
import by.lab1.PacketMaker;
import by.lab1.model.PortAndHisTextArea;
import javafx.scene.control.TextArea;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import java.nio.charset.StandardCharsets;

public class ReadEvent implements SerialPortEventListener {
    private final PortAndHisTextArea port;
    private final TextArea output;
    private final TextArea logger;
    private static final String CARRY_OVER = "\n";

    public ReadEvent(PortAndHisTextArea port, TextArea output, TextArea logger) {
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
                logger.appendText(outputData + CARRY_OVER);
                outputData = PacketMaker.getDataFromPacket(outputData);
                output.appendText(outputData + CARRY_OVER);
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}