package by.lab1.event;

import by.lab1.service.CSMACDservice;
import by.lab1.service.PacketMaker;
import by.lab1.model.PortAndHisTextArea;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import javafx.scene.control.TextArea;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class SendEvent {
    private static final int MAX_NUMBER_OF_ATTEMPTS = 10;
    private final TextArea input;
    private final TextArea debug;

    public SendEvent(TextArea input, TextArea debug, PortAndHisTextArea port) {
        this.input = input;
        this.debug = debug;
        this.port = port;
    }

    private final PortAndHisTextArea port;

    public void enterClickEvent() {
            input.setEditable(false);
            if (input.getLength() == 1) {
                input.clear();
                return;
            }
            new Thread(() -> {
                try {
                    String dataToSend = input.getText();//with carry over(((
                    input.clear();


                        //TODO почекать кол-во попоыток
                        int attemptsCounter;
                        if (isPortAvailable()) {
                            int counter = 0;
                            for (Character symbol : dataToSend.toCharArray()) {
                                attemptsCounter = 1;
                                if (symbol == '\n') {
                                    break;
                                }
                                while (attemptsCounter <= MAX_NUMBER_OF_ATTEMPTS) {
                                    while (CSMACDservice.isChannelBusy()) {
                                    }
                                    CSMACDservice.sleepDuringCollisionWindow();
                                    if (CSMACDservice.isCollisionOccured()) {
                                        attemptsCounter++;
                                        CSMACDservice.makeCollisionDelay(attemptsCounter);
                                        debug.appendText("c");
                                    } else {
                                        break;
                                    }
                                }
                                sendTextToPort(String.valueOf(symbol));
                                debug.appendText("\n");

                            }
                        } else debug.appendText("Unable to sent information!");
                } catch (SerialPortException e) {
                    e.printStackTrace();
                } finally {
                    input.setEditable(true);
                }
            }).start();
    }

    private void simulateCollision() throws SerialPortException {
        sendTextToPort((((int) (Math.random() * 2)) == 0) ? "1" : "0"); //fake collision
    }

    private static String excludeCarryOver(String text) {
        text = text.substring(0, text.length() - 1);
        return text;
    }

    private void sendTextToPort(String text) throws SerialPortException {
        port.getSerialPort().writeBytes(text.getBytes(StandardCharsets.UTF_8));
    }

    private boolean isPortAvailable() {
        String[] portNames = SerialPortList.getPortNames();
        return Arrays.asList(portNames).contains(port.getPortName());
    }

    private String makePackets(String temp) throws SerialPortException {
        StringBuilder packets = new StringBuilder();
        while (temp.length() / 15 != 0) {
            var packet = PacketMaker.makePacket(temp.substring(0, 15));
            temp = temp.substring(15);
            packets.append(packet);
        }
        if (!temp.isEmpty()) {
            packets.append(PacketMaker.makePacket(temp));
        }
        return packets.toString();
    }
}
