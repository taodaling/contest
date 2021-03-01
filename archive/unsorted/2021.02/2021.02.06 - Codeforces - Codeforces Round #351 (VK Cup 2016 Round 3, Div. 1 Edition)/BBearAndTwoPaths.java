package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SequenceUtils;

public class BBearAndTwoPaths {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int a = in.ri();
        int b = in.ri();
        int c = in.ri();
        int d = in.ri();
        if (n <= 4 || k < n + 1) {
            out.println(-1);
            return;
        }
        int[] special = new int[n + 1];
        special[a] = special[b] = special[c] = special[d] = 1;
        IntegerArrayList list = new IntegerArrayList(n);
        list.add(a);
        list.add(c);
        for (int i = 1; i <= n; i++) {
            if (special[i] == 0) {
                list.add(i);
            }
        }
        list.add(d);
        list.add(b);
        IntegerArrayList second = list.clone();
        SequenceUtils.swap(second, 0, 1);
        SequenceUtils.swap(second, n - 2, n - 1);
        for(int x : list.toArray()){
            out.append(x).append(' ');
        }
        out.println();
        for(int x : second.toArray()){
            out.append(x).append(' ');
        }
    }
}
