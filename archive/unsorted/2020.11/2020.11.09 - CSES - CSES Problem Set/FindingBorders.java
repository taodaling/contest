package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.string.KMPAutomaton;

public class FindingBorders {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e6];
        int n = in.readString(s, 0);
        KMPAutomaton kmp = new KMPAutomaton(n);
        for (int i = 0; i < n; i++) {
            kmp.build(s[i]);
        }
        int border = kmp.maxBorder(n - 1);
        IntegerArrayList list = new IntegerArrayList(n);
        while (border >= 1) {
            list.add(border);
            border = kmp.maxBorder(border - 1);
        }
        list.reverse();
        for (int x : list.toArray()) {
            out.append(x).append(' ');
        }
    }
}
