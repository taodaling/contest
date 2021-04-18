package on2021_04.on2021_04_14_Codeforces___Codeforces_Round__206__Div__1_.C__Vasya_and_Beautiful_Arrays;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class CVasyaAndBeautifulArrays {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] a = in.ri(n);
        int min = Arrays.stream(a).min().orElse(-1);
        if (min <= k) {
            out.println(min);
            return;
        }
        int N = (int) 1e6;
        int[] delta = new int[N + 2];
        for (int x : a) {
            delta[x - k]++;
            delta[x + 1]--;
        }
        for (int i = 1; i <= N; i++) {
            delta[i] += delta[i - 1];
        }
        int best = k;
        for (int i = k + 1; i <= N; i++) {
            int sum = 0;
            for (int j = i; j <= N; j += i) {
                sum += delta[j];
            }
            if(sum == n){
                best = i;
            }
        }
        out.println(best);
    }
}
