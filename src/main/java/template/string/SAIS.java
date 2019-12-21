package template.string;


import template.primitve.generated.IntegerList;
import template.primitve.generated.MultiWayIntegerDeque;
import template.utils.CompareUtils;

/**
 * SAIS is an O(n) algorithm to get SA of an array
 */
public class SAIS {
    private int l;
    private int[] rank2Index;
    private int[] index2Rank;
    private int[] lcp;
    private int[] data;

    /**
     * Query the rank of s[i..]
     */
    public int queryRank(int i) {
        return index2Rank[i - l];
    }

    /**
     * Get the kth smallest suffix (Min rank denoted with 0)
     */
    public int queryKth(int k) {
        return rank2Index[k] + l;
    }

    /**
     * Get the longest common prefix between queryKth(i - 1) and queryKth(i)
     */
    public int longestCommonPrefixBetween(int i) {
        return lcp[i];
    }

    public SAIS(char[] array) {
        this(array, 0, array.length - 1);
    }

    public SAIS(int[] array) {
        this(array, 0, array.length - 1);
    }

    public SAIS(char[] array, int l, int r) {
        if (l > r) {
            throw new IllegalArgumentException();
        }
        this.l = l;
        int n = r - l + 1;
        data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = array[i + l];
        }
        process();
    }

    public SAIS(int[] array, int l, int r) {
        if (l > r) {
            throw new IllegalArgumentException();
        }
        int n = r - l + 1;
        data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = array[i + l];
        }
        process();
    }

    private void process() {
        int n = data.length;
        rank2Index = new int[n];
        index2Rank = new int[n];
        lcp = new int[n];
        for (int i = 0; i < n; i++) {
            rank2Index[i] = i;
        }
        int minElement = CompareUtils.minOf(data, 0, n - 1);
        CompareUtils.radixSort(rank2Index, 0, n - 1, x -> data[x] - minElement);
        int rank = 0;
        index2Rank[rank2Index[0]] = 0;
        for (int i = 1; i < data.length; i++) {
            if (data[rank2Index[i]] > data[rank2Index[i - 1]]) {
                rank++;
            }
            index2Rank[rank2Index[i]] = rank;
        }

        System.arraycopy(index2Rank, 0, rank2Index, 0, n);
        buildSA(rank2Index, new IntegerList(n), new IntegerList(n), new MultiWayIntegerDeque(n, n), index2Rank);
        for (int i = 0; i < n; i++) {
            rank2Index[index2Rank[i]] = i;
        }
        buildLcp();
    }

    private void buildLcp() {
        int n = lcp.length;
        for (int i = 0; i < n; i++) {
            int ri = index2Rank[i];
            if (ri == 0) {
                continue;
            }
            int j = rank2Index[ri - 1];
            int same = i == 0 ? 0 : Math.max(lcp[index2Rank[i - 1]] - 1, 0);
            while (j + same < n && i + same < n && data[j + same] == data[i + same]) {
                same++;
            }
            lcp[index2Rank[i]] = same;
        }
    }

    private static final int TYPE_MINUS = 0;
    private static final int TYPE_PLUS = 1;


    private static void buildSA(int[] data, IntegerList plus, IntegerList minus, MultiWayIntegerDeque deque, int[] output) {
        int n = data.length;
        if (n == 1) {
            output[0] = 0;
            return;
        }
        byte[] type = new byte[n];
        for (int i = n - 1; i >= 0; i--) {
            if (i == n - 1 || data[i] > data[i + 1] || data[i] == data[i + 1] && type[i + 1] == TYPE_MINUS) {
                type[i] = TYPE_MINUS;
            } else {
                type[i] = TYPE_PLUS;
            }
        }

        plus.clear();
        minus.clear();

        // find relation between star type
        deque.expandQueueNum(n);
        deque.clear();
        for (int i = 1; i < n; i++) {
            if (type[i - 1] == TYPE_MINUS && type[i] == TYPE_PLUS) {
                deque.addLast(data[i], i);
            }
        }
        for (int i = 0; i < n; i++) {
            while (!deque.isEmpty(i)) {
                plus.add(deque.removeFirst(i));
            }
        }

        if (!plus.isEmpty()) {
            induceSort(plus, minus, type, data, deque);

            minus.clear();
            minus.addAll(plus);
            plus.clear();
            for (int i = 0, until = minus.size(); i < until; i++) {
                int index = minus.get(i);
                if (index > 0 && type[index - 1] == TYPE_MINUS && type[index] == TYPE_PLUS) {
                    plus.add(index);
                }
            }

            minus.clear();
            for (int i = 1; i < n; i++) {
                if (type[i - 1] == TYPE_MINUS && type[i] == TYPE_PLUS) {
                    minus.add(i);
                }
            }

            int[] order2Index = minus.toArray();
            int[] alias = new int[order2Index.length];
            int[] index2Order = new int[n];
            for (int i = 0; i < order2Index.length; i++) {
                index2Order[order2Index[i]] = i;
            }
            // assign alias
            int r = 0;
            alias[index2Order[plus.get(0)]] = r;
            for (int i = 1; i < order2Index.length; i++) {
                int l1 = plus.get(i);
                int l2 = plus.get(i - 1);
                int r1 = index2Order[l1];
                if (r1 + 1 == order2Index.length) {
                    r1 = n - 1;
                } else {
                    r1 = order2Index[r1 + 1];
                }
                int r2 = index2Order[l2];
                if (r2 + 1 == order2Index.length) {
                    r2 = n - 1;
                } else {
                    r2 = order2Index[r2 + 1];
                }
                if (compareArray(data, l1, r1, data, l2, r2) != 0) {
                    r++;
                }
                alias[index2Order[plus.get(i)]] = r;
            }
            buildSA(alias, plus, minus, deque, output);
            int[] index2Rank = output;
            plus.clear();
            plus.expandWith(0, order2Index.length);
            for (int i = 0; i < order2Index.length; i++) {
                plus.set(index2Rank[i], order2Index[i]);
            }
        }

        // find relation between minus type
        induceSort(plus, minus, type, data, deque);
        plus.reverse(0, plus.size() - 1);
        minus.reverse(0, minus.size() - 1);

        // merge
        int[] index2Rank = output;
        int rank = 0;
        while (plus.size() + minus.size() > 0) {
            if (plus.isEmpty() || (!minus.isEmpty() && data[minus.tail()] <= data[plus.tail()])) {
                index2Rank[minus.pop()] = rank++;
            } else {
                index2Rank[plus.pop()] = rank++;
            }
        }

        return;
    }

    private static int compareArray(int[] a, int al, int ar, int[] b, int bl, int br) {
        for (int i = al, j = bl; i <= ar && j <= br; i++, j++) {
            if (a[i] != b[j]) {
                return a[i] - b[j];
            }
        }
        return -((ar - al) - (br - bl));
    }

    private static void induceSort(IntegerList plus, IntegerList minus, byte[] type, int[] data, MultiWayIntegerDeque deque) {
        int n = data.length;
        deque.expandQueueNum(n);
        minus.clear();
        plus.reverse(0, plus.size() - 1);

        // from star to minus
        deque.clear();
        deque.addFirst(data[n - 1], n - 1);
        for (int i = 0; i < n; i++) {
            while (!deque.isEmpty(i)) {
                int index = deque.removeFirst(i);
                if (type[index] == TYPE_MINUS) {
                    minus.add(index);
                }
                if (index > 0 && type[index - 1] == TYPE_MINUS) {
                    deque.addLast(data[index - 1], index - 1);
                }
            }
            while (!plus.isEmpty() && data[plus.tail()] == i) {
                int index = plus.pop();
                if (index > 0 && type[index - 1] == TYPE_MINUS) {
                    deque.addLast(data[index - 1], index - 1);
                }
            }
        }
        deque.clear();
        int rightScan = minus.size() - 1;
        // from minus to plus
        for (int i = n - 1; i >= 0; i--) {
            while (!deque.isEmpty(i)) {
                int index = deque.removeFirst(i);
                if (type[index] == TYPE_PLUS) {
                    plus.add(index);
                }
                if (index > 0 && type[index - 1] == TYPE_PLUS) {
                    deque.addLast(data[index - 1], index - 1);
                }
            }
            while (rightScan >= 0 && data[minus.get(rightScan)] == i) {
                int index = minus.get(rightScan--);
                if (index > 0 && type[index - 1] == TYPE_PLUS) {
                    deque.addLast(data[index - 1], index - 1);
                }
            }
        }
        plus.reverse(0, plus.size() - 1);
    }

}
