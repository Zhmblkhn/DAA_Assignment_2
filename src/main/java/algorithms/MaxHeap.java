package algorithms;

import metrics.PerformanceTracker;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class MaxHeap {
    private int[] heap;
    private int size;
    private PerformanceTracker metrics;

    public MaxHeap(int initialCapacity, PerformanceTracker metrics) {
        if (initialCapacity < 0) throw new IllegalArgumentException("capacity must be >= 0");
        this.metrics = metrics;
        this.heap = new int[Math.max(1, initialCapacity)];
        if (this.metrics != null) this.metrics.incAllocations(heap.length);
        this.size = 0;
    }

    public MaxHeap(int[] data, PerformanceTracker metrics) {
        if (data == null) throw new IllegalArgumentException("data must not be null");
        this.metrics = metrics;
        this.heap = Arrays.copyOf(data, data.length);
        if (this.metrics != null) this.metrics.incAllocations(heap.length);
        this.size = data.length;
        buildHeap();
    }

    private void buildHeap() {
        for (int i = parent(size - 1); i >= 0; i--) siftDown(i);
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public int peek() {
        if (size == 0) throw new NoSuchElementException("heap is empty");
        return heap[0];
    }

    public void insert(int value) {
        ensureCapacity(size + 1);
        write(size, value);
        size++;
        siftUp(size - 1);
    }

    public int extractMax() {
        if (size == 0) throw new NoSuchElementException("heap is empty");
        int max = read(0);
        swap(0, size - 1);
        size--;
        if (size > 0) siftDown(0);
        return max;
    }

    public void increaseKey(int index, int newValue) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index out of range");
        int cur = read(index);
        if (newValue < cur) throw new IllegalArgumentException("new value must be >= current value");
        write(index, newValue);
        siftUp(index);
    }

    public boolean increaseKeyByValue(int oldValue, int newValue) {
        for (int i = 0; i < size; i++) {
            if (read(i) == oldValue) {
                increaseKey(i, newValue);
                return true;
            }
        }
        return false;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= heap.length) return;
        int oldLen = heap.length;
        int newLen = Math.max(oldLen * 2, minCapacity);
        heap = Arrays.copyOf(heap, newLen);
        if (metrics != null) metrics.incAllocations(newLen - oldLen);
    }

    private void siftUp(int idx) {
        while (idx > 0) {
            int p = parent(idx);
            if (metrics != null) metrics.incComparisons();
            if (read(p) < read(idx)) {
                swap(p, idx);
                idx = p;
            } else break;
        }
    }

    private void siftDown(int idx) {
        while (true) {
            int l = left(idx);
            if (l >= size) break;
            int r = l + 1;
            int largest = l;
            if (metrics != null) metrics.incComparisons();
            if (r < size) {
                if (metrics != null) metrics.incComparisons();
                if (read(r) > read(l)) largest = r;
            }
            if (metrics != null) metrics.incComparisons();
            if (read(idx) < read(largest)) {
                swap(idx, largest);
                idx = largest;
            } else break;
        }
    }

    private int parent(int i) { return (i - 1) >>> 1; }
    private int left(int i) { return (i << 1) + 1; }
    private int right(int i) { return (i << 1) + 2; }

    private void swap(int i, int j) {
        if (i == j) return;
        int vi = read(i);
        int vj = read(j);
        write(i, vj);
        write(j, vi);
        if (metrics != null) metrics.incSwaps();
    }

    private int read(int idx) {
        if (metrics != null) metrics.incArrayAccesses();
        return heap[idx];
    }

    private void write(int idx, int value) {
        if (metrics != null) metrics.incArrayAccesses();
        heap[idx] = value;
    }
}
