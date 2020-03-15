package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class DStringEquivalence {

    public void collect(int i, int cnt, StringBuilder builder, List<String> ans) {
        if (i == 0) {
            ans.add(builder.toString());
            return;
        }
        char end = (char) ('a' + cnt);
        for (char j = 'a'; j < end; j++) {
            builder.append(j);
            collect(i - 1, cnt, builder, ans);
            builder.setLength(builder.length() - 1);
        }
        builder.append(end);
        collect(i - 1, cnt + 1, builder, ans);
        builder.setLength(builder.length() - 1);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        List<String> ans = new ArrayList<>();
        collect(n, 0, new StringBuilder(n), ans);
        for (String s : ans) {
            out.println(s);
        }
    }
}
