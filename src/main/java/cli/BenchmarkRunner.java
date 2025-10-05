package cli;

import algorithms.MaxHeap;
import metrics.PerformanceTracker;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class BenchmarkRunner {
    public static void main(String[] args) {
        int[] sizes = new int[] {100, 1000, 10000, 50000};
        String csvFile = "DAA_Assignment_2-results.csv";
        try (FileWriter fw = new FileWriter(csvFile)) {
            fw.write(PerformanceTracker.csvHeader());
            fw.write(System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int n : sizes) {
            int[] base = randomArray(n);

            PerformanceTracker pt = new PerformanceTracker();
            pt.startTimer();
            MaxHeap heap = new MaxHeap(base, pt);
            pt.stopTimer();
            writeCsv("build", n, pt);

            pt.reset();
            int[] copy = Arrays.copyOf(base, base.length);
            heap = new MaxHeap(copy, pt);
            pt.startTimer();
            while (!heap.isEmpty()) heap.extractMax();
            pt.stopTimer();
            writeCsv("extract_all", n, pt);

            System.out.println("n=" + n + " done");
        }
        System.out.println("Results written to " + csvFile);
    }

    private static void writeCsv(String op, int n, PerformanceTracker pt) {
        String line = pt.toCsv(op, n);
        try (FileWriter fw = new FileWriter("DAA_Assignment_2-results.csv", true)) {
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
