package com.knbdtu.operatingsystems;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

class ASum extends RecursiveAction {

    int[] A;
    int low, high;
    int sum;

    public ASum(int[] A, int low, int high, int sum) {
        this.A = A;
        this.low = low;
        this.high = high;
        this.sum = sum;
    }

    @Override
    protected void compute() {
        if(low > high) {
            sum = 0;
            return;
        } else if(low == high) {
            sum = A[low];
            return;
        } else {
            int mid = (low + high) / 2;
            ASum left = new ASum(A, low, mid, sum);
            ASum right = new ASum(A, mid + 1, high, sum);
            invokeAll(left, right);
            sum = left.sum + right.sum;
            return;
        }
    }
}

public class ForkJoinDemo {
    public static void main(String[] args) {
        int[] A = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ASum asum = new ASum(A, 0, 9, 0);
        forkJoinPool.invoke(asum);
        System.out.println(asum.sum);
    }
}


