package on2020_06.on2020_06_10_TopCoder_SRM__753.MojisBag;



import template.binary.Bits;
import template.primitve.generated.datastructure.IntegerList;

import java.util.Arrays;

public class MojisBag {
    int depth = 30;

    public int maximumXor(int Q, int base, int add, int rate) {
        int[] used = new int[2];
        Arrays.fill(used, -1);

        int[] history = new int[Q];
        int historyLength = 0;


        int[] X = new int[Q];
        X[0] = add;
        for (int i = 1; i < Q; i++) X[i] = (int) (((long) X[i - 1] * base + add) % (int) (1e9 + 7));

        int[] Y = new int[Q];
        BNode tree = new BNode();
        IntegerList list = new IntegerList(Q);
        for (int i = 0; i < Q; i++) {
            if (X[i] % rate != 0) {
                //add
                int xor = tree.xor(X[i], 0, depth);
                if ((used[0] ^ used[1]) < xor) {
                    used[0] = X[i];
                    used[1] = X[i] ^ xor;
                }
                history[historyLength++] = X[i];
                tree.modify(X[i], depth, 1);
            } else {
                if (historyLength > 0) {
                    int index = X[i] % historyLength;

                    if (history[index] != -1) {
                        //remove
                        tree.modify(history[index], depth, -1);
                        history[index] = -1;
                        //reset
                        if (!tree.exist(used[0], depth) || !tree.exist(used[1], depth)) {
                            list.clear();
                            tree.collect(list, 0, depth);
                            used[0] = used[1] = 0;
                            for (int j = 0; j < list.size(); j++) {
                                int val = list.get(j);
                                int xor = tree.xor(val, 0, depth);
                                if ((used[0] ^ used[1]) < xor) {
                                    used[0] = val;
                                    used[1] = val ^ xor;
                                }
                            }
                        }
                    }
                }
            }

            Y[i] = used[0] ^ used[1];
        }

        int z = Y[0];
        for(int i = 1; i < Q; i++){
            z = (int) (((long)z * base + Y[i]) % (int) (1e9 + 7));
        }

        return z;
    }
}

class BNode {
    BNode[] next = new BNode[2];
    int size;

    public BNode get(int i) {
        if (next[i] == null) {
            next[i] = new BNode();
        }
        return next[i];
    }

    public void collect(IntegerList list, int val, int depth) {
        if (size == 0) {
            return;
        }
        if (depth < 0) {
            list.add(val);
            return;
        }
        for (int i = 0; i < 2; i++) {
            get(i).collect(list, Bits.set(val, depth, i == 1), depth - 1);
        }
    }

    public void modify(int val, int depth, int x) {
        size += x;
        if (depth < 0) {
            return;
        }
        get(Bits.get(val, depth)).modify(val, depth - 1, x);
    }

    public boolean exist(int val, int depth) {
        if (depth < 0) {
            return size > 0;
        }
        return get(Bits.get(val, depth)).exist(val, depth - 1);
    }

    public int xor(int val, int xor, int depth) {
        if (depth < 0) {
            return xor;
        }
        int bit = Bits.get(val, depth);
        for (int i = 1; i >= 0; i--) {
            if (get(bit ^ i).size > 0) {
                return get(bit ^ i).xor(val, Bits.set(xor, depth, i == 1), depth - 1);
            }
        }
        return 0;
    }
}