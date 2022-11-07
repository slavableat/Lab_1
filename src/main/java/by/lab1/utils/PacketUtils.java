package by.lab1.utils;

import by.lab1.service.HammingService;
import by.lab1.service.PacketMaker;

import java.util.ArrayList;
import java.util.List;

public class PacketUtils {

    public static final int FLAG_LENGTH = 8;
    public static final int DESTINATION_ADDRESS_LENGTH = 4;
    public static final int SOURCE_ADDRESS_LENGTH = 4;
    public static final int LENGTH_LENGTH = 4;
    public static final int FCS_ZERO_LENGTH = 0;


    public static String getLengthDataFCSFromPacket(String packet) {
        return packet.substring(FLAG_LENGTH + DESTINATION_ADDRESS_LENGTH + SOURCE_ADDRESS_LENGTH);
    }

    public static char getParityBitFromLengthDataFCS(String lengthDataFCS) {
        int len = getLengthFromLengthDataFCS(lengthDataFCS);
        String receivedHammingCodeAndParity = lengthDataFCS.substring(LENGTH_LENGTH + len);
        return receivedHammingCodeAndParity.charAt(receivedHammingCodeAndParity.length() - 1);
    }//01234567-89AB-CDEF-GHO0-1111-1111

    public static String getDataBitsFromLengthDataFCS(String lengthDataFCS) {
        var length = getLengthFromLengthDataFCS(lengthDataFCS);
        return lengthDataFCS.substring(LENGTH_LENGTH, LENGTH_LENGTH + length);
    }

    public static String getHammingCodeFromHammingCodeAndParity(String hammingCodeAndParity) {
        return hammingCodeAndParity.substring(0, hammingCodeAndParity.length() - 1);
    }

    public static char getParityBitForHammingCodeAndParity(String hammingCodeAndParity) {
        return hammingCodeAndParity.charAt(hammingCodeAndParity.length() - 1);
    }

    public static int getLengthFromPacket(String packet) {
        var lengthDataFCS = getLengthDataFCSFromPacket(packet);
        return Integer.valueOf(lengthDataFCS.substring(0, LENGTH_LENGTH), 2);
    }

    public static int getLengthFromLengthDataFCS(String lengthDataFCS) {
        return Integer.valueOf(lengthDataFCS.substring(0, LENGTH_LENGTH), 2);
    }

    //0000-0000-1111
    public static String getHammingCodeFromLengthDataFCS(String lengthDataFCS) {
        var len = getLengthFromLengthDataFCS(lengthDataFCS);
        String receivedHammingCode = lengthDataFCS.substring(LENGTH_LENGTH + len);
        return receivedHammingCode.substring(0, receivedHammingCode.length() - 1);
    }

    public static String generateRandomError(String dataBits) {
        char[] result = dataBits.toCharArray();
        int randomNumber = (int) (Math.random() * 9);

        if (randomNumber % 5 == 0) {
            int errorIndex1 = (int) (Math.random() * dataBits.length() - 1);
            result[errorIndex1] = result[errorIndex1] == '1' ? '0' : '1';
            int errorIndex2 = errorIndex1;
            while (errorIndex2 == errorIndex1) {
                errorIndex2 = (int) (Math.random() * dataBits.length() - 1);
            }
            result[errorIndex2] = result[errorIndex2] == '1' ? '0' : '1';
        } else {
            if (randomNumber % 2 == 0) {
                int errorIndex = (int) (Math.random() * dataBits.length() - 1);
                result[errorIndex] = result[errorIndex] == '1' ? '0' : '1';
            }
        }
        return String.valueOf(result);
    }

    public static int getErrorCountsIntoLengthDataFCS(String lengthDataFCS) {
        String rightHammingCodeAndParity = HammingService.setHammingCodeWithParityBit(PacketUtils.getDataBitsFromLengthDataFCS(lengthDataFCS));
        String hammingCode = PacketUtils.getHammingCodeFromHammingCodeAndParity(rightHammingCodeAndParity);
        if (PacketUtils.getParityBitForHammingCodeAndParity(rightHammingCodeAndParity) == PacketUtils.getParityBitFromLengthDataFCS(lengthDataFCS)) {
            if (!hammingCode.equals(PacketUtils.getHammingCodeFromLengthDataFCS(lengthDataFCS))) {
                return 2;
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }

    public static String getBinaryStringOfParameterLength(String data) {
        var lengthOfData = Integer.toBinaryString(data.length());
        return leftConcatWithZeroes(lengthOfData, LENGTH_LENGTH);
    }

    public static String leftConcatWithZeroes(String binaryInteger, int requiredLength) {
        while (binaryInteger.length() < requiredLength) {
            binaryInteger = "0".concat(binaryInteger);
        }
        return binaryInteger;
    }

    /// length-data-0
    public static String getDataBitsWhereFCSisZeroFromPacket(String packet) {
        var startIndexOfData = FLAG_LENGTH + DESTINATION_ADDRESS_LENGTH + SOURCE_ADDRESS_LENGTH + LENGTH_LENGTH;
        return packet.substring(startIndexOfData, packet.length() - FCS_ZERO_LENGTH);
    }

    public static String getDataBitsFromPacket(String packet) {
        return getDataBitsFromLengthDataFCS(PacketUtils.getLengthDataFCSFromPacket(packet));
    }

    public static String getHammingCodeFromPacket(String packet) {
        return getHammingCodeFromLengthDataFCS(getLengthDataFCSFromPacket(packet));
    }

    public static String fixSingleErrorAndGetDataBits(String packet) {
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
        dataChars[indexOfError] = dataChars[indexOfError] == '1' ? '0' : '1';
        return String.valueOf(dataChars);
    }

    public static String getFlagIntoPacketAfterBitStuffing() {
        var flag = PacketMaker.FLAG;
        var lastSymbol = flag.charAt(flag.length() - 1);
        char reverseLastSymbol;
        //ToDo IDEA say that this condition is always false
        if (lastSymbol == '1') reverseLastSymbol = '0';

        else reverseLastSymbol = '1';
        return flag.substring(0, flag.length() - 1) + (reverseLastSymbol);
    }

    public static String markFlagCombinations(String packet) {
        var flagIntoPacketAfterBitStuffing = PacketUtils.getFlagIntoPacketAfterBitStuffing();
        var markedFlagIntoPacketAfterBitStuffing = flagIntoPacketAfterBitStuffing.substring(0, flagIntoPacketAfterBitStuffing.length() - 1)
                + "[" + flagIntoPacketAfterBitStuffing.charAt(flagIntoPacketAfterBitStuffing.length() - 1) + "]";

        return packet.replaceAll(flagIntoPacketAfterBitStuffing, markedFlagIntoPacketAfterBitStuffing);
    }

    public static String markFCSfield(String packet) {
        int lengthOfFCS = HammingService.setHammingCodeWithParityBit(PacketUtils.getDataBitsFromPacket(packet)).length();
        return packet.substring(0, packet.length() - lengthOfFCS) + "(" + packet.substring(packet.length() - lengthOfFCS - 1) + ")";
    }
}
