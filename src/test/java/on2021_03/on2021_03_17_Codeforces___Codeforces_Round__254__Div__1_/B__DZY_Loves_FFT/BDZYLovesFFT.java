package on2021_03.on2021_03_17_Codeforces___Codeforces_Round__254__Div__1_.B__DZY_Loves_FFT;



import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;
import template.utils.SortUtils;

import java.util.stream.IntStream;

public class BDZYLovesFFT {
    long x;

    long getNextX() {
        x = (x * 37 + 10007) % 1000000007;
        return x;
    }

    int[] a;
    int[] b;
    int n;
    int d;

    void initAB() {
        for (int i = 0; i < n; i = i + 1) {
            a[i] = i + 1;
        }
        for (int i = 0; i < n; i = i + 1) {
            SequenceUtils.swap(a, i, (int) getNextX() % (i + 1));
        }
        for (int i = 0; i < n; i = i + 1) {
            if (i < d)
                b[i] = 1;
            else
                b[i] = 0;
        }
        for (int i = 0; i < n; i = i + 1) {
            SequenceUtils.swap(b, i, (int) getNextX() % (i + 1));
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        a = new int[n];
        b = new int[n];
        d = in.ri();
        x = in.ri();

        initAB();
        int[] indices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(indices, (i, j) -> -Integer.compare(a[i], a[j]), 0, n);
        int[] c = new int[n];
        BitSet notSetted = new BitSet(n);
        BitSet bset = new BitSet(n);
        notSetted.fill(true);
        for (int i = 0; i < n; i++) {
            if (b[i] == 1) {
                bset.set(i);
            }
        }
        BitSet tmp = new BitSet(n);
        int remain = n;
        for (int i : indices) {
            tmp.copy(bset);
            tmp.rightShift(i);
            tmp.and(notSetted);
            for (int j = tmp.nextSetBit(0); j < tmp.capacity(); j = tmp.nextSetBit(j + 1)) {
                notSetted.clear(j);
                c[j] = a[i];
                remain--;
            }
            if(remain == 0){
                break;
            }
        }
        for (int t : c) {
            out.println(t);
        }
    }
}
