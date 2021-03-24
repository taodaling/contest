package on2021_03.on2021_03_24_Codeforces___Codeforces_Round__238__Div__1_.B__Toy_Sum;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class BToySum {
    int mirror(int x) {
        return (int) 1e6 - x - 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] used = new int[(int) 1e6];
        for (int i = 0; i < n; i++) {
            used[in.ri() - 1] = -1;
        }
        int need = 0;
        for (int i = 0; i < used.length; i++) {
            int mi = mirror(i);
            if (i >= mi) {
                continue;
            }
            if (used[i] == -1 && used[mi] == -1) {
                need++;
            } else if (used[i] == -1) {
                used[mi] = 1;
            } else if (used[mi] == -1) {
                used[i] = 1;
            }
        }
        for (int i = 0; i < used.length && need > 0; i++) {
            int mi = mirror(i);
            if (i >= mi) {
                continue;
            }
            if (used[i] == 0 && used[mi] == 0) {
                need--;
                used[i] = 1;
                used[mi] = 1;
            }
        }
        int cnt = (int) Arrays.stream(used).filter(x -> x == 1).count();
        out.println(cnt);
        for (int i = 0; i < used.length; i++) {
            if (used[i] == 1) {
                out.append(i + 1).append(' ');
            }
        }
    }
}
