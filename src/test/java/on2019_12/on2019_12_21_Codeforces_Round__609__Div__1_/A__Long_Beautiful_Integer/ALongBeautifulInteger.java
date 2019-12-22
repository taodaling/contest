package on2019_12.on2019_12_21_Codeforces_Round__609__Div__1_.A__Long_Beautiful_Integer;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ALongBeautifulInteger {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        k = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readChar() - '0';
        }

        int len = n;
        loop = new int[k];
        if (!build(a, 0)) {
            len = n + 1;
            Arrays.fill(loop, 0);
            loop[0] = 1;
        }

        out.println(len);
        for(int i = 0; i < len; i++){
            out.append(loop[i % k]);
        }
    }

    int n;
    int k;
    int[] loop;

    public boolean build(int[] a, int i) {
        if (a.length == i) {
            return true;
        }
        if (i < k) {
            loop[i] = a[i];
            if (build(a, i + 1)) {
                return true;
            }
            if (a[i] == 9) {
                return false;
            }
            loop[i] = a[i] + 1;
            for (int j = i + 1; j < k; j++) {
                loop[j] = 0;
            }
            return true;
        }
        return loop[i % k] > a[i] || (loop[i % k] == a[i] && build(a, i + 1));
    }
}
