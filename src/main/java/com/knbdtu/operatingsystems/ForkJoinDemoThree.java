package com.knbdtu.operatingsystems;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class ReciprocalSumArrayTwo extends RecursiveTask<Double> {

    int MIN_SEQUENTIAL_THRESHOLD = 5;

    double[] A;
    int low, high;

    public ReciprocalSumArrayTwo(double[] A, int low, int high) {
        this.A = A;
        this.low = low;
        this.high = high;
    }

    protected Double compute() {
        if(high - low <= MIN_SEQUENTIAL_THRESHOLD) {
            double sum = 0.0;
            for(int i = low; i <= high; i++) {
                sum += 1 / A[i];
            }
            return sum;
        } else {
            int mid = (low + high) / 2;
            ReciprocalSumArrayTwo left = new ReciprocalSumArrayTwo(A, low, mid);
            ReciprocalSumArrayTwo right = new ReciprocalSumArrayTwo(A, mid + 1, high);
            left.fork(); //Future async
            double rightSum = right.compute();
            double leftSum = left.join();//Future get
            return rightSum + leftSum;
        }
    }
}

public class ForkJoinDemoThree {
    public static void main(String[] args) {
        double[] A = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        ReciprocalSumArrayTwo reciprocalSumArray = new ReciprocalSumArrayTwo(A, 0, 9);
        double sum = ForkJoinPool.commonPool().invoke(reciprocalSumArray);
        System.out.println(sum);
    }
}