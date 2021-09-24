package on2021_08.on2021_08_11_CS_Academy___Virtual_Beta_Round__6.LIS_Generator;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class LISGenerator {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.ri();
        if (k == 1) {
            out.println(1);
            return;
        }
        List<Integer> ans = new ArrayList<>();
        int cur = 1;
        while (cur * 2 <= k) {
            int x = nextInc();
            ans.add(x);
            ans.add(x);
            cur *= 2;
        }
        k -= cur;
        int longest = ans.size() / 2;
        int build = 0;
        for (int i = 30; i >= 0; i--) {
            if (Bits.get(k, i) == 0) {
                continue;
            }
            int offset = i * 2;
            while (build + i < longest) {
                build++;
                int x = nextDec();
                ans.add(offset, x);
            }
        }
        for (int x : ans) {
            out.append(x).append(' ');
        }
    }

    int incAlloc = 1;
    int decAlloc = (int) 1e9;

    public int nextInc() {
        return incAlloc++;
    }

    public int nextDec() {
        return decAlloc--;
    }
}
