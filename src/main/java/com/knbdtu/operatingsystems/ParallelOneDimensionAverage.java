package com.knbdtu.operatingsystems;

import java.util.Arrays;

import static edu.rice.hj.Module0.forallPhased;
import static edu.rice.hj.Module0.next;
import static edu.rice.pcdp.PCDP.*;
import edu.rice.hj.api.SuspendableException;

public class ParallelOneDimensionAverage {


    private static int N = 100000;
    private static int TOTAL_ITERATIONS = 1000;
    private static double[] myVal =new double[N + 1];
    private static double[] myNew =new double[N + 1];

    private static void runSequential() {
        Arrays.fill(myNew, 0.0);
        Arrays.fill(myVal, 0.0);

        myVal[0] = 0;
        myVal[N] = 1;
        for(int iter = 0; iter < TOTAL_ITERATIONS; iter++) {
            for(int j = 1; j <= N - 1; j++) {
                myNew[j] = (myVal[j - 1] + myVal[j + 1]) / 2.0;
            }

            double[] temp = myNew;
            myNew = myVal;
            myVal = temp;
        }
        System.out.println(myNew[N - 1]);
    }

    private static void runParallel() {
        Arrays.fill(myNew, 0.0);
        Arrays.fill(myVal, 0.0);
        myVal[0] = 0.0;
        myVal[N] = 1;

        for(int iter = 0; iter < TOTAL_ITERATIONS; iter++) {
            forallChunked(1, N - 1, (j)  ->
                {
                    myNew[j] = (myVal[j - 1] + myVal[j + 1]) / 2.0;
                }
            );

            double[] temp = myNew;
            myNew = myVal;
            myVal = temp;
        }

        System.out.println(myNew[N - 1]);
    }

    private static void runParallelGroupedTasks(final int tasks) {
        Arrays.fill(myNew, 0.0);
        Arrays.fill(myVal, 0.0);
        myVal[0] = 0.0;
        myVal[N] = 1;

        for(int iter = 0; iter < TOTAL_ITERATIONS; iter++) {
            forall(0, tasks - 1, (i)  -> {
                        for(int j = i * (N / tasks) + 1; j < (i + 1) * (N / tasks); j++)
                        {
                            myNew[j] = (myVal[j - 1] + myVal[j + 1]) / 2.0;
                        }
                    }
            );

            double[] temp = myNew;
            myNew = myVal;
            myVal = temp;
        }

        System.out.println(myNew[N - 1]);
    }

    private static void runParallelGroupedTasksWithBarrier(final int tasks) throws SuspendableException {
        Arrays.fill(myNew, 0.0);
        Arrays.fill(myVal, 0.0);
        myVal[0] = 0.0;
        myVal[N] = 1;
        forallPhased(0, tasks - 1, (i)  -> {

            for(int iter = 0; iter < TOTAL_ITERATIONS; iter++) {
                for(int j = i * (N / tasks) + 1; j < (i + 1) * (N / tasks); j++) {
                    myNew[j] = (myVal[j - 1] + myVal[j + 1]) / 2.0;
                }

                next(); // Barrier

                double[] temp = myNew;
                myNew = myVal;
                myVal = temp;
            }
        });

        System.out.println(myNew[N - 1]);
    }

    public static void main(String[] args) throws SuspendableException {

        int tasks = 2;

        Long stime = System.currentTimeMillis();
        runParallel();
        System.out.println("[Parallel] Total time taken :- "+(System.currentTimeMillis() - stime)+"ms");

        stime = System.currentTimeMillis();
        runSequential();
        System.out.println("[Sequential] Total time taken :- "+(System.currentTimeMillis() - stime)+"ms");

        stime = System.currentTimeMillis();

        runParallelGroupedTasks(tasks);
        System.out.println("[ParallelGroupedTasks="+tasks+"] Total time taken :- "+(System.currentTimeMillis() - stime)+"ms");

        stime = System.currentTimeMillis();
        runParallelGroupedTasksWithBarrier(tasks);
        System.out.println("[ParallelGroupedTasksWithBarrier="+tasks+"] Total time taken :- "+(System.currentTimeMillis() - stime)+"ms");
    }
}
