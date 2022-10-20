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
//                if(outputData.substring(9).indexOf(PacketMaker.FLAG) != -1){
//                    dataFromLastPart = outputData.substring(outputData.substring(9).indexOf(PacketMaker.FLAG));
//                }
                //todo вынести в метод
                var flag = PacketMaker.FLAG;
                while (flag.startsWith("0")) {
                    flag = flag.substring(1);
                }
                var lastSymbol = flag.charAt(flag.length() - 1);
                char reverseLastSymbol;
                if (lastSymbol == '1') reverseLastSymbol = '0';
                else reverseLastSymbol = '1';
                var flagIntoPacketAfterBitStuffing = flag.substring(0, flag.length() - 1) + (reverseLastSymbol);
                var markedFlagIntoPacketAfterBitStuffing = flag.substring(0, flag.length() - 1) + "[" + reverseLastSymbol + "]";


                if (outputData.lastIndexOf(PacketMaker.FLAG) != 0) {
                    while (!outputData.isEmpty()) {
                        if (outputData.lastIndexOf(PacketMaker.FLAG) != 0) {
                            var firstPAcket = outputData.substring(0, outputData.substring(8).indexOf(PacketMaker.FLAG) + PacketMaker.FLAG.length());
                            logger.appendText(firstPAcket.replaceAll(flagIntoPacketAfterBitStuffing,markedFlagIntoPacketAfterBitStuffing ) + CARRY_OVER);
                            var outputData1 = PacketMaker.getDataFromPacket(firstPAcket);
                            output.appendText(outputData1 + CARRY_OVER);
                            outputData = outputData.substring(firstPAcket.length());
                        } else {
                            logger.appendText(outputData.replaceAll(flagIntoPacketAfterBitStuffing,markedFlagIntoPacketAfterBitStuffing ) + CARRY_OVER);
                            outputData = PacketMaker.getDataFromPacket(outputData);
                            output.appendText(outputData + CARRY_OVER);
                            break;
                        }
                    }
                } else {
                    logger.appendText(outputData.replaceAll(flagIntoPacketAfterBitStuffing,markedFlagIntoPacketAfterBitStuffing ) + CARRY_OVER);
                    outputData = PacketMaker.getDataFromPacket(outputData);
                    output.appendText(outputData + CARRY_OVER);
                }
//1100010110001000

            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
//11000111000100
