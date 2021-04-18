package on2021_04.on2021_04_09_Codeforces___Codeforces_Round__215__Div__1_.C__Sereja_and_the_Arrangement_of_Numbers;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Comparator;

public class CSerejaAndTheArrangementOfNumbers {
    public long choose2(long n) {
        return n * (n - 1) / 2;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int best = 1;
        for (int k = 1; k <= n && k <= m; k++) {
            long need;
            if (k % 2 == 1) {
                need = choose2(k) + 1;
            } else {
                need = choose2(k) + k / 2 - 1 + 1;
            }
            if (need <= n) {
                best = k;
            }
        }

        Choice[] choices = new Choice[m];
        for(int i = 0; i < m; i++){
            choices[i] = new Choice(in.ri(), in.ri());
        }
        Arrays.sort(choices, Comparator.comparingInt(x -> -x.w));
        long ans = 0;
        for(int i = 0; i < best; i++){
            ans += choices[i].w;
        }
        out.println(ans);
    }
}

class Choice {
    int q;
    int w;

    public Choice(int q, int w) {
        this.q = q;
        this.w = w;
    }
}
