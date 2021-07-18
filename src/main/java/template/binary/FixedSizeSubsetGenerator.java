package template.binary;

public class FixedSizeSubsetGenerator {
    int v;
    int[] actualBitsPs = new int[32];
    int[] offset = new int[32];
    int size;
    int virtualMask;

    int l;
    int r;
    int k;
    int mask;

    /**
     * O(bitcount(v))
     *
     * @param v
     * @param k
     */
    public void init(int v, int k) {
        assert v != -1;
        this.k = k;
        this.v = v;
        size = 0;
        int cur = -1;
        while (v != 0) {
            cur++;
            if ((v & 1) == 1) {
                offset[size++] = cur;
            }
            v >>>= 1;
        }
        if (size < k) {
            return;
        }
        for (int i = 0; i < size; i++) {
            actualBitsPs[i] = 1 << offset[i];
            if (i > 0) {
                actualBitsPs[i] |= actualBitsPs[i - 1];
            }
        }
        mask = 0;
        first = true;
    }

    boolean first;

    /**
     * O(1) average
     *
     * @return
     */
    public boolean next() {
        if (first) {
            first = false;
            l = 0;
            r = k - 1;
            if (r >= size) {
                return false;
            }
            mask = actualBitsPs[k - 1];
            virtualMask = (1 << k) - 1;
            return true;
        }

        if (l > r) {
            l = l + 1;
            assert Bits.get(virtualMask, l) == 1;
            r = l;
            while (r + 1 < size && Bits.get(virtualMask, r + 1) == 1) {
                r++;
            }
        }

        if (r + 1 >= size) {
            return false;
        }
        //empty
        mask ^= (1 << offset[r]) ^ (1 << offset[r + 1]);
        virtualMask ^= (1 << r) ^ (1 << r + 1);
        r--;
        if (l <= r && l > 0) {
            //move back
            mask ^= actualBitsPs[r] ^ actualBitsPs[l - 1];
            virtualMask ^= ((1 << r + 1) - 1) ^ ((1 << l) - 1);
            r -= l;
            l = 0;
            mask ^= actualBitsPs[r];
            virtualMask ^= (1 << r + 1) - 1;
        }

        assert Integer.bitCount(virtualMask) == k;
        return true;
    }

    public int mask() {
        return mask;
    }

}
