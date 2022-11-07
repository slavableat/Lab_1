package by.lab1.event;

import by.lab1.service.PacketMaker;
import by.lab1.model.PortAndHisTextArea;
import javafx.scene.control.TextArea;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SendEvent {
    private final TextArea input;
    private final TextArea debug;

    public SendEvent(TextArea input, TextArea debug, PortAndHisTextArea port) {
        this.input = input;
        this.debug = debug;
        this.port = port;
    }

    private final PortAndHisTextArea port;

    private static final String CARRY_OVER = "\n";


    public void mouseClickedEvent() {
        try {
            if (isPortAvailable()) {
                String text = input.getText();
                if (text.length() > 1) {
                    text = text.substring(0, text.length() - 1);
                    if (text.length() / 15 >= 1) {
                        sendTextToPort(makePackets(text));
                    } else {
                        sendTextToPort(PacketMaker.makePacket(text));
                    }
                }
                input.clear();
            } else {
                debug.appendText("Unable to send data to port!" + CARRY_OVER);
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
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
