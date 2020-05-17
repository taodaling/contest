package on2020_05.on2020_05_18_Codeforces___Codeforces_Round__423__Div__1__rated__based_on_VK_Cup_Finals_.E__Rusty_String;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SequenceUtils;

import java.util.concurrent.ThreadPoolExecutor;

public class ERustyString {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        KMPAutomaton kmp = new KMPAutomaton(n);
        for (int i = 0; i < n; i++) {
            char c = in.readChar();
            kmp.build(c);
        }
        int last = n;
        IntegerList possible = new IntegerList(n);
        while (last > 0) {
            possible.add(last == n ? n : n - last);
            last = kmp.maxBorder(last - 1);
        }

        possible.sort();
        out.println(possible.size());
        for (int i = 0; i < possible.size(); i++) {
            out.append(possible.get(i)).append(' ');
        }
        out.println();
    }
}


class KMPAutomaton {
    char[] data;
    int[] fail;
    int buildLast;
    int matchLast = 0;
    int length;

    public KMPAutomaton(int cap) {
        data = new char[cap + 2];
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

    public void match(char c) {
        matchLast = visit(c, matchLast) + 1;
    }

    public boolean match(char a, char b) {
        return a == b || a == '?' || b == '?';
    }

    public int visit(char c, int trace) {
        while (trace >= 0 && !match(data[trace + 1], c)) {
            trace = fail[trace];
        }
        return trace;
    }

    public void build(char c) {
        buildLast++;
        fail[buildLast] = visit(c, fail[buildLast - 1]) + 1;
        data[buildLast] = c;
    }

}
