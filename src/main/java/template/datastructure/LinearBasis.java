package template.datastructure;

import java.util.Arrays;

public class LinearBasis implements Cloneable {
    private long[] map = new long[64];
    /**
     * map[i] = xor of source[i]
     */
    private long[] source = new long[64];
    private long set;

    public int size() {
        return Long.bitCount(set);
    }

    public void clear() {
        set = 0;
        Arrays.fill(map, 0);
        Arrays.fill(source, 0);
    }

    public long representation(long x) {
        long ans = 0;
        for (int i = 63; i >= 0 && x != 0; i--) {
            if (bitAt(x, i) == 0 || map[i] == 0) {
                continue;
            }
            x ^= map[i];
            ans ^= 1L << i;
        }
        assert x == 0;
        return ans;
    }

    public long representationOriginal(long x) {
        long ans = 0;
        for (int i = 63; i >= 0 && x != 0; i--) {
            if (bitAt(x, i) == 0 || map[i] == 0) {
                continue;
            }
            x ^= map[i];
            ans ^= source[i];
        }
        assert x == 0;
        return ans;
    }

    /**
     * translate bits returned by most methods into original style
     *
     * @param x
     * @return
     */
    public long getOriginal(long x) {
        long ans = 0;
        for (int i = 0; i < 64; i++) {
            if (bitAt(x, i) == 1) {
                ans ^= source[i];
            }
        }
        return ans;
    }

    public long[] toArray() {
        long[] ans = new long[size()];
        int tail = 0;
        for (int i = 63; i >= 0; i--) {
            if (map[i] != 0) {
                ans[tail++] = map[i];
            }
        }
        return ans;
    }

    /**
     * return the index of  added element ([0,64)), -1 means can't add val
     *
     * @param val
     * @return
     */
    public int add(long val) {
        long state = 0;
        for (int i = 63; i >= 0 && val != 0; i--) {
            if (bitAt(val, i) == 0 || map[i] == 0) {
                continue;
            }
            val ^= map[i];
            state ^= source[i];
        }
        if (val != 0) {
            int log = 63 - Long.numberOfLeadingZeros(val);
            map[log] = val;
            source[log] = state | (1L << log);
            set |= 1L << log;
            return log;
        }
        return -1;
    }

    /**
     * Check whether val can be get by xor the numbers in basis
     */
    public boolean contain(long val) {
        for (int i = 63; i >= 0 && val != 0; i--) {
            if (bitAt(val, i) == 0) {
                continue;
            }
            val ^= map[i];
        }
        return val == 0;
    }

    private long bitAt(long val, int i) {
        return (val >>> i) & 1;
    }

    public long xorNumberCount() {
        return 1L << size();
    }

    /**
     * Find the k-th smallest possible generated number, and we consider 0 is the 0-th smallest.
     */
    public long theKthSmallestNumber(long k) {
        int id = 0;
        long num = 0;
        for (int i = 0; i < 64; i++) {
            if (map[i] == 0) {
                continue;
            }
            if (bitAt(k, id) == 1) {
                num ^= map[i];
            }
            id++;
        }
        return num;
    }

    /**
     * The rank of n in all generated numbers, 0's rank is 0
     */
    public long theRankOfNumber(long n) {
        int index = size() - 1;
        long rank = 0;
        for (int i = 63; i >= 0; i--) {
            if (map[i] == 0) {
                continue;
            }
            if (bitAt(n, i) == 1) {
                rank |= 1L << index;
                n ^= map[i];
            }
            index--;
        }
        return rank;
    }

    /**
     * Find the maximum value x ^ v where v is generated
     */
    public long theMaximumNumberXor(long x) {
        for (int i = 0; i < 64; i++) {
            if (map[i] == 0) {
                continue;
            }
            if (bitAt(x, i) == 0) {
                x ^= map[i];
            }
        }
        return x;
    }

    @Override
    public LinearBasis clone() {
        try {
            LinearBasis ans = (LinearBasis) super.clone();
            ans.map = ans.map.clone();
            ans.source = ans.source.clone();
            return ans;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }
}
