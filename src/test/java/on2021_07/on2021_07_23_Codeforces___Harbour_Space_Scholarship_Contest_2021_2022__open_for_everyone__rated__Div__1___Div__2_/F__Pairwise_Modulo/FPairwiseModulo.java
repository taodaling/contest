package on2021_07.on2021_07_23_Codeforces___Harbour_Space_Scholarship_Contest_2021_2022__open_for_everyone__rated__Div__1___Div__2_.F__Pairwise_Modulo;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.LongBIT;
import template.utils.Debug;

public class FPairwiseModulo {
    Debug debug = new Debug(false);

    int L = debug.enable() ? (int) 1e1 : (int) 3e5;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        IntegerBIT fCnt = new IntegerBIT(L);
//        IntegerBIT gCnt = new IntegerBIT(L);
        LongBIT fSum = new LongBIT(L);
        LongBIT gSum = new LongBIT(L);
        long prev = 0;
        for (int i = 0; i < n; i++) {
            int x = a[i];
            long part1 = 0;
            long part2 = 0;
            part1 += fSum.query(L);
            for (int j = 0; j <= L; j += x) {
                int l = j;
                int r = j + x - 1;
                part1 -= (long) fCnt.query(l, r) * j;
            }
            part2 = (long) i * x + gSum.query(x);

            //update
            fCnt.update(x, 1);
            fSum.update(x, x);
            for (int j = x; j <= L; j += x) {
                gSum.update(j, -x);
            }

            debug.debug("part1", part1);
            debug.debug("part2", part2);
            prev += part1 + part2;
            out.println(prev);
        }
    }
}

