package by.lab1.event;

import by.lab1.service.PacketMaker;
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


                //todo вынести в метод
                var flag = PacketMaker.FLAG;
                var lastSymbol = flag.charAt(flag.length() - 1);
                char reverseLastSymbol;
                //ToDo IDEA say that this condition is always false
                if (lastSymbol == '1') reverseLastSymbol = '0';

                else reverseLastSymbol = '1';
                var flagIntoPacketAfterBitStuffing = flag.substring(0, flag.length() - 1) + (reverseLastSymbol);
                var markedFlagIntoPacketAfterBitStuffing = flag.substring(0, flag.length() - 1) + "[" + reverseLastSymbol + "]";
                StringBuilder dataToOutput =  new StringBuilder();

                if (outputData.lastIndexOf(PacketMaker.FLAG) != 0) {

                    while (!outputData.isEmpty()) {
                        if (outputData.lastIndexOf(PacketMaker.FLAG) != 0) {
                            var firstPAcket = outputData.substring(0, outputData.substring(8).indexOf(PacketMaker.FLAG) + PacketMaker.FLAG.length());
                            logger.appendText(firstPAcket.replaceAll(flagIntoPacketAfterBitStuffing,markedFlagIntoPacketAfterBitStuffing ) + CARRY_OVER);
                            var outputData1 = PacketMaker.getDataFromPacket(firstPAcket);
                            dataToOutput.append(outputData1);
                            outputData = outputData.substring(firstPAcket.length());
                        } else {
                            logger.appendText(outputData.replaceAll(flagIntoPacketAfterBitStuffing,markedFlagIntoPacketAfterBitStuffing ) + CARRY_OVER);
                            outputData = PacketMaker.getDataFromPacket(outputData);
                            dataToOutput.append(outputData);
                            break;
                        }
                    }
                } else {
                    logger.appendText(outputData.replaceAll(flagIntoPacketAfterBitStuffing,markedFlagIntoPacketAfterBitStuffing ) + CARRY_OVER);
                    outputData = PacketMaker.getDataFromPacket(outputData);
                    dataToOutput.append(outputData);
                }
                output.appendText(dataToOutput + CARRY_OVER);
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
