package by.lab1;

public class PacketMaker {
    public final static String FLAG = "01100010";
    private final static String DESTINATION_ADRESS = "0000";
    private final static String SOURCE_ADDRESS = "0000";

    private final static String FCS = "0";

    public static String makePacket(String data) {
        StringBuilder packetMaker = new StringBuilder();
        packetMaker.append(FLAG);
        packetMaker.append(DESTINATION_ADRESS);
        packetMaker.append(SOURCE_ADDRESS);
        //todo make max value
        var lengthOfData = Integer.toBinaryString(data.length());
        while (lengthOfData.length() < 4) {
            lengthOfData = "0".concat(lengthOfData);
        }
        var dataForBitStaffing = lengthOfData.concat(data).concat(FCS);
        var bitstuffer = new BitStuffer(FLAG);
        var bitstuffedData = bitstuffer.bitStaff(dataForBitStaffing);
        packetMaker.append(bitstuffedData);
        return packetMaker.toString();
    }

    public static String getDataFromPacket(String packet) {
        String bitStuffedData = packet.substring(FLAG.length() + DESTINATION_ADRESS.length() + SOURCE_ADDRESS.length());
        var bitstuffer = new BitStuffer(FLAG);
        var debitStuffedData = bitstuffer.debitStaff(bitStuffedData);
        return debitStuffedData.substring(4, debitStuffedData.length() - 1); //4- count of data length bit (0-15)
    }
}
