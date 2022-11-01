package by.lab1.service;


public class PacketMaker {
    public final static String FLAG = "01100010";
    private final static String DESTINATION_ADRESS = "0000";
    private final static String SOURCE_ADDRESS = "0000";

    private final static String FCS = "0";
    public static final int LENGTH_OF_DATA_LENGTH_FIELD = 4;

    public static String makePacket(String data) {
        return  FLAG +
                DESTINATION_ADRESS +
                SOURCE_ADDRESS +
                getBynaryStringOfLength(data) +
                new BitStuffer(FLAG).bitStaff(data) +
                FCS;
    }

    private static String getBynaryStringOfLength(String data) {
        var lengthOfData = Integer.toBinaryString(data.length());
        while (lengthOfData.length() < LENGTH_OF_DATA_LENGTH_FIELD) {
            lengthOfData = "0".concat(lengthOfData);
        }
        return lengthOfData;
    }

    public static String getDataFromPacket(String packet) {
        String bitStuffedData = packet.substring(FLAG.length() + DESTINATION_ADRESS.length() + SOURCE_ADDRESS.length() + LENGTH_OF_DATA_LENGTH_FIELD, packet.length() - FCS.length());
        var debitStuffedData = new BitStuffer(FLAG).debitStaff(bitStuffedData);
        return debitStuffedData.substring(0, debitStuffedData.length() - 1);
    }
}
