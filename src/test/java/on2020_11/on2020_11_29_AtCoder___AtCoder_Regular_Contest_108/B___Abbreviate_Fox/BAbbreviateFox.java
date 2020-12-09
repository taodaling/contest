package on2020_11.on2020_11_29_AtCoder___AtCoder_Regular_Contest_108.B___Abbreviate_Fox;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CollectionUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BAbbreviateFox {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[n];
        in.rs(s);
        List<Character> dq = new ArrayList<>(n);
        for (char c : s) {
            dq.add(c);
            int m = dq.size();
            if (m >= 3 && dq.get(m - 1) == 'x' &&
                    dq.get(m - 2) == 'o' && dq.get(m - 3) == 'f') {
                for (int i = 0; i < 3; i++) {
                    CollectionUtils.pop(dq);
                }
            }
        }

        out.println(dq.size());
    }

}
