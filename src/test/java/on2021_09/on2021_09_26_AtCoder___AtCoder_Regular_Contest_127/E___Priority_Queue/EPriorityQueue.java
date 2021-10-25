package on2021_09.on2021_09_26_AtCoder___AtCoder_Regular_Contest_127.E___Priority_Queue;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.Debug;

import java.util.Arrays;

public class EPriorityQueue {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int A = in.ri();
        int B = in.ri();
        int[] x = in.ri(A + B);
        if (B == 0) {
            out.println(1);
            return;
        }
        IntegerArrayList poses = new IntegerArrayList(B);
        for (int i = 0; i < x.length; i++) {
            if (x[i] == 2) {
                poses.add(i);
            }
        }
        poses.reverse();
        int sum = 0;
        int[] delProtection = new int[B + 1];
        int scan = A + B - 1;
        int scan2 = A + B - 1;
        for (int i = 0; i <= B; i++) {
            while (scan >= 0 && x[scan] != 2) {
                sum += x[scan];
                scan--;
            }
            delProtection[i] = sum;
            if(scan >= 0){
                x[scan] = 0;
                scan2 = Math.min(scan2, scan - 1);
                while(x[scan2] != 1){
                    scan2--;
                }
                x[scan2] = 0;
            }
        }

        debug.debug("delProtection", delProtection);
        long[] prev = new long[A + 1];
        long[] next = new long[A + 1];
        prev[0] = 1;
        for (int i = 1; i <= A; i++) {
            Arrays.fill(next, 0);
            for (int j = 0; j <= A; j++) {
                if (prev[j] == 0) {
                    continue;
                }
                for (int retain = 0; retain <= 1; retain++) {
                    int alive = j + retain;
                    if (alive > A) {
                        continue;
                    }
                    int dead = i - alive;
                    if (dead > B) {
                        continue;
                    }
                    if (delProtection[dead] >= alive) {
                        next[alive] += prev[j];
                    }
                }
            }

            for (int j = 0; j <= A; j++) {
                next[j] = DigitUtils.mod(next[j], mod);
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;


            debug.debug("i", i);
            debug.debug("prev", prev);
        }

        long ans = prev[A - B];
        out.println(ans);
    }

    Debug debug = new Debug(false);
}
