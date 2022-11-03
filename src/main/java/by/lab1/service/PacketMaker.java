package by.lab1.service;


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
        String bitStuffedData = packet.substring(FLAG.length() + DESTINATION_ADRESS.length() + SOURCE_ADDRESS.length() + LENGTH_OF_DATA_LENGTH_FIELD, packet.length() - FCS_ZERO.length());
        if (bitStuffedData.contains(FLAG.substring(0, FLAG.length() - 1))) {//без последнего реверсного бита
            var debitStuffedData = new BitStuffer(FLAG).debitStaff(bitStuffedData);
            return debitStuffedData.substring(0, debitStuffedData.length() - 1);
        } else {
            int errorCount = HammingService.getErrorCounts(packet.substring(FLAG.length() + DESTINATION_ADRESS.length() + SOURCE_ADDRESS.length()));
            if (errorCount == 0) {
                Integer len = Integer.valueOf(packet.substring(FLAG.length() + DESTINATION_ADRESS.length() + SOURCE_ADDRESS.length(), LENGTH_OF_DATA_LENGTH_FIELD), 2);
                return packet.substring(FLAG.length() + DESTINATION_ADRESS.length() + SOURCE_ADDRESS.length() + len);
            } else if (errorCount == 1) {
                ///исправить ошибки
                return null;
            } else if (errorCount == 2) {
                ///вернуть как есть
                return null;
            }
            return null;
        }
    }

    public static String getDataFromPacketForLogger(String packet) {
        String bitStuffedData = packet.substring(FLAG.length() + DESTINATION_ADRESS.length() + SOURCE_ADDRESS.length() + LENGTH_OF_DATA_LENGTH_FIELD, packet.length() - FCS_ZERO.length());
        if (bitStuffedData.contains(FLAG.substring(0, FLAG.length() - 1))) {//без последнего реверсного бита
            var debitStuffedData = new BitStuffer(FLAG).debitStaff(bitStuffedData);
            return debitStuffedData.substring(0, debitStuffedData.length() - 1);
        } else {
            int errorCount = HammingService.getErrorCounts(packet.substring(FLAG.length() + DESTINATION_ADRESS.length() + SOURCE_ADDRESS.length()));
            if (errorCount == 0) {
                Integer len = Integer.valueOf(packet.substring(FLAG.length() + DESTINATION_ADRESS.length() + SOURCE_ADDRESS.length(), LENGTH_OF_DATA_LENGTH_FIELD), 2);
                return packet.substring(FLAG.length() + DESTINATION_ADRESS.length() + SOURCE_ADDRESS.length() + len);
            } else if (errorCount == 1) {
                /// показать где ошибки и сказать сколько
                return null;
            } else if (errorCount == 2) {
                /// сказать что две ошибки
                return null;
            }
            return null;
        }
    }
}

