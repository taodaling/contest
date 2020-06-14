package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;

public class CLamps {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n];
        in.populate(a);


        int[] cnts = new int[n + 1];
        for (int i = 0; i < k; i++) {
            Arrays.fill(cnts, 0);
            for (int j = 0; j < n; j++) {
                int l = Math.max(0, j - a[j]);
                int r = Math.min(n - 1, j + a[j]);
                cnts[l]++;
                cnts[r + 1]--;
            }


            boolean exist = false;
            for (int j = 0; j < n; j++) {
                if (j > 0) {
                    cnts[j] += cnts[j - 1];
                }
                a[j] = cnts[j];
                if (a[j] < n) {
                    exist = true;
                }
            }

            if (!exist) {
                break;
            }

            //debug.debug("", Arrays.toString(a));
        }

        for (int i = 0; i < n; i++) {
            out.append(a[i]).append(' ');
        }
    }
}
