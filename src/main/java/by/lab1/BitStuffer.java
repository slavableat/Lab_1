package by.lab1;

public class BitStuffer {
    private String flag;
    private char reverseLastSymbol;

    public BitStuffer(String flag8bit) {
        this.flag = flag8bit;
        while (this.flag.startsWith("0")) {
            this.flag = this.flag.substring(1);
        }
        var lastSymbol = this.flag.charAt(this.flag.length() - 1);
        if (lastSymbol == '1') reverseLastSymbol = '0';
        else reverseLastSymbol = '1';
    }

    //todo лаг искать не полностью а без последнего и в любом случае вставлять 1 в конце а на стороне приема все единциы просто выпилить
    public String bitStaff(String data) {
        String str = data.replaceAll(flag.substring(0, flag.length() - 1), flag.substring(0, flag.length() - 1) + reverseLastSymbol);
//        System.out.println(data);
//        System.out.println(str);
        return str;
    }

    public String debitStaff(String data) {
        return data.replaceAll(flag.substring(0, flag.length() - 1) + reverseLastSymbol, flag.substring(0, flag.length() - 1));
    }
}
