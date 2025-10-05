package algorithms;

import metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class MaxHeapTest {

    @Test
    void testEmptyExtractThrows() {
        MaxHeap h = new MaxHeap(4, null);
        assertThrows(NoSuchElementException.class, h::extractMax);
    }

    @Test
    void testInsertExtractOrder() {
        MaxHeap h = new MaxHeap(8, null);
        int[] values = {5, 1, 9, 3, 7, 2, 9, 8};
        for (int v : values) h.insert(v);
        Arrays.sort(values);
        for (int i = values.length - 1; i >= 0; i--) {
            assertEquals(values[i], h.extractMax());
        }
        assertTrue(h.isEmpty());
    }

    @Test
    void testBuildHeapFromArrayAndDuplicates() {
        int[] data = {3, 1, 4, 1, 5, 9, 2, 9};
        MaxHeap h = new MaxHeap(data, null);
        int[] out = new int[data.length];
        for (int i = 0; i < out.length; i++) out[i] = h.extractMax();
        for (int i = 1; i < out.length; i++) {
            assertTrue(out[i - 1] >= out[i]);
        }
    }

    @Test
    void testIncreaseKeyByValueAndByIndex() {
        MaxHeap h = new MaxHeap(4, null);
        h.insert(2);
        h.insert(4);
        h.insert(3);
        boolean ok = h.increaseKeyByValue(2, 10);
        assertTrue(ok);
        assertEquals(10, h.extractMax());
    }

    @Test
    void testIncreaseKeyInvalid() {
        MaxHeap h = new MaxHeap(new int[]{5, 4, 3}, null);
        assertThrows(IndexOutOfBoundsException.class, () -> h.increaseKey(10, 100));
        assertThrows(IllegalArgumentException.class, () -> h.increaseKey(0, 1));
    }
}
