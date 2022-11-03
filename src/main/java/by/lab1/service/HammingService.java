package by.lab1.service;

import java.util.*;
import static java.lang.Math.pow;

public class HammingService {
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

    public static void main(String[] args) {

        System.out.println(getErrorBitPosition(1, List.of(0)));
    }
}
/*
package by.lab1.service;

import static java.lang.Math.pow;

public class HammingService {
    static String setHammingCode(String data)
    {
        int len = data.length();
        int r = 1;
        while((int)pow(2, r) <= len) {
            r++;
        }
        String fcs = "";
        for(int st = 1; st < (int)pow(2, r); st *= 2) {
            int cb = 0;
            for(int i = st - 1; i < len; i += st) {
                int j = 0;
                while(j < st && i < len) {
                    //todo
                    cb ^= Integer.parseInt(data.substring(i, i+1));
                    j++;
                    i++;
                }
            }
//todo
            fcs += String.valueOf(cb);
        }
        int cb = 0;
        for(int i = 0; i < data.length(); i++) {
            //todo
            cb ^= Integer.parseInt(data.substring(i, i+1));
        }
        //todo
        fcs += String.valueOf(cb);
        return fcs;
    }

    public static void main(String[] args) {
        System.out.println(setHammingCode("1101"));
    }
}

 */
