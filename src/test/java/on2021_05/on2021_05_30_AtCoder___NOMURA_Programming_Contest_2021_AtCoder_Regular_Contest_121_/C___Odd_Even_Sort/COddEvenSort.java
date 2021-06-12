package on2021_05.on2021_05_30_AtCoder___NOMURA_Programming_Contest_2021_AtCoder_Regular_Contest_121_.C___Odd_Even_Sort;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class COddEvenSort {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        debug.debug("testNumber", testNumber);
        int n = in.ri();
        p = in.ri(n);
        inv = new int[n];
        seq = new IntegerArrayList(n * n);
        sign = 0;
        for (int i = 0; i < n; i++) {
            p[i]--;
            inv[p[i]] = i;
        }
        for (int i = 0; i < n; i++) {
            if (inv[i] == i) {
                continue;
            }
            if ((inv[i] - 1) % 2 != sign) {
                //shuffle any
                boolean ok = false;
                for (int j = i; j < n - 1; j++) {
                    if (j != inv[i] && j + 1 != inv[i] && j % 2 == sign) {
                        swap(j);
                        ok = true;
                        break;
                    }
                }
                if(!ok && inv[i] == n - 3){
                    swap(inv[i]);
                    swap(inv[i] - 2);
                    ok = true;
                }
                if (!ok && i == n - 3) {
                    swap(inv[i]);
                    swap(inv[i] - 2);
                    ok = true;
                }
                if (!ok && i == n - 2) {
                    swap(n - 3);
                    swap(n - 2);
                    swap(n - 3);
                    swap(n - 2);
                    swap(n - 3);
                    ok = true;
                    break;
                }
            }
            while (inv[i] != i) {
                swap(inv[i] - 1);
            }
        }

        out.println(seq.size());
        for (int p : seq.toArray()) {
            out.append(p + 1).append(' ');
        }
        out.println();

        assert check();
    }

    boolean check() {
        for (int i = 0; i < p.length; i++) {
            if (i != p[i]) {
                return false;
            }
        }
        return true;
    }

    IntegerArrayList seq;
    int[] inv;
    int[] p;
    int sign = 1;

    void swap(int i) {
        assert i % 2 == sign;
        sign ^= 1;
        if (p.length == 2) {
            sign = 0;
        }
        seq.add(i);
        SequenceUtils.swap(p, i, i + 1);
        inv[p[i]] = i;
        inv[p[i + 1]] = i + 1;
    }
}
