package by.lab1.event;

import by.lab1.model.PortAndHisTextArea;
import by.lab1.service.PacketMaker;
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
                String packets = new String(dataByteFormat, StandardCharsets.UTF_8);

                StringBuilder dataToOutput = new StringBuilder();

                while (!packets.isEmpty()) {
                    if (packets.lastIndexOf(PacketMaker.FLAG) != 0) {
                        var firstPAcket = packets.substring(0, packets.substring(8).indexOf(PacketMaker.FLAG) + PacketMaker.FLAG.length());
                        logger.appendText(PacketMaker.getPacketForLogger(firstPAcket) + CARRY_OVER);
                        dataToOutput.append(PacketMaker.getDataFromPacketForOutput(firstPAcket));
                        packets = packets.substring(firstPAcket.length());
                    } else {
                        logger.appendText(PacketMaker.getPacketForLogger(packets) + CARRY_OVER);
                        dataToOutput.append(PacketMaker.getDataFromPacketForOutput(packets));
                        break;
                    }
                }
                output.appendText(dataToOutput + CARRY_OVER);
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
