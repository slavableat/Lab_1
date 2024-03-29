package by.lab1.model;

import jssc.SerialPort;
import javafx.scene.control.TextArea;


public class PortAndHisTextArea {
    private final SerialPort serialPort;
    private final TextArea area;

    public PortAndHisTextArea(SerialPort serialPort, TextArea area) {
        this.serialPort = serialPort;
        this.area = area;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }
    public String getPortName(){
        return serialPort.getPortName();
    }
    public TextArea getArea() {
        return area;
    }
}
