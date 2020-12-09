package template.string;

public class KMPAutomaton {
    int[] data;
    int[] fail;
    int buildLast;
    int matchLast = 0;
    int length;

    public KMPAutomaton(int cap) {
        data = new int[cap + 2];
        fail = new int[cap + 2];
        fail[0] = -1;
        buildLast = 0;
        length = cap;
    }

    /**
     * Get the border of s[0...i - 1]
     */
    public int maxBorder(int i) {
        return fail[i + 1];
    }

    public KMPAutomaton(KMPAutomaton automaton) {
        data = automaton.data;
        fail = automaton.fail;
        buildLast = automaton.buildLast;
        length = automaton.length;
    }

    public boolean isMatch() {
        return matchLast == length;
    }

    public int length() {
        return length;
    }

    public void beginMatch() {
        matchLast = 0;
    }

    public void match(int c) {
        matchLast = visit(c, matchLast) + 1;
    }

    public int visit(int c, int trace) {
        while (trace >= 0 && data[trace + 1] != c) {
            trace = fail[trace];
        }
        return trace;
    }

    public void build(int c) {
        buildLast++;
        fail[buildLast] = visit(c, fail[buildLast - 1]) + 1;
        data[buildLast] = c;
    }

}
