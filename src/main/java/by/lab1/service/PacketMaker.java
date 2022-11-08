package by.lab1.service;


import by.lab1.utils.PacketUtils;

public class PacketMaker {
    public final static String FLAG = "01100010";
    private final static String DESTINATION_ADRESS = "0000";
    private final static String SOURCE_ADDRESS = "0000";

    private final static String FCS_ZERO = "0";

    public static String makePacket(String data) {
        return FLAG +
                DESTINATION_ADRESS +
                SOURCE_ADDRESS +
                PacketUtils.getBinaryStringOfParameterLength(data) +
                getBitstuffedDataWithZEROfcsORDataWithHammingCodeFCS(data);
    }

    private static String getBitstuffedDataWithZEROfcsORDataWithHammingCodeFCS(String dataBits) {
        if (dataBits.contains(FLAG.substring(0, FLAG.length() - 1))) {
            return new BitStuffer(FLAG).bitStaff(dataBits) + FCS_ZERO;
        } else {
            return PacketUtils.generateRandomError(dataBits) + HammingService.setHammingCodeWithParityBit(dataBits);
        }
    }


    public static String getDataFromPacketForOutput(String packet) {
        String bitStuffedOrNotBitStuffedData = PacketUtils.getDataBitsWhereFCSisZeroFromPacket(packet);
        if (bitStuffedOrNotBitStuffedData.contains(FLAG.substring(0, FLAG.length() - 1))) {//без последнего реверсного бита
            var debitStuffedData = new BitStuffer(FLAG).debitStaff(bitStuffedOrNotBitStuffedData);
            return debitStuffedData.substring(0, debitStuffedData.length() - 1);
        } else {
            int errorCount = PacketUtils.getErrorCountsIntoLengthDataFCS(PacketUtils.getLengthDataFCSFromPacket(packet));
            if (errorCount == 1) {
                return PacketUtils.fixSingleErrorAndGetDataBits(packet);
            } else if (errorCount == 2) {
//                System.out.println("two");
            } else if (errorCount == 0) {
//                System.out.println("zero");
            }
            return PacketUtils.getDataBitsFromPacket(packet);
        }
    }


    public static String getPacketForLogger(String packet) {
        return PacketUtils.markFCSfield(packet);
    }
}