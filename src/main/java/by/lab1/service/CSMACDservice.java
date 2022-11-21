package by.lab1.service;

public class CSMACDservice {
    public static boolean isChannelBusy() {
        int randomNumber = (int) (Math.random() * 8);
        return randomNumber % 2 == 0;
    }

    public static boolean isCollisionOccured() {
        int randomNumber = (int) (Math.random() * 8);
        return randomNumber % 5 == 0;
    }

    //    public static void makeCollisionDelay(int numberOfAttempts) {
//        try {
////            double value = Math.pow(2, Math.min(numberOfAttempts, 10));
//            int randomNumber = (int) (Math.random() * Math.min(numberOfAttempts, 10));
//            Thread.sleep(randomNumber * 100L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
    public static void makeCollisionDelay(int numberOfAttempts) {
        try {
            Thread.sleep(Math.min(numberOfAttempts, 10) * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepDuringCollisionWindow() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
