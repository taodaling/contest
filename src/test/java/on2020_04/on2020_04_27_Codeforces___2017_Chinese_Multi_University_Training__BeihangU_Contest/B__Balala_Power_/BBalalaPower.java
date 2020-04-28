package on2020_04.on2020_04_27_Codeforces___2017_Chinese_Multi_University_Training__BeihangU_Contest.B__Balala_Power_;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.utils.CompareUtils;
import template.utils.SequenceUtils;

public class BBalalaPower {
    Modular mod = new Modular(1e9 + 7);
    int charset = 'z' - 'a' + 1;
    int limit = 100000;

    int[] s = new int[limit];
    int[][] ps = new int[charset][limit];
    int[] len = new int[charset];
    boolean[] allowZero = new boolean[charset];

    private int normalize(int[] x) {
        int overflow = 0;
        for (int i = 0; i + 1 < x.length; i++) {
            overflow += x[i];
            x[i] = overflow % charset;
            overflow /= charset;
        }
        x[x.length - 1] += overflow;
        int ans = x.length - 1;
        while (ans > 0 && x[ans] == 0) {
            ans--;
        }
        return ans;
    }

    public int compare(int a, int b) {
        if (len[a] != len[b]) {
            return Integer.compare(len[a], len[b]);
        }
        for (int i = len[a]; i >= 0; i--) {
            if (ps[a][i] != ps[b][i]) {
                return Integer.compare(ps[a][i], ps[b][i]);
            }
        }
        return 0;
    }

    int valueOf(int[] x) {
        int ans = 0;
        for (int i = x.length - 1; i >= 0; i--) {
            ans = mod.valueOf((long) ans * charset + x[i]);
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        if(!in.hasMore()){
            throw new UnknownError();
        }

        int n = in.readInt();
        SequenceUtils.deepFill(ps, 0);
        SequenceUtils.deepFill(allowZero, true);

        for (int i = 0; i < n; i++) {
            int m = in.readString(s, 0);
            for (int j = 0; j < m; j++) {
                s[j] -= 'a';
                ps[s[j]][m - j - 1]++;
            }
            allowZero[s[0]] = false;
        }
        for (int i = 0; i < charset; i++) {
            len[i] = normalize(ps[i]);
        }
        int[] indices = new int[charset];
        for (int i = 0; i < charset; i++) {
            indices[i] = i;
        }
        CompareUtils.quickSort(indices, this::compare, 0, indices.length);

        int sum = 0;
        boolean[] used = new boolean[charset];
        for (int i = 0; i < charset; i++) {
            int x = indices[i];
            int index = 0;
            if (!allowZero[x]) {
                index = 1;
            }
            while (used[index]) {
                index++;
            }
            used[index] = true;
            sum = mod.plus(sum, mod.mul(valueOf(ps[x]), index));
        }

        out.printf("Case #%d: %d", testNumber, sum).println();
    }
}
