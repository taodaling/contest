package on2021_07.on2021_07_19_DMOJ___DMOPC__19_Contest_5.P4___Cafeteria_Confrontation_Conundrum;



import template.datastructure.MonotoneOrderBeta;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Comparator;

public class P4CafeteriaConfrontationConundrum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        long[] take = in.rl(n);
        int[] victim = new int[n];
        for (int i = 0; i < n; i++) {
            victim[i] = in.ri() - 1;
        }
        for (int i = 0; i < n; i++) {
            if (victim[i] >= 0) {
                take[i] += take[victim[i]];
            }
        }
        MonotoneOrderBeta<Long, Integer> mo = new MonotoneOrderBeta<Long, Integer>(Comparator.naturalOrder(), Comparator.naturalOrder(), true, false);
        for (int i = 0; i < n; i++) {
            mo.add(take[i], i);
        }
        for (int i = 0; i < q; i++) {
            long need = -in.rl() + in.rl();
            if (need <= 0) {
                out.println(-1);
                continue;
            }
            Integer atleast = mo.ceil(need);
            if (atleast == null) {
                out.println(-1);
                continue;
            }
            out.println(atleast + 1);
        }
    }
}
