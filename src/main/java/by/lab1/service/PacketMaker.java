package by.lab1.service;


import by.lab1.utils.PacketUtils;

import java.util.ArrayList;
import java.util.List;

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
            return dataBits + HammingService.setHammingCodeWithParityBit(dataBits);
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
                //todo найти разницу между конрольными битами без бита паритета и передать их массивом в метод getErrorBitPosition(...)
                var rightHammingCode = PacketUtils.getHammingCodeFromHammingCodeAndParity(HammingService.setHammingCodeWithParityBit(PacketUtils.getDataBitsFromPacket(packet)));
                var receivedHammingCode = PacketUtils.getHammingCodeFromPacket(packet);
                Integer right = Integer.parseInt(rightHammingCode, 2);
                Integer received = Integer.parseInt(receivedHammingCode, 2);
                String result = Integer.toBinaryString(right ^ received);
                result = PacketUtils.leftConcatWithZeroes(result, rightHammingCode.length());
                List<Integer> indexes = new ArrayList<>();
                for (int i = 0; i < result.length(); i++) {
                    if (result.charAt(i) == '1') indexes.add(i);
                }
                var data = PacketUtils.getDataBitsFromPacket(packet);
                var indexOfError = HammingService.getErrorBitPosition(PacketUtils.getLengthFromPacket(packet), indexes);
                char[] dataChars = data.toCharArray();
//                System.out.println(dataChars);
//                System.out.println(indexOfError + " index");
                dataChars[indexOfError] = dataChars[indexOfError] == '1' ? '0' : '1';
                return String.valueOf(dataChars);
            } else if (errorCount == 2) {
//                System.out.println("two");
            } else if (errorCount == 0) {
//                System.out.println("zero");
            }
            return PacketUtils.getDataBitsFromPacket(packet);
        }
    }

    //TODO информация для логгера
    //    public static String getPacketForLogger(String packet) {
//
//    }
    public static void main(String[] args) {
//        System.out.println(getLengthDataFCSFromPacket("01100010-0000-0000-1111-010100101010001-00110"));
        System.out.println(getDataFromPacketForOutput("01100010000000001111" + PacketUtils.generateRandomError("010100101010001") + "00110"));
        System.out.println(getDataFromPacketForOutput("01100010000000001111" + PacketUtils.generateRandomError("010100101010001") + "00110"));
        System.out.println(getDataFromPacketForOutput("01100010000000001111" + PacketUtils.generateRandomError("010100101010001") + "00110"));
        System.out.println(getDataFromPacketForOutput("01100010000000001111" + PacketUtils.generateRandomError("010100101010001") + "00110"));
        System.out.println(getDataFromPacketForOutput("01100010000000001111" + PacketUtils.generateRandomError("010100101010001") + "00110"));
        System.out.println(getDataFromPacketForOutput("01100010000000001111" + PacketUtils.generateRandomError("010100101010001") + "00110"));
        System.out.println(getDataFromPacketForOutput("01100010000000001111" + PacketUtils.generateRandomError("010100101010001") + "00110"));
        System.out.println(getDataFromPacketForOutput("01100010000000001111" + PacketUtils.generateRandomError("010100101010001") + "00110"));
        System.out.println(getDataFromPacketForOutput("01100010000000001111" + PacketUtils.generateRandomError("010100101010001") + "00110"));
        System.out.println(getDataFromPacketForOutput("01100010000000001111" + PacketUtils.generateRandomError("010100101010001") + "00110"));
    }
}