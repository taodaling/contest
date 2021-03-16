package on2021_03.on2021_03_16_Luogu.P6164__________;



import template.io.FastInput;
import template.io.FastOutput;
import template.string.IntArrayIntSequenceAdapter;
import template.string.IntSequence;
import template.string.SuffixBalancedTree;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.Deque;

public class P6164 {
    SuffixBalancedTree tree = new SuffixBalancedTree();
    int[] s = new int[(int) 3e6 + 1];
    int mask = 0;

    private int read(FastInput in) {
        int n = in.rs(s);
//        StringBuilder a = new StringBuilder();
//        for(int i = 0; i < n; i++){
//            a.append((char)s[i]);
//        }
        if(!debug.enable() && decode) {
            int t = mask;
            for (int i = 0; i < n; i++) {
                t = (t * 131 + i) % n;
                SequenceUtils.swap(s, i, t);
            }
        }
//        StringBuilder b = new StringBuilder();
//        for(int i = 0; i < n; i++){
//            b.append((char)s[i]);
//        }
        SequenceUtils.reverse(s, 0, n - 1);
        return n;
    }

    public void insert(FastInput in) {
        int n = read(in);
        for (int i = n - 1; i >= 0; i--) {
            tree.addPrefix(s[i]);
        }
    }

    boolean decode = false;
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.ri();
        insert(in);
        decode = true;
        for (int i = 0; i < q; i++) {
           // debug.debug("tree", tree);
            String type = in.rs();
            if (type.equals("ADD")) {
                insert(in);
            } else if (type.equals("DEL")) {
                int k = in.ri();
                for (int j = 0; j < k; j++) {
                    tree.removePrefix();
                }
            } else {
                int n = read(in);
                s[n] = Integer.MAX_VALUE;
                IntSequence seq = new IntArrayIntSequenceAdapter(s, 0, n);
                int sum = tree.leq(seq);
                s[n - 1]--;
                sum -= tree.leq(seq);
                out.println(sum);
                mask ^= sum;
            }
        }
    }
}
