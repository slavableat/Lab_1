package by.lab1.service;


import org.apache.commons.lang3.StringUtils;

public class PacketMaker {
    public final static String FLAG = "01100010";
    private final static String DESTINATION_ADRESS = "0000";
    private final static String SOURCE_ADDRESS = "0000";

    private final static String FCS_ZERO = "0";
    public static final int LENGTH_OF_DATA_LENGTH_FIELD = 4;

    public static String makePacket(String data) {
        return FLAG +
                DESTINATION_ADRESS +
                SOURCE_ADDRESS +
                getBynaryStringOfLength(data) +
                getBitstuffedDataWithZEROfcsORDataWithHammingCodeFCS(data);
    }

    private static String getBitstuffedDataWithZEROfcsORDataWithHammingCodeFCS(String dataBits) {
        if (dataBits.contains(FLAG.substring(0, FLAG.length() - 1))) {
            return new BitStuffer(FLAG).bitStaff(dataBits) + FCS_ZERO;
        } else {
            return dataBits + HammingService.setHammingCodeWithParityBit(dataBits);
        }
    }

    private static String getBynaryStringOfLength(String data) {
        var lengthOfData = Integer.toBinaryString(data.length());
        while (lengthOfData.length() < LENGTH_OF_DATA_LENGTH_FIELD) {
            lengthOfData = "0".concat(lengthOfData);
        }
        return lengthOfData;
    }

    public static String getDataFromPacketForOutput(String packet) {
        var startIndexOfData = FLAG.length() + DESTINATION_ADRESS.length() + SOURCE_ADDRESS.length() + LENGTH_OF_DATA_LENGTH_FIELD;
        String bitStuffedOrNotBitStuffedData = packet.substring(startIndexOfData, packet.length() - FCS_ZERO.length());
        if (bitStuffedOrNotBitStuffedData.contains(FLAG.substring(0, FLAG.length() - 1))) {//без последнего реверсного бита
            var debitStuffedData = new BitStuffer(FLAG).debitStaff(bitStuffedOrNotBitStuffedData);
            return debitStuffedData.substring(0, debitStuffedData.length() - 1);
        } else {
            var startIndexOfLengthDataFCS = FLAG.length() + DESTINATION_ADRESS.length() + SOURCE_ADDRESS.length();
            int errorCount = HammingService.getErrorCounts(packet.substring(startIndexOfLengthDataFCS));
            if (errorCount == 0 || errorCount == 2) {
                Integer len = Integer.valueOf(packet.substring(startIndexOfLengthDataFCS, startIndexOfLengthDataFCS + LENGTH_OF_DATA_LENGTH_FIELD), 2);
                return packet.substring(startIndexOfData, startIndexOfData + len);
            } else if (errorCount == 1)
                //todo найти разницу между конрольными битами без бита паритета и передать их массивом в метод getErrorBitPosition(...)
                return null;
        }
        return null;
    }


    //    public static String getPacketForLogger(String packet) {
//
//    }
    public static void main(String[] args) {
//        System.out.println(getDataFromPacketForOutput("0110001000000000010011110010"));
        System.out.println(StringUtils.indexOfDifference("1111", "0111"));
    }
}