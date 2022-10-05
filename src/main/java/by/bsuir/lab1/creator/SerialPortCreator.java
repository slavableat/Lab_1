package by.bsuir.lab1.creator;

import by.bsuir.lab1.event.SerialPortReader;
import by.bsuir.lab1.model.MySerialPort;
import by.bsuir.lab1.model.Parities;
import javafx.scene.control.TextArea;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.List;

public class SerialPortCreator {
    private TextArea output;
    private TextArea debug;

    public SerialPortCreator(TextArea textView, TextArea debug) {
        this.output = textView;
        this.debug = debug;
    }

    public static SerialPort createSerialPort(String portName) throws SerialPortException {
        SerialPort port = new SerialPort(portName);
        port.openPort();
        return port;
    }

    public static void configurePorts(List<MySerialPort> ports, Parities parity) {
        try {
            for (MySerialPort port : ports) {
                port.getSerialPort().addEventListener(new SerialPortReader(port, port.getOutput()),
                        SerialPort.MASK_RXCHAR);
            }
            setPortParams(ports, parity);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public static void setPortParams(List<MySerialPort> ports, Parities parity) {
        for (MySerialPort port : ports) {
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
}
