package by.lab1.creator;

import by.lab1.event.SerialPortReader;
import by.lab1.model.CustomPort;
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

    public static void configurePorts(List<CustomPort> ports, Parities parity) {
        try {
            for (CustomPort port : ports) {
                port.getSerialPort().addEventListener(new SerialPortReader(port, port.getOutput(),logger),
                        SerialPort.MASK_RXCHAR);
            }
            setPortParams(ports, parity);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public static void setPortParams(List<CustomPort> ports, Parities parity) {
        for (CustomPort port : ports) {
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
