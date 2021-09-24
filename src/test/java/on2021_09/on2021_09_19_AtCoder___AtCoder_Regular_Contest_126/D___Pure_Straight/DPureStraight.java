package on2021_09.on2021_09_19_AtCoder___AtCoder_Regular_Contest_126.D___Pure_Straight;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class DPureStraight {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        k = in.ri();
        int[] a = in.ri(n);
        for (int i = 0; i < n; i++) {
            a[i]--;
        }
        prev = new int[2][1 << k];
        next = new int[2][1 << k];
        int[] bc = new int[1 << k];
        for (int i = 0; i < 1 << k; i++) {
            bc[i] = Integer.bitCount(i);
        }

        int inf = (int) 1e9;
        SequenceUtils.deepFill(prev, inf);
        prev[0][0] = 0;
        int mask = (1 << k) - 1;
        for (int i = 0; i < n; i++) {
            int x = a[i];
            SequenceUtils.deepFill(next, inf);
            for (int j = 0; j < 2; j++) {
                for (int z = j; z < 2; z++) {
                    for (int t = 0; t < 1 << k; t++) {
                        //add
                        if (Bits.get(t, x) == 0) {
                            int nt = t | (1 << x);
                            int nc = prev[j][t];
                            nc += bc[(mask ^ nt) & ((1 << x) - 1)];
                            next[z][nt] = Math.min(next[z][nt], nc);
                        }
                        //not add
                        int nt = t;
                        int nc = prev[j][t];
                        if (j == 1) {
                            nc += bc[t ^ mask];
                        } else {
                            nc += bc[t];
                        }
                        next[z][nt] = Math.min(next[z][nt], nc);
                    }
                }
            }
            int[][] tmp = prev;
            prev = next;
            next = tmp;
        }
        int ans = prev[1][mask];
        out.println(ans);
    }

    public int largeMask(int t) {
        return -1 ^ ((1 << t + 1) - 1);
    }

    int[][] prev;
    int[][] next;
    int n;
    int k;

}
