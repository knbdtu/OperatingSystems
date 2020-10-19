package com.knbdtu.operatingsystems;

class MyRunnable implements Runnable {

    public void run() {
        for(int i  = 0; i < 100; i++) {
            System.out.println(i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }

        }
    }
}

public class RunnableDemo {
    public static void main(String[] args) throws InterruptedException {
        Runnable myRunnable = new MyRunnable();
        Thread t1 = new Thread(myRunnable, "Nitin");
        t1.start();
    }
}
