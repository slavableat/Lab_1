package by.lab1.testBitStuff;

import java.io.*;
import java.net.*;

public class BitStuffingServer {
    public static void main(String[] args) throws IOException {
        ServerSocket skt = new ServerSocket(6789);

        // Used to block until a client connects to the server
        Socket socket = skt.accept();

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        // Receiving the string from the client which
        // needs to be stuffed
        while(true){
            String s = dis.readUTF();
            System.out.println("Stuffed data from client: " + s);

            System.out.println("Unstuffed data: ");
            int cnt = 0;

            // removal of stuffed bits:
            // start from 9th bit because the first 8
            //  bits are of the special pattern.
            for (int i = 8; i < s.length() - 8; i++) {
                char ch = s.charAt(i);
                if (ch == '1') {
                    cnt++;
                    System.out.print(ch);

                    // After 5 consecutive 1's one stuffed bit
                    //'0' is added. We need to remove that.
                    if (cnt == 5) {
                        i++;
                        cnt = 0;
                    }
                } else {

                    // print unstuffed data
                    System.out.print(ch);

                    // we only need to maintain count
                    // of consecutive 1's
                    cnt = 0;
                }
            }
            System.out.println();
        }

    }
}
