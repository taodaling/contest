package on2020_09.on2020_09_14_Codeforces___Codeforces_Round__359__Div__1_.A__Robbers__watch;



import template.io.FastInput;
import template.io.FastOutput;

public class ARobbersWatch {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();

        lenN = require(n - 1);
        lenM = require(m - 1);

        int ans = enumerate(new boolean[base], new int[lenN + lenM], 0);
        out.println(ans);
    }

    int lenN;
    int lenM;
    int n;
    int m;

    public int enumerate(boolean[] used, int[] seq, int pos) {
        if (pos == seq.length) {
            long valL = 0;
            for (int i = lenN - 1; i >= 0; i--) {
                valL = valL * base + seq[i];
            }
            long valR = 0;
            for (int i = lenN + lenM - 1; i >= lenN; i--) {
                valR = valR * base + seq[i];
            }
            if (valL < n && valR < m) {
                return 1;
            }
            return 0;
        }
        int ans = 0;
        for(int i = 0; i < used.length; i++){
            if(used[i]){
                continue;
            }
            used[i] = true;
            seq[pos] = i;
            ans += enumerate(used, seq, pos + 1);
            used[i] = false;
        }
        return ans;
    }

    public int require(int n) {
        return Math.max(1, require0(n));
    }


    int base = 7;

    public int require0(int n) {
        return n == 0 ? 0 : (require0(n / 7) + 1);
    }

}
