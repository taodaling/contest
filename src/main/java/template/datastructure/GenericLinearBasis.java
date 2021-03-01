package template.datastructure;

import template.binary.Log2;

import java.math.BigInteger;
import java.util.Arrays;

public class GenericLinearBasis {
    private BitSet[] map;
    /**
     * map[i] = xor of source[i]
     */
    private BitSet[] source;
    private BitSet set;
    private int m;

    public GenericLinearBasis(int m) {
        this.m = m;
        map = new BitSet[m];
        source = new BitSet[m];
        for (int i = 0; i < m; i++) {
            map[i] = new BitSet(m);
            source[i] = new BitSet(m);
        }
        set = new BitSet(m);
        tmp = new BitSet(m);
        state = new BitSet(m);
    }

    public int size() {
        return set.size();
    }

    public void clear() {
        set.fill(false);
        for (int i = 0; i < m; i++) {
            map[i].fill(false);
            source[i].fill(false);
        }
    }

    BitSet tmp;
    BitSet state;

    public boolean representation(BitSet x, BitSet ans) {
        ans.fill(false);
        tmp.copy(x);
        x = tmp;
        for (int i = m - 1; i >= 0; i--) {
            if (!x.get(i) || !set.get(i)) {
                continue;
            }
            x.xor(map[i]);
            ans.flip(i);
        }
        return x.size() == 0;
    }

    public boolean representationOriginal(BitSet x, BitSet ans) {
        ans.fill(false);
        tmp.copy(x);
        x = tmp;
        for (int i = m - 1; i >= 0; i--) {
            if (!x.get(i) || !set.get(i)) {
                continue;
            }
            x.xor(map[i]);
            ans.xor(source[i]);
        }
        return x.size() == 0;
    }

//    public BitSet[] toArray() {
//        BitSet[] ans = new BitSet[size()];
//        int tail = 0;
//        for (int i = m - 1; i >= 0; i--) {
//            if (map[i] != 0) {
//                ans[tail++] = map[i];
//            }
//        }
//        return ans;
//    }

    /**
     * return the index of  added element ([0,64)), -1 means can't add val
     *
     * @param val
     * @return
     */
    public int add(BitSet val) {
        tmp.copy(val);
        val = tmp;
        state.fill(false);
        for (int i = m - 1; i >= 0; i--) {
            if (!val.get(i) || !set.get(i)) {
                continue;
            }
            val.xor(map[i]);
            state.xor(source[i]);
        }
        if (!val.isEmpty()) {
            int log = val.prevSetBit(val.capacity() - 1);
            map[log].copy(val);
            state.set(log);
            source[log].copy(state);
            set.set(log);
            return log;
        }
        return -1;
    }

    /**
     * Check whether val can be get by xor the numbers in basis
     */
    public boolean contain(BitSet val) {
        tmp.copy(val);
        val = tmp;
        for (int i = m - 1; i >= 0; i--) {
            if (!val.get( i)) {
                continue;
            }
            val.xor(map[i]);
        }
        return val.isEmpty();
    }

//    public void copy(GenericLinearBasis model){
//        System.arraycopy(model.map, 0, map, 0, map.length);
//        System.arraycopy(model.source, 0, source, 0, source.length);
//        set = model.set;
//    }


    public BigInteger xorNumberCount() {
        return BigInteger.ONE.shiftLeft(size());
    }

//    /**
//     * Find the k-th smallest possible generated number, and we consider 0 is the 0-th smallest.
//     */
//    public BitSet theKthSmallestNumber(BitSet k) {
//        int id = 0;
//        BitSet num = 0;
//        for (int i = 0; i < 64; i++) {
//            if (map[i] == 0) {
//                continue;
//            }
//            if (bitAt(k, id) == 1) {
//                num ^= map[i];
//            }
//            id++;
//        }
//        return num;
//    }
//
//    /**
//     * The rank of n in all generated numbers, 0's rank is 0
//     */
//    public BitSet theRankOfNumber(BitSet n) {
//        int index = size() - 1;
//        BitSet rank = 0;
//        for (int i = 63; i >= 0; i--) {
//            if (map[i] == 0) {
//                continue;
//            }
//            if (bitAt(n, i) == 1) {
//                rank |= 1L << index;
//                n ^= map[i];
//            }
//            index--;
//        }
//        return rank;
//    }
//
//    /**
//     * Find the maximum value x ^ v where v is generated
//     */
//    public BitSet theMaximumNumberXor(BitSet x) {
//        for (int i = 63; i >= 0; i--) {
//            if (map[i] == 0) {
//                continue;
//            }
//            if (bitAt(x, i) == 0) {
//                x ^= map[i];
//            }
//        }
//        return x;
//    }

//    @Override
//    public GenericLinearBasis clone() {
//        try {
//            GenericLinearBasis ans = (GenericLinearBasis) super.clone();
//            ans.map = ans.map.clone();
//            ans.source = ans.source.clone();
//            return ans;
//        } catch (CloneNotSupportedException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public String toString() {
        return set.toString();
    }
}
