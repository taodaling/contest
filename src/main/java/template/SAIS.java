package template;

public class SAIS {
    private int l;
    private int r;
    private int charset;
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
        return rank2Index[k];
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

    public SAIS(char[] array, int l, int r){
        if (l > r) {
            throw new IllegalArgumentException();
        }
        int n = data.length;
        for(int i = 0; i < n; i++){
            data[i] = array[i + l];
        }
        process();
    }

    public SAIS(int[] array, int l, int r){
        if (l > r) {
            throw new IllegalArgumentException();
        }
        int n = data.length;
        for(int i = 0; i < n; i++){
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
        CompareUtils.radixSort(rank2Index, 0, n - 1, x -> data[x]);
        buildReverseIndex();

        buildSA();
        buildReverseIndex();
        buildLcp();
    }

    /**
     * Get longest common prefix between i-th and i - 1-th suffix
     */
    private void buildLcp() {
        int n = lcp.length;
        for (int i = 0; i < n; i++) {
            int ri = rank2Index[i];
            if (ri == 0) {
                continue;
            }
            int j = rank2Index[ri - 1];
            int same = i == 0 ? 0 : Math.max(lcp[index2Rank[i - 1]] - 1, 0);
            while (j + same < n && i + same < n && data[j + same] == data[i + same]) {
                same++;
            }
            lcp[i] = same;
        }
    }

    private void buildReverseIndex() {
        int n = data.length;
        for (int i = 0; i < n; i++) {
            index2Rank[rank2Index[i]] = i;
        }
    }

    private static final int TYPE_MINUS = 0;
    private static final int TYPE_PLUS = 1;


    private void buildSA() {
        int n = data.length;
        IntList plus = new IntList(n);
        IntList minus = new IntList(n);
        MultiWayIntDeque deque = new MultiWayIntDeque(Math.max(charset, n), n);
        buildSA();
    }

    private void buildSA(IntList plus, IntList minus, MultiWayIntDeque deque, int[] data, int[] rank2Index) {
        int n = data.length;
        byte[] type = new byte[data.length];
        for (int i = n - 1; i >= 0; i--) {
            if (i == n - 1 || data[i] > data[i + 1] || data[i] == data[i + 1] && type[i + 1] == TYPE_MINUS) {
                type[i] = TYPE_MINUS;
            } else {
                type[i] = TYPE_PLUS;
            }
        }

        // find relation between star type
        for (int i = 1; i < n; i++) {
            if (type[i - 1] == TYPE_MINUS && type[i] == TYPE_PLUS) {
                deque.addLast(data[i], i);
            }
        }
        for (int i = 0; i < charset; i++) {
            while (!deque.isEmpty(i)) {
                plus.add(deque.removeFirst(i));
            }
        }
        induceSort(plus, minus, deque, type, data);

        minus.clear();
        minus.addAll(plus);
        plus.clear();
        for (int i = 0, until = minus.size(); i < until; i++) {
            int index = minus.get(i);
            if (index > 0 && type[index - 1] == TYPE_MINUS && type[i] == TYPE_PLUS) {
                plus.add(index);
            }
        }



        // find relation between minus type
        induceSort(plus, minus, deque, type, data);
        plus.reverse(0, plus.size() - 1);
        minus.reverse(0, minus.size() - 1);

        // merge
        int rank = 0;
        while (plus.size() + minus.size() > 0) {
            if (plus.isEmpty() || (!minus.isEmpty() && data[minus.tail()] <= data[plus.tail()])) {
                rank2Index[rank++] = minus.pop();
            } else {
                rank2Index[rank++] = plus.pop();
            }
        }
    }


    private void induceSort(IntList plus, IntList minus, MultiWayIntDeque deque, byte[] type, int[] data) {
        minus.clear();
        plus.reverse(0, plus.size() - 1);

        // from star to minus
        deque.clear();
        for (int i = 0; i < charset; i++) {
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
        for (int i = charset - 1; i >= 0; i--) {
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
                int index = plus.get(rightScan--);
                if (index > 0 && type[index - 1] == TYPE_PLUS) {
                    deque.addLast(data[index - 1], index - 1);
                }
            }
        }
        plus.reverse(0, plus.size() - 1);
    }

}
