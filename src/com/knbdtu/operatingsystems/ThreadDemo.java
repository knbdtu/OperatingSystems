package com.knbdtu.operatingsystems;

class MyThread extends Thread {
    MyThread(String name) {
        super(name);
    }

    public void run() {
        for(int i  = 0; i < 100; i++) {
            System.out.println(getName() + " " + Integer.toString(i));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(getName() + " Thread interrupted");
            }

        }
    }
}

public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        MyThread t1 = new MyThread("Nitin");
        t1.start();
    }
}
