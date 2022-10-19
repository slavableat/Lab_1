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
    private static String dataFromLastPart;

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
//                if(outputData.substring(9).indexOf(PacketMaker.FLAG) != -1){
//                    dataFromLastPart = outputData.substring(outputData.substring(9).indexOf(PacketMaker.FLAG));
//                }
                if (outputData.lastIndexOf(PacketMaker.FLAG) != 0) {
                    while (!outputData.isEmpty()) {
                        if (outputData.lastIndexOf(PacketMaker.FLAG) != 0) {
                            var firstPAcket = outputData.substring(0, outputData.substring(8).indexOf(PacketMaker.FLAG) + PacketMaker.FLAG.length());
                            logger.appendText(firstPAcket + CARRY_OVER);
                            var outputData1 = PacketMaker.getDataFromPacket(firstPAcket);
                            output.appendText(outputData1 + CARRY_OVER);
                            outputData = outputData.substring(firstPAcket.length());
                        } else {
                            logger.appendText(outputData + CARRY_OVER);
                            outputData = PacketMaker.getDataFromPacket(outputData);
                            output.appendText(outputData + CARRY_OVER);
                            break;
                        }
                    }
                } else {
                    logger.appendText(outputData + CARRY_OVER);
                    outputData = PacketMaker.getDataFromPacket(outputData);
                    output.appendText(outputData + CARRY_OVER);
                }
//111111111111111111111111111111111111111111111111

            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
