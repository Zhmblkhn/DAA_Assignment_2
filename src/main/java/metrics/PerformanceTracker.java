package metrics;

public class PerformanceTracker {
    private long comparisons;
    private long swaps;
    private long arrayAccesses;
    private long allocations;
    private long startNs;
    private long elapsedNs;

    public void reset() {
        comparisons = 0;
        swaps = 0;
        arrayAccesses = 0;
        allocations = 0;
        startNs = 0;
        elapsedNs = 0;
    }

    public void incComparisons() { comparisons++; }
    public void incComparisons(long n) { comparisons += n; }
    public void incSwaps() { swaps++; }
    public void incSwaps(long n) { swaps += n; }
    public void incArrayAccesses() { arrayAccesses++; }
    public void incArrayAccesses(long n) { arrayAccesses += n; }
    public void incAllocations() { allocations++; }
    public void incAllocations(long n) { allocations += n; }

    public void startTimer() { startNs = System.nanoTime(); }
    public void stopTimer() {
        if (startNs != 0) {
            elapsedNs += System.nanoTime() - startNs;
            startNs = 0;
        }
    }

    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getArrayAccesses() { return arrayAccesses; }
    public long getAllocations() { return allocations; }
    public long getElapsedNs() { return elapsedNs; }

    public static String csvHeader() {
        return "operation,n,time_ns,comparisons,swaps,array_accesses,allocations";
    }

    public String toCsv(String operation, int n) {
        return String.format("%s,%d,%d,%d,%d,%d",
                operation,
                n,
                getElapsedNs(),
                getComparisons(),
                getSwaps(),
                getArrayAccesses(),
                getAllocations());
    }
}
