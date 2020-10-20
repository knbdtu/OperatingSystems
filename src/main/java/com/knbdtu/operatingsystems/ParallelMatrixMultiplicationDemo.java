package com.knbdtu.operatingsystems;

import static edu.rice.pcdp.PCDP.*;

public class ParallelMatrixMultiplicationDemo {


    private static void seqMatrixMultiplication(int[][] A, int[][] B) throws Exception {
        int R1 = A.length;
        int C1 = A[0].length;

        int R2 = B.length;
        int C2 = B[0].length;

        if (!(R1 > 0 && C1 > 0 && R2 > 0 && C2 > 0)) {
            throw new Exception("DIMENSIONS_CANNOT_BE_ZERO");
        }

        if (C1 != R2) {
            throw new Exception("MATRIX_DIMENSION_ISSUE");
        }

        long stime = System.nanoTime();
        int[][] C = new int[R1][C2];

        for(int i = 0; i < R1; i++) {
            for(int j = 0; j < C2; j++) {
                int sum = 0;
                for(int k = 0; k < C1; k++) {
                   sum = sum + A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        }

        System.out.println("Time taken for sequential Matrix Multiplication :- "+ (System.nanoTime() - stime) / (1000 * 1000) + "ms");
    }

    private static void parallelMatrixMultiplication(int[][] A, int[][] B) throws Exception {
        int R1 = A.length;
        int C1 = A[0].length;

        int R2 = B.length;
        int C2 = B[0].length;

        if (!(R1 > 0 && C1 > 0 && R2 > 0 && C2 > 0)) {
            throw new Exception("DIMENSIONS_CANNOT_BE_ZERO");
        }

        if (C1 != R2) {
            throw new Exception("MATRIX_DIMENSION_ISSUE");
        }

        long stime = System.nanoTime();
        int[][] C = new int[R1][C2];

        forall2dChunked(0, R1 - 1, 0, C2 - 1,  (i, j) ->
                {
                    C[i][j] = 0;
                    for(int k = 0; k < C1; k++) {
                        C[i][j] += A[i][k] * B[k][j];
                    }
                }
        );

        System.out.println("Time taken for parallel Matrix Multiplication :- "+ (System.nanoTime() - stime) / (1000 * 1000) + "ms");
    }

    public static void main(String[] args) {
        int[][] A = new int[501][501];
        int[][] B = new int[501][501];

        for(int i = 0; i < 501; i++) {
            for(int j = 0; j < 501; j++) {
                A[i][j] = i + j;
                B[i][j] = i + j;
            }
        }

        try {
            seqMatrixMultiplication(A, B);
        } catch (Exception e) {
            System.out.println("Issue with sequential Matrix Multiplication");
        }

        try {
            parallelMatrixMultiplication(A, B);
        } catch (Exception e) {
            System.out.println("Issue with parallel Matrix Multiplication");
        }

    }
}
