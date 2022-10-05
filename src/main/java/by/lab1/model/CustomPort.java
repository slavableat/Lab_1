package by.lab1.model;

import jssc.SerialPort;
import javafx.scene.control.TextArea;


public class CustomPort {
    private SerialPort serialPort;
    private TextArea output;

    public CustomPort(SerialPort serialPort, TextArea output) {
        this.serialPort = serialPort;
        this.output = output;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }
    public String getPortName(){
        return serialPort.getPortName();
    }
    public TextArea getOutput() {
        return output;
    }
}
