package template.datastructure;

import template.binary.Log2;

import java.util.Arrays;

public class IntLinearBasis {
    static final int BITS = 32;

    private int[] map = new int[BITS];
    /**
     * map[i] = xor of source[i]
     */
    private int[] source = new int[BITS];
    private int set;

    public int size() {
        return Integer.bitCount(set);
    }

    public void clear() {
        set = 0;
        Arrays.fill(map, 0);
        Arrays.fill(source, 0);
    }

    public int representation(int x) {
        int ans = 0;
        for (int i = BITS - 1; i >= 0 && x != 0; i--) {
            if (bitAt(x, i) == 0 || map[i] == 0) {
                continue;
            }
            x ^= map[i];
            ans ^= 1 << i;
        }
        assert x == 0;
        return ans;
    }

    public int getBitValue(int i) {
        return map[i];
    }

    public int representationOriginal(int x) {
        int ans = 0;
        for (int i = BITS - 1; i >= 0 && x != 0; i--) {
            if (bitAt(x, i) == 0 || map[i] == 0) {
                continue;
            }
            x ^= map[i];
            ans ^= source[i];
        }
        assert x == 0;
        return ans;
    }

    public int[] toArray() {
        int[] ans = new int[size()];
        int tail = 0;
        for (int i = BITS - 1; i >= 0; i--) {
            if (map[i] != 0) {
                ans[tail++] = map[i];
            }
        }
        return ans;
    }

    /**
     * return the index of  added element ([0,30)), -1 means can't add val
     *
     * @param val
     * @return
     */
    public int add(int val) {
        int state = 0;
        for (int i = BITS - 1; i >= 0 && val != 0; i--) {
            if (bitAt(val, i) == 0 || map[i] == 0) {
                continue;
            }
            val ^= map[i];
            state ^= source[i];
        }
        if (val != 0) {
            int log = Log2.floorLog(val);
            map[log] = val;
            source[log] = state | (1 << log);
            set |= 1 << log;
            return log;
        }
        return -1;
    }

    /**
     * Check whether val can be get by xor the numbers in basis
     */
    public boolean contain(int val) {
        for (int i = BITS - 1; i >= 0 && val != 0; i--) {
            if (bitAt(val, i) == 0) {
                continue;
            }
            val ^= map[i];
        }
        return val == 0;
    }

    private int bitAt(int val, int i) {
        return (val >>> i) & 1;
    }

    public int xorNumberCount() {
        return 1 << size();
    }

    /**
     * Find the k-th smallest possible generated number, and we consider 0 is the 0-th smallest.
     */
    public int theKthSmallestNumber(int k) {
        int id = 0;
        int num = 0;
        for (int i = 0; i < BITS; i++) {
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
    public int theRankOfNumber(int n) {
        int index = size() - 1;
        int rank = 0;
        for (int i = BITS - 1; i >= 0; i--) {
            if (map[i] == 0) {
                continue;
            }
            if (bitAt(n, i) == 1) {
                rank |= 1 << index;
                n ^= map[i];
            }
            index--;
        }
        return rank;
    }

    /**
     * Find the maximum value x ^ v where v belong to spanning space of this
     */
    public int theMaximumNumberXor(int x) {
        for (int i = BITS - 1; i >= 0; i--) {
            if (map[i] == 0) {
                continue;
            }
            if (bitAt(x, i) == 0) {
                x ^= map[i];
            }
        }
        return x;
    }

    /**
     * Find the minimum value x ^ v where v belong to spanning space of this
     */
    public int theMinimumNumberXor(int x) {
        for (int i = BITS - 1; i >= 0; i--) {
            if (map[i] == 0) {
                continue;
            }
            if (bitAt(x, i) == 1) {
                x ^= map[i];
            }
        }
        return x;
    }

    public void copy(IntLinearBasis model) {
        System.arraycopy(model.map, 0, map, 0, map.length);
        System.arraycopy(model.source, 0, source, 0, source.length);
        set = model.set;
    }

    @Override
    public IntLinearBasis clone() {
        try {
            IntLinearBasis ans = (IntLinearBasis) super.clone();
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
