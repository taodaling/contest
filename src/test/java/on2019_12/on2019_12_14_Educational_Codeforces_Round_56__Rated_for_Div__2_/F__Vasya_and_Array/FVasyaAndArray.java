package on2019_12.on2019_12_14_Educational_Codeforces_Round_56__Rated_for_Div__2_.F__Vasya_and_Array;



import template.algo.PreSum;
import template.datastructure.IntVersionArray;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Modular;
import template.string.SAIS;

public class FVasyaAndArray {
    Modular mod = new Modular(998244353);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int len = in.readInt();

        int[] s = new int[n];
        for (int i = 0; i < n; i++) {
            s[i] = in.readInt();
        }

        if (len == 1) {
            out.println(0);
            return;
        }

        RotateVersionArray[] deques = new RotateVersionArray[k + 1];
        for (int i = 1; i <= k; i++) {
            deques[i] = new RotateVersionArray(len);
        }

        int[] sum = new int[k + 1];
        int[] oneVal = new int[k + 1];
        if (s[0] == -1) {
            for (int i = 1; i <= k; i++) {
                deques[i].set(1, 1);
                sum[i] = 1;
            }
        } else {
            deques[s[0]].set(1, 1);
            sum[s[0]] = 1;
        }


        for (int i = 1; i < n; i++) {
            int total = sumOf(sum, 1, k);

            if (s[i] == -1) {
                for (int j = 1; j <= k; j++) {
                    oneVal[j] = mod.subtract(total, sum[j]);
                }
                for (int j = 1; j <= k; j++) {
                    sum[j] = mod.subtract(sum[j], deques[j].get(len - 1));
                    deques[j].rotate();
                    deques[j].set(1, oneVal[j]);
                    sum[j] = mod.plus(sum[j], oneVal[j]);
                }
            } else {
                int j = s[i];
                oneVal[j] = mod.subtract(total, sum[j]);
                sum[j] = mod.subtract(sum[j], deques[j].get(len - 1));
                deques[j].rotate();
                deques[j].set(1, oneVal[j]);
                sum[j] = mod.plus(sum[j], oneVal[j]);

                for (int t = 1; t <= k; t++) {
                    if (t != j) {
                        deques[t].clear();
                        sum[t] = 0;
                    }
                }
            }
        }

        int ans = sumOf(sum, 1, k);
        out.println(ans);
    }

    public int sumOf(int[] sum, int l, int r) {
        long ans = 0;
        for (int i = l; i <= r; i++) {
            ans += sum[i];
        }
        return mod.valueOf(ans);
    }
}

class RotateVersionArray {
    private int offset;
    private IntVersionArray va;
    private int n;

    public RotateVersionArray(int n) {
        va = new IntVersionArray(n);
        this.n = n;
    }

    public void rotate() {
        offset--;
        if (offset < 0) {
            offset += n;
        }
    }

    public int get(int i) {
        return va.get((i + offset) % n);
    }

    public void clear() {
        va.clear();
    }

    public void set(int i, int val) {
        va.set((i + offset) % n, val);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < n; i++) {
            builder.append(get(i)).append(',');
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}