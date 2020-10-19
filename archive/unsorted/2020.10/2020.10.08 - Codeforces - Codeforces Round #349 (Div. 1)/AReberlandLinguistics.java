package contest;

import org.apache.commons.lang.math.IntRange;
import template.datastructure.BitSet;
import template.io.FastInput;
import template.utils.ArrayIndex;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

public class AReberlandLinguistics {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        String s = in.readString().substring(5);
        int n = s.length();
        if (n == 0) {
            out.println(0);
            return;
        }
        TreeSet<String> ans = new TreeSet<>();
        Set<BitSet>[] sets = IntStream.range(0, n).mapToObj(i -> new HashSet<>()).toArray(i -> new Set[i]);
        sets[n - 1].add(new BitSet(ai.totalSize()));
        for (int i = n - 1; i >= 0; i--) {
            if (sets[i].isEmpty()) {
                continue;
            }
            for (int j = i - 1; j >= i - 2 && j >= 0; j--) {
                String sub = s.substring(j, i + 1);
                ans.add(sub);
                int index = indexOf(sub);
                for (BitSet bs : sets[i]) {
                    if (bs.get(index)) {
                        continue;
                    }
                    BitSet clone = bs.clone();
                    clone.set(index);
                    if (j > 0) {
                        sets[j - 1].add(clone);
                    }
                }
            }
        }

        out.println(ans.size());
        for (String v : ans) {
            out.println(v);
        }
    }

    int charset = 'z' - 'a' + 1;
    ArrayIndex ai = new ArrayIndex(charset + 1, charset, charset);

    public int indexOf(String s) {
        int[] val = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            val[i] = s.charAt(i) - 'a';
        }
        if (val.length == 2) {
            return ai.indexOf(charset, val[0], val[1]);
        }
        return ai.indexOf(val[0], val[1], val[2]);
    }
}
