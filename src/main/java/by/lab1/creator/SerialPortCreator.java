package by.lab1.creator;

import by.lab1.event.SerialPortReader;
import by.lab1.model.PortAndHisTextArea;
import by.lab1.model.Parities;
import javafx.scene.control.TextArea;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.List;

public class SerialPortCreator {
    private static TextArea logger;

    public static SerialPort createSerialPort(String portName) throws SerialPortException {
        SerialPort port = new SerialPort(portName);
        port.openPort();
        return port;
    }

    public static void setEventListenerForReceiver(PortAndHisTextArea receiver) {
        try {
            receiver.getSerialPort().addEventListener(new SerialPortReader(receiver, receiver.getArea(), logger),
                    SerialPort.MASK_RXCHAR);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public static void setPortParams(List<PortAndHisTextArea> ports, Parities parity) {
        for (PortAndHisTextArea port : ports) {
            try {
                port.getSerialPort().setParams(
                        SerialPort.BAUDRATE_9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        parity.ordinal()
                );
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setLogger(TextArea logger) {
        SerialPortCreator.logger = logger;
    }
}
