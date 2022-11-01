package by.lab1.service;

public class BitStuffer {
    private final String flag;
    private char reverseLastSymbol;

    public BitStuffer(String flag8bit) {
        this.flag = flag8bit;
        checkLastSymbol();
    }

    private void checkLastSymbol(){
        var lastSymbol = this.flag.charAt(this.flag.length() - 1);
        if (lastSymbol == '1') reverseLastSymbol = '0';
        else reverseLastSymbol = '1';
    }

    public String bitStaff(String data) {
        return data.replaceAll(flag.substring(0, flag.length() - 1), flag.substring(0, flag.length() - 1) + reverseLastSymbol);
    }

    public String debitStaff(String data) {
        return data.replaceAll(flag.substring(0, flag.length() - 1) + reverseLastSymbol, flag.substring(0, flag.length() - 1));
    }
}
