package on2020_06.on2020_06_20_Codeforces___Codeforces_Global_Round_8.E__Ski_Accidents;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ESkiAccidents {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        List<Integer>[] prev = IntStream.range(0, n).mapToObj(i -> new ArrayList<Integer>()).toArray(i -> new List[n]);
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            prev[b].add(a);
        }

        int[] type = new int[n];
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (int p : prev[i]) {
                mask |= type[p];
            }
            if (Bits.get(mask, 1) == 1) {
                type[i] = 1 << 2;
                continue;
            }
            if (Bits.get(mask, 0) == 1) {
                type[i] = 1 << 1;
                continue;
            }
            type[i] = 1 << 0;
        }

        int[] ans = IntStream.range(0, n).filter(i -> type[i] == (1 << 2)).toArray();
        out.println(ans.length);
        for (int x : ans) {
            out.append(x + 1).append(' ');
        }
        out.println();
    }
}

