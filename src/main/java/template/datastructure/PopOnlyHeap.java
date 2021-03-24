package template.datastructure;

import template.utils.HeapUtils;
import template.utils.SequenceUtils;

import java.util.Comparator;
import java.util.function.IntFunction;

public class PopOnlyHeap<T> {
    Object[] data;
    Comparator cmp;
    int n;

    public PopOnlyHeap(int n, Comparator cmp) {
        data = new Object[n + 1];
        this.cmp = cmp;
    }

    /**
     * O(n)
     * @param func
     * @param n
     */
    public void init(IntFunction<T> func, int n) {
        this.n = n;
        for (int i = 1; i <= n; i++) {
            data[i] = func.apply(i - 1);
        }
        HeapUtils.heapify(data, n, cmp);
    }

    public int size() {
        return n;
    }

    /**
     * O(log_2n)
     * @return
     */
    public T pop() {
        T ans = (T) data[1];
        SequenceUtils.swap(data, 1, n);
        n--;
        if (n > 0) {
            HeapUtils.moveDown(data, n, 1, cmp);
        }
        return ans;
    }

    public T peek(){
        return (T) data[1];
    }
}
