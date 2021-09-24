package template.datastructure;

import template.binary.Bits;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerSparseTable;

import java.util.Arrays;

/**
 * <p>
 * reference: https://codeforces.com/blog/entry/92310
 * </p>
 * O(n) build and O(1) search
 * <p>
 * range min query
 * </p>
 */
public class RMQBeta {
    int n;
    IntegerComparator comp;
    static int shift = 5;
    static int blockSize = 1 << shift;
    static int andMask = blockSize - 1;
    int[] minIndices;
    int[] toLeft;
    IntegerSparseTable st;

    private int min(int a, int b) {
        return comp.compare(a, b) <= 0 ? a : b;
    }

    public void init(int n, IntegerComparator comp) {
        this.comp = comp;
        int considerPart = ((n - 1) >>> shift) + 1;
        Arrays.fill(minIndices, 0, considerPart, -1);
        for (int i = 0; i < n; i++) {
            int to = i >>> shift;
            if (minIndices[to] == -1 ||
                    comp.compare(minIndices[to], i) > 0) {
                minIndices[to] = i;
            }
        }
        st.init(considerPart, i -> minIndices[i], (a, b) -> comp.compare(a, b) <= 0 ? a : b);
        int mask = 0;
        for (int i = 0; i < n; i++) {
            if ((i & andMask) == 0) {
                mask = 0;
            }
            int b = i >>> shift;
            while (mask != 0) {
                int head = Bits.highestOneBitOffset(mask);
                if (comp.compare(i, (b << shift) | head) <= 0) {
                    mask = Bits.clear(mask, head);
                } else {
                    break;
                }
            }
            mask = Bits.set(mask, i & andMask);
            toLeft[i] = mask;
        }
    }

    public RMQBeta(int n, IntegerComparator comp) {
        this(n);
        init(n, comp);
    }

    public RMQBeta(int n) {
        this.n = n;
        minIndices = new int[DigitUtils.ceilDiv(n, blockSize)];
        st = new IntegerSparseTable(minIndices.length);
        toLeft = new int[n];
    }

    public int query(int l, int r) {
        assert l <= r;
        int bl = l >>> shift;
        int br = r >>> shift;
        int tl = l & andMask;
//        int tr = r & andMask;
        if (bl == br) {
            return Integer.numberOfTrailingZeros(toLeft[r] & Bits.tailIntMask(32 - tl)) | (bl << shift);
        }
        int res1 = Integer.numberOfTrailingZeros(toLeft[(bl << shift) | andMask] & Bits.tailIntMask(32 - tl)) | (bl << shift);
        int res2 = Integer.numberOfTrailingZeros(toLeft[r]) | (br << shift);
        int best = min(res1, res2);
        if (bl + 1 < br) {
            best = min(best, st.query(bl + 1, br - 1));
        }
        return best;
    }
}
