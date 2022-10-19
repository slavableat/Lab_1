package by.lab1;

public class PacketMaker {
    private final static String FLAG = "01100010";
    private final static String DESTINATION_ADRESS = "0000";
    private final static String SOURCE_ADDRESS = "0000";

    private final static String FCS = "0";

    //Todo сделать разделение ифнормации на пакеты по 16 символов если ифны больше чем 16 символов, узнать нужно ли битстафить длину
    public static String makePacket(String data) {
        StringBuilder packetMaker = new StringBuilder();
        packetMaker.append(FLAG);
        packetMaker.append(DESTINATION_ADRESS);
        packetMaker.append(SOURCE_ADDRESS);
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
        String bitStuffedData = packet.substring(FLAG.length() + DESTINATION_ADRESS.length() + SOURCE_ADDRESS.length(), packet.length());
        var bitstuffer = new BitStuffer(FLAG);
        var debitStuffedData = bitstuffer.debitStaff(bitStuffedData);
        return debitStuffedData.substring(4, debitStuffedData.length() - 1); //4- length
    }
}
