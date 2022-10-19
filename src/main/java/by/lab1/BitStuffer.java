package by.lab1;

public class BitStuffer {
    private String flag;
    private char lastSymbol;
    private char reverseLastSymbol;

    public BitStuffer(String flag) {
        this.flag = flag;
        while (flag.startsWith("0")) {
            flag = flag.substring(1);
        }
        lastSymbol = flag.charAt(flag.length() - 1);
        if (lastSymbol == '1') reverseLastSymbol = '0';
        else reverseLastSymbol = '1';
    }

    public  String bitStaff(String data) {
//        var finalData = new StringBuilder();
//        int count = 0;
//        var finalData = new StringBuilder();

        String str = data.replaceAll(flag, flag.substring(0, flag.length() - 1) + reverseLastSymbol + lastSymbol);
//        for (int i = 0; i < data.length(); i++) {
//            char ch = data.charAt(i);
//            if (ch == '1') {
//
//                // count number of consecutive 1's
//                // in user's data
//                count++;
//
//                if (count < 2)
//                    finalData.append(ch);
//                else {
//
//                    // add one '0' after 5 consecutive 1's
//                    finalData.append(ch+"0");
//                    count = 0;
//                }
//            } else {
//                finalData.append(ch);
//                count = 0;
//            }
//        }

        System.out.println(data);
        System.out.println(str);
        return str;
    }

    public  String debitStaff(String data) {
        return data.replaceAll(flag.substring(0, flag.length() - 1) + reverseLastSymbol + lastSymbol, flag);
    }

//    public static void main(String[] args) {
//
//        System.out.println(debitStaff(bitStaff("11000101111100010110001011111000101100010111110001011000101111100010")));
//    }
}
