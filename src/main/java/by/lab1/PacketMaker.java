package by.lab1;

public class PacketMaker {
    private final static String FLAG = "01100010";
    private final static String DESTINATION_ADRESS = "0000";
    private final static String SOURCE_ADDRESS = "0000";

    private final static String FCS = "0000";

    //Todo сделать разделение ифнормации на пакеты по 16 символов если ифны больше чем 16 символов, узнать нужно ли битстафить длину
    public static void makePacket(String data) {
         StringBuilder packetMaker = new StringBuilder();
         packetMaker.append(FLAG);
         packetMaker.append(DESTINATION_ADRESS);
         packetMaker.append(SOURCE_ADDRESS);
         var lengthOfData = String.valueOf(data.length());
         var dataForBitStaffing = lengthOfData.concat(data);
//         var dataAfterBitStaffing = (new BitStuffer(FLAG)).bitStaff(dataForBitStaffing);
//         packetMaker.append(dataAfterBitStaffing);
         packetMaker.append(FCS);
    }
}
