package com.knbdtu.operatingsystems;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

class ReciprocalSumArray extends RecursiveAction {

    int MIN_SEQUENTIAL_THRESHOLD = 5;

    double[] A;
    int low, high;
    double sum = 0;

    public ReciprocalSumArray(double[] A, int low, int high, double sum) {
        this.A = A;
        this.low = low;
        this.high = high;
        this.sum = sum;
    }

    @Override
    public void compute() {
        if(high - low <= MIN_SEQUENTIAL_THRESHOLD) {
            for(int i = low; i <= high; i++) {
                sum += 1 / A[i];
            }
        } else {
            int mid = (low + high) / 2;
            ReciprocalSumArray left = new ReciprocalSumArray(A, low, mid, sum);
            ReciprocalSumArray right = new ReciprocalSumArray(A, mid + 1, high, sum);
            left.fork();
            right.compute();
            left.join();
            sum = left.sum + right.sum;
        }
    }
}

public class ForkJoinDemoTwo {
    public static void main(String[] args) {
        double[] A = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ReciprocalSumArray reciprocalSumArray = new ReciprocalSumArray(A, 0, 9, 0.0);
        forkJoinPool.invoke(reciprocalSumArray);
        System.out.println(reciprocalSumArray.sum);
    }
}
