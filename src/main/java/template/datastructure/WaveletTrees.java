package template.datastructure;

import template.math.DigitUtils;

import java.util.stream.IntStream;

/**
 * O(n\log_2M) time and O(n\log_2M/32) memory for construction
 */
public class WaveletTrees {
    private Node root;
    private long L;
    private long R;
    int[] indices;

    public WaveletTrees(long[] data) {
        indices = IntStream.range(0, data.length).toArray();
        L = Long.MAX_VALUE;
        R = Long.MIN_VALUE;
        for (long x : data) {
            L = Math.min(x, L);
            R = Math.max(x, R);
        }
        root = new Node(data, indices, new int[data.length], 0, data.length - 1, L, R);
    }

    /**
     * Find the index of k-th smallest element in [l, r]
     * @param l
     * @param r
     * @param k
     * @return
     */
    public int kthSmallestIndex(int l, int r, int k) {
        return root.kthSmallestElement(indices, 0, indices.length - 1, l, r, k);
    }

    /**
     * count how many number in range [l, r] less or equal to k
     * @param l
     * @param r
     * @param k
     * @return
     */
    public int leq(int l, int r, long k) {
        return root.leq(0, indices.length - 1, l, r, L, R, k);
    }

    /**
     * count how many number in range [l, r] greater or equal to L and less or equal to R
     * @param l
     * @param r
     * @param L
     * @param R
     * @return
     */
    public int range(int l, int r, int L, int R) {
        return leq(l, r, R) - leq(l, r, L - 1);
    }

    private static class Node {
        BitPreSum ps;
        Node left;
        Node right;

        /**
         * O(log_2M)
         *
         * @param l
         * @param r
         * @param rangeL
         * @param rangeR
         * @param L
         * @param R
         * @param key
         * @return
         */
        public int leq(int l, int r, int rangeL, int rangeR, long L, long R, long key) {
            if (ps == null) {
                return 1;
            }
            assert l <= r;
            assert rangeL >= 0 && rangeR <= r - l;
            int m = l + ps.total() - 1;
            long M = DigitUtils.floorAverage(L, R);
            int pr = ps.prefix(rangeR);
            int pl = ps.prefix(rangeL - 1);
            if (M >= key) {
                return left.leq(l, m, pl, pr - 1, L, M, key);
            }
            int inLeft = pr;
            if (rangeL > 0) {
                inLeft -= pl;
            }
            return inLeft + right.leq(m + 1, r, rangeL == 0 ? 0 : rangeL - pl, rangeR - pr, M + 1, R, key);
        }

        /**
         * O(log_2M)
         *
         * @param indices
         * @param l
         * @param r
         * @param rangeL
         * @param rangeR
         * @param k
         * @return
         */
        public int kthSmallestElement(int[] indices, int l, int r, int rangeL, int rangeR, int k) {
            if (ps == null) {
                return indices[l + k - 1];
            }
            assert k > 0;
            assert l <= r;
            assert rangeL >= 0 && rangeR <= r - l;
            int m = l + ps.total() - 1;
            int pr = ps.prefix(rangeR);
            int pl = ps.prefix(rangeL - 1);
            int inLeft = pr;
            if (rangeL > 0) {
                inLeft -= pl;
            }
            if (inLeft >= k) {
                return left.kthSmallestElement(indices, l, m, pl, pr - 1, k);
            } else {
                return right.kthSmallestElement(indices, m + 1, r, rangeL - pl, rangeR - pr, k - inLeft);
            }
        }

        /**
         * O(n\log_2M)
         *
         * @param data
         * @param indices
         * @param buf
         * @param l
         * @param r
         * @param L
         * @param R
         */
        public Node(long[] data, int[] indices, int[] buf, int l, int r, long L, long R) {
            if (L == R || l > r) {
                //stop here
                return;
            }
            long M = DigitUtils.floorAverage(L, R);
            ps = new BitPreSum(r - l + 1);
            int indicesTail = l;
            int bufTail = 0;
            for (int i = l; i <= r; i++) {
                if (data[indices[i]] <= M) {
                    indices[indicesTail++] = indices[i];
                    ps.set(i - l);
                } else {
                    buf[bufTail++] = indices[i];
                }
            }
            System.arraycopy(buf, 0, indices, indicesTail, bufTail);
            ps.build();

            left = new Node(data, indices, buf, l, indicesTail - 1, L, M);
            right = new Node(data, indices, buf, indicesTail, r, M + 1, R);
        }
    }
}
