package template.string;

public class ACAutomaton {
    public int[][] next;
    public int[] fails;
    private int minChar;
    private int maxChar;
    private int allocIndicator;
    private int range;

    public int size() {
        return allocIndicator;
    }

    public void init() {
        allocIndicator = 1;
    }

    private int alloc() {
        return allocIndicator++;
    }

    public ACAutomaton(int minChar, int maxChar, int cap) {
        this.minChar = minChar;
        this.maxChar = maxChar;
        range = maxChar - minChar + 1;
        next = new int[range][cap + 1];
        fails = new int[cap + 1];
        init();
    }

    public void prepareBuild() {
        buildLast = 0;
    }

    public void prepareMatch() {
        matchLast = 0;
    }

    public int buildLast;
    public int matchLast;

    public void build(int c) {
        c -= minChar;
        if (next[c][buildLast] == 0) {
            next[c][buildLast] = alloc();
        }
        buildLast = next[c][buildLast];
    }

    public void match(int c) {
        c -= minChar;
        matchLast = next[c][matchLast];
    }

    public int[] endBuild() {
        int[] dq = new int[allocIndicator];
        int dqTail = 1;
        int dqHead = 1;
        fails[0] = -1;
        for (int i = 0; i < range; i++) {
            if (next[i][0] != 0) {
                dq[dqTail++] = next[i][0];
                fails[next[i][0]] = 0;
            }
        }

        while (dqHead < dqTail) {
            int head = dq[dqHead++];
            for (int i = 0; i < range; i++) {
                if (next[i][head] != 0) {
                    dq[dqTail++] = next[i][head];
                    int fail = visit(fails[head], i);
                    if (fail == -1) {
                        fail = 0;
                    } else {
                        fail = next[i][fail];
                    }
                    fails[next[i][head]] = fail;
                }
            }
        }

        dqTail = 1;
        dqHead = 1;
        for (int i = 0; i < range; i++) {
            if (next[i][0] != 0) {
                dq[dqTail++] = next[i][0];
            }
        }
        while (dqHead < dqTail) {
            int head = dq[dqHead++];
            for (int i = 0; i < range; i++) {
                if (next[i][head] != 0) {
                    dq[dqTail++] = next[i][head];
                } else {
                    next[i][head] = next[i][fails[head]];
                }
            }
        }

        assert dqTail == allocIndicator;
        return dq;
    }

    public int visit(int trace, int index) {
        while (trace != -1 && next[index][trace] == 0) {
            trace = fails[trace];
        }
        return trace;
    }
}
