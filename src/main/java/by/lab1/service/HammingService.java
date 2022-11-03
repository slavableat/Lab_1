package by.lab1.service;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static java.lang.Math.pow;

public class HammingService {

    public static final int LENGTH_END_INDEX = 3;

    //made by KALYAN
    static String setHammingCodeWithParityBit(String data) {
        int lengthOfData = data.length();
        int powerOfTwo = 1;
        while ((int) pow(2, powerOfTwo) <= lengthOfData) {
            powerOfTwo++;
        }
        StringBuilder fcs = new StringBuilder();
        for (int resultOfRaisingToPower = 1; resultOfRaisingToPower < (int) pow(2, powerOfTwo); resultOfRaisingToPower *= 2) {
            int resultOfXor = 0;
            for (int ordinalNumberOfBit = resultOfRaisingToPower - 1; ordinalNumberOfBit < lengthOfData; ordinalNumberOfBit += resultOfRaisingToPower) {
                int currentNumberOfRepetitions = 0;
                while (currentNumberOfRepetitions < resultOfRaisingToPower && ordinalNumberOfBit < lengthOfData) {
                    resultOfXor ^= Integer.parseInt(String.valueOf(data.charAt(ordinalNumberOfBit)));
                    currentNumberOfRepetitions++;
                    ordinalNumberOfBit++;
                }
            }
            fcs.append(resultOfXor);
        }
        int parityBit = 0;
        for (int i = 0; i < data.length(); i++) {
            parityBit ^= Integer.parseInt(String.valueOf(data.charAt(i)));
        }
        fcs.append(parityBit);
        return fcs.toString();
    }

    //todo param нумерация индексов с нуля
    public static Integer getErrorBitPosition(int lengthOfData, List<Integer> changedHammingCodeIndexes) {
        int powerOfTwo = 1;
        while ((int) pow(2, powerOfTwo) <= lengthOfData) {
            powerOfTwo++;
        }
        Map<Integer, List<Integer>> hammingCodeIndexesAndTheirDataBitIndexes = new HashMap<>();
        int ordinalNumber = 0;
        for (int resultOfRaisingToPower = 1; resultOfRaisingToPower < (int) pow(2, powerOfTwo); resultOfRaisingToPower *= 2) {
            List<Integer> subArray = new ArrayList<>();
            for (int ordinalNumberOfBit = resultOfRaisingToPower - 1; ordinalNumberOfBit < lengthOfData; ordinalNumberOfBit += resultOfRaisingToPower) {
                int currentNumberOfRepetitions = 0;
                while (currentNumberOfRepetitions < resultOfRaisingToPower && ordinalNumberOfBit < lengthOfData) {
                    subArray.add(ordinalNumberOfBit);
                    currentNumberOfRepetitions++;
                    ordinalNumberOfBit++;
                }
            }
            hammingCodeIndexesAndTheirDataBitIndexes.put(ordinalNumber++, subArray);
        }
        Map<Integer, List<Integer>> changedIndexesAndTheirDataBitIndexes = new HashMap<>();
        for (var index : changedHammingCodeIndexes) {
            changedIndexesAndTheirDataBitIndexes.put(index, hammingCodeIndexesAndTheirDataBitIndexes.get(index));
        }

        //сверяем пересекающиеся индексы
        HashSet<Integer> newSet = new HashSet<>(changedIndexesAndTheirDataBitIndexes.get(changedHammingCodeIndexes.get(0)));
        for (var index :
                changedHammingCodeIndexes) {
            newSet.retainAll(changedIndexesAndTheirDataBitIndexes.get(index));
        }

        //убираем из общей коллекции переданные в функцию индексы
        for (var index : changedHammingCodeIndexes) {
            hammingCodeIndexesAndTheirDataBitIndexes.remove(index);
        }

        //проверяем владеют ли персекающимися индксами другие, если да, то удаляем их из множества
        HashSet<Integer> copyArray = (HashSet<Integer>) newSet.clone();

        for (var resultIndex : copyArray) {
            for (var entry : hammingCodeIndexesAndTheirDataBitIndexes.entrySet()) {
                if (entry.getValue().contains(resultIndex)) {
                    newSet.remove(resultIndex);
                }
            }
        }
        return newSet.toArray().length;
    }

    public static String generateRandomError(String cadre) {
        char[] result = cadre.toCharArray();
        int randomNumber = (int) (Math.random() * 9);

        if (randomNumber % 5 == 0) {
            int errorIndex1 = (int) (Math.random() * cadre.length() - 1);
            result[errorIndex1] = result[errorIndex1] == '1' ? '0' : '1';
            int errorIndex2 = errorIndex1;
            while (errorIndex2 == errorIndex1) {
                errorIndex2 = (int) (Math.random() * cadre.length() - 1);
            }
            result[errorIndex2] = result[errorIndex2] == '1' ? '0' : '1';
        } else {
            if (randomNumber % 2 == 0) {
                int errorIndex = (int) (Math.random() * cadre.length() - 1);
                result[errorIndex] = result[errorIndex] == '1' ? '0' : '1';
            }
        }
        return String.valueOf(result);
    }

    public static int getErrorCounts(String lengthDataFCS) {
        Integer len = Integer.valueOf(lengthDataFCS.substring(0, LENGTH_END_INDEX), 2);
        String hamminCodeOfReceivedData = setHammingCodeWithParityBit(lengthDataFCS.substring(LENGTH_END_INDEX + 1, LENGTH_END_INDEX + 1 + len));
        String receivedHammingCode = lengthDataFCS.substring(LENGTH_END_INDEX + 1 + len);
        String controlBits = hamminCodeOfReceivedData.substring(0, hamminCodeOfReceivedData.length() - 1);
        String receivedControlBits = receivedHammingCode.substring(0, receivedHammingCode.length() - 1);
        if (hamminCodeOfReceivedData.charAt(hamminCodeOfReceivedData.length()) == receivedHammingCode.charAt(receivedHammingCode.length())) {
            if (!controlBits.equals(receivedControlBits)) {
                return 2;
            } else {
                return 0;
            }
        } else {
            return 1;
//            StringUtils.indexOfDifference(controlBits,receivedControlBits)
            //различные значения по порядку 0111 -- 1011 то есть индекс 0 и 1 и тд.....
        }
    }

    public static void main(String[] args) {
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(generateRandomError("1111111111111"));
        System.out.println(getErrorBitPosition(1, List.of(0)));
    }
}

