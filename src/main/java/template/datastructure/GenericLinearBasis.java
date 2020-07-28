package template.datastructure;

public class GenericLinearBasis {
    private BitSet[] basis;
    private int size;
    private int dimension;
    private BitSet or;
    private BitSet buf;

    /**
     * O(n^2/32)
     * @param dimension
     */
    public GenericLinearBasis(int dimension) {
        this.dimension = dimension;
        basis = new BitSet[dimension];
        for (int i = 0; i < dimension; i++) {
            basis[i] = new BitSet(dimension);
        }
        or = new BitSet(dimension);
        buf = new BitSet(dimension);
    }

    /**
     * O(n^2/32)
     */
    public void clear() {
        size = 0;
        or.fill(false);
    }

    /**
     * <p>Determine is bits can be written as linear composition of existing bases</p>
     * <p>O(n\log_2n)</p>
     */
    public boolean test(BitSet bits) {
        buf.copy(bits);
        bits = buf;
        for (int i = dimension - 1; i >= 0; i--) {
            if (!bits.get(i)) {
                continue;
            }
            if (or.get(i)) {
                bits.xor(basis[i]);
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * O(n\log_2n)
     * @param bits
     */
    public void add(BitSet bits) {
        buf.copy(bits);
        bits = buf;
        for (int i = dimension - 1; i >= 0; i--) {
            if (!bits.get(i)) {
                continue;
            }
            if (or.get(i)) {
                bits.xor(basis[i]);
                continue;
            }
            or.set(i);
            size++;
            basis[i].copy(bits);
            for (int j = i + 1; j < dimension; j++) {
                if (!or.get(j) || !basis[j].get(i)) {
                    continue;
                }
                basis[j].xor(basis[i]);
            }
            return;
        }
    }

    /**
     * O(1)
     * @return
     */
    public int size() {
        return size;
    }
}
