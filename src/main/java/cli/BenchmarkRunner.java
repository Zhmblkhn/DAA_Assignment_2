package cli;

import algorithms.MaxHeap;
import algorithms.MaxHeapOptimized;
import metrics.PerformanceTracker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class BenchmarkRunner {
    public static void main(String[] args) {
        int[] sizes = new int[]{100, 1000, 10000, 50000};

        String csvFileNormal = "DAA_Assignment_2-results.csv";
        String csvFileOptimized = "DAA_Assignment_2-optimized.csv";

        try (FileWriter fw1 = new FileWriter(csvFileNormal);
             FileWriter fw2 = new FileWriter(csvFileOptimized)) {
            String header = "operation,n,time_ns,comparisons,swaps,array_accesses,allocations";
            fw1.write(header);
            fw1.write(System.lineSeparator());
            fw2.write(header);
            fw2.write(System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int n : sizes) {
            int[] base = randomArray(n);
            System.out.println("Running benchmark for n = " + n);

            PerformanceTracker pt = new PerformanceTracker();

            pt.startTimer();
            MaxHeap heap = new MaxHeap(base, pt);
            pt.stopTimer();
            writeCsv(csvFileNormal, "build", n, pt);

            pt.reset();
            int[] copy = Arrays.copyOf(base, base.length);
            heap = new MaxHeap(copy, pt);
            pt.startTimer();
            while (!heap.isEmpty()) heap.extractMax();
            pt.stopTimer();
            writeCsv(csvFileNormal, "extract_all", n, pt);

            pt.reset();
            pt.startTimer();
            MaxHeapOptimized optHeap = new MaxHeapOptimized(base, pt);
            pt.stopTimer();
            writeCsv(csvFileOptimized, "build", n, pt);

            pt.reset();
            int[] copy2 = Arrays.copyOf(base, base.length);
            optHeap = new MaxHeapOptimized(copy2, pt);
            pt.startTimer();
            while (!optHeap.isEmpty()) optHeap.extractMax();
            pt.stopTimer();
            writeCsv(csvFileOptimized, "extract_all", n, pt);

            System.out.println("n=" + n + " done");
        }

        System.out.println();
        System.out.println("Results written to:");
        System.out.println(" -> " + "DAA_Assignment_2-results.csv");
        System.out.println(" -> " + "DAA_Assignment_2-optimized.csv");
    }

    private static void writeCsv(String file, String op, int n, PerformanceTracker pt) {
        String line = String.format("%s,%d,%d,%d,%d,%d,%d",
                op,
                n,
                pt.getElapsedNs(),
                pt.getComparisons(),
                pt.getSwaps(),
                pt.getArrayAccesses(),
                pt.getAllocations());
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(line);
            fw.write(System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int[] randomArray(int n) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt(0, n * 10 + 1);
        return a;
    }
}
