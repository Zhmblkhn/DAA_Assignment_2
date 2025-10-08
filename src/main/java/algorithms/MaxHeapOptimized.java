package algorithms;

import metrics.PerformanceTracker;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class MaxHeapOptimized {
    private int[] heap;
    private int size;
    private final PerformanceTracker metrics;

    public MaxHeapOptimized(int initialCapacity, PerformanceTracker metrics) {
        if (initialCapacity < 0) throw new IllegalArgumentException("capacity must be >= 0");
        this.metrics = metrics;
        this.heap = new int[Math.max(1, initialCapacity)];
        if (this.metrics != null) this.metrics.incAllocations(heap.length);
        this.size = 0;
    }

    public MaxHeapOptimized(int[] data, PerformanceTracker metrics) {
        if (data == null) throw new IllegalArgumentException("data must not be null");
        this.metrics = metrics;
        this.heap = Arrays.copyOf(data, data.length);
        if (this.metrics != null) this.metrics.incAllocations(heap.length);
        this.size = data.length;
        buildHeap();
    }

    private void buildHeap() {
        for (int i = parent(size - 1); i >= 0; i--) siftDownOptimized(i);
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public int peek() {
        if (size == 0) throw new NoSuchElementException("heap is empty");
        return heap[0];
    }

    public void insert(int value) {
        ensureCapacity(size + 1);
        heap[size] = value;
        if (metrics != null) metrics.incArrayAccesses();
        size++;
        siftUpOptimized(size - 1);
    }

    public int extractMax() {
        if (size == 0) throw new NoSuchElementException("heap is empty");
        int max = heap[0];
        if (metrics != null) metrics.incArrayAccesses();
        heap[0] = heap[size - 1];
        if (metrics != null) metrics.incArrayAccesses();
        size--;
        if (size > 0) siftDownOptimized(0);
        return max;
    }

    public void increaseKey(int index, int newValue) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index out of range");
        int cur = heap[index];
        if (metrics != null) metrics.incArrayAccesses();
        if (newValue < cur) throw new IllegalArgumentException("new value must be >= current value");
        heap[index] = newValue;
        if (metrics != null) metrics.incArrayAccesses();
        siftUpOptimized(index);
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= heap.length) return;
        int oldLen = heap.length;
        int newLen = Math.max(oldLen * 2, minCapacity);
        heap = Arrays.copyOf(heap, newLen);
        if (metrics != null) metrics.incAllocations(newLen - oldLen);
    }

    private void siftUpOptimized(int idx) {
        int value = heap[idx];
        if (metrics != null) metrics.incArrayAccesses();
        while (idx > 0) {
            int p = parent(idx);
            if (metrics != null) metrics.incComparisons();
            if (heap[p] < value) {
                heap[idx] = heap[p];
                if (metrics != null) {
                    metrics.incArrayAccesses(2);
                    metrics.incSwaps();
                }
                idx = p;
            } else break;
        }
        heap[idx] = value;
        if (metrics != null) metrics.incArrayAccesses();
    }

    private void siftDownOptimized(int idx) {
        int value = heap[idx];
        if (metrics != null) metrics.incArrayAccesses();
        while (true) {
            int left = left(idx);
            if (left >= size) break;
            int right = left + 1;
            int largest = left;
            if (right < size) {
                if (metrics != null) metrics.incComparisons();
                if (heap[right] > heap[left]) largest = right;
            }
            if (metrics != null) metrics.incComparisons();
            if (heap[largest] > value) {
                heap[idx] = heap[largest];
                if (metrics != null) {
                    metrics.incArrayAccesses(2);
                    metrics.incSwaps();
                }
                idx = largest;
            } else break;
        }
        heap[idx] = value;
        if (metrics != null) metrics.incArrayAccesses();
    }

    private int parent(int i) { return (i - 1) >>> 1; }
    private int left(int i) { return (i << 1) + 1; }
}
