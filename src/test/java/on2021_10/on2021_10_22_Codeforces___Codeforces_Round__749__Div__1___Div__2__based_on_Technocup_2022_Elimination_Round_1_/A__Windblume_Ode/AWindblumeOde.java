package on2021_10.on2021_10_22_Codeforces___Codeforces_Round__749__Div__1___Div__2__based_on_Technocup_2022_Elimination_Round_1_.A__Windblume_Ode;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;

import java.util.Arrays;

public class AWindblumeOde {
    EulerSieve sieve = new EulerSieve((int) 1e5);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        boolean[] added = new boolean[n];
        Arrays.fill(added, true);
        int sum = Arrays.stream(a).sum();
        if (sieve.isPrime(sum)) {
            for (int i = 0; i < n; i++) {
                if (sieve.isComp(sum - a[i])) {
                    added[i] = false;
                    break;
                }
            }
        }
        int cnt = 0;
        for (boolean x : added) {
            if(x){
                cnt++;
            }
        }
        out.println(cnt);
        for(int i = 0; i < n; i++){
            if(added[i]){
                out.append(i + 1).append(' ');
            }
        }
        out.println();
    }
}
