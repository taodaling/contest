package on2021_05.on2021_05_21_AtCoder___ZONe_Energy_Programming_Contest.F___Encounter_and_Farewell;



import template.binary.Log2;
import template.datastructure.IntLinearBasis;
import template.datastructure.LinearBasis;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

public class FEncounterAndFarewell {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        boolean[] a = new boolean[n];
        for (int i = 0; i < m; i++) {
            a[in.ri()] = true;
        }
        IntLinearBasis lb = new IntLinearBasis();
        int[] mask = new int[32];
        for (int i = 0; i < n; i++) {
            if (a[i]) {
                continue;
            }
            int t = lb.add(i);
            if (t >= 0) {
                mask[t] = i;
            }
        }
        if (lb.xorNumberCount() != n) {
            out.println(-1);
            return;
        }
        debug.debug("lb", lb);
        for (int i = 0; i < n; i++) {
            int xor = lb.representationOriginal(i);
            if (xor == 0) {
                continue;
            }
            int v = Log2.floorLog(xor);
            out.append(i).append(' ').append(i ^ mask[v]).println();
            debug.debug("i", i);
            debug.debug("xor", xor);
        }
    }

    Debug debug = new Debug(false);
}
