package contest;

import java.util.Arrays;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.IntList;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] coe = new int[n + 1];
        int[] p = new int[n + 1];
        for (int i = n; i >= 0; i--) {
            coe[i] = in.readInt();
        }
        EulerSieve es = new EulerSieve(n);
        IntList ans = new IntList();
        Gcd gcd = new Gcd();
        int num = 0;
        for (int i = 0; i <= n; i++) {
            num = gcd.gcd(num, Math.abs(coe[i]));
        }
        if (num > 0) {
            ans.addAll(new PollardRho().findAllFactors(num).keySet().stream().mapToInt(Integer::intValue)
                            .toArray());
        }


        for (int i = 0; i < es.getPrimeCount(); i++) {
            int prime = es.get(i);
            if(coe[0] % prime != 0){
                continue;
            }
            Arrays.fill(p, 0);
            for (int j = n; j >= 0; j--) {
                p[DigitUtils.mod(j, prime - 1)] += DigitUtils.mod(coe[j], prime);
            }
            boolean allZero = true;
            for (int j = 0; j <= n && allZero; j++) {
                allZero = allZero && DigitUtils.mod(p[j], prime) == 0;
            }
            if (allZero) {
                ans.add(prime);
            }
        }

        ans.unique();
        for(int i = 0; i < ans.size(); i++){
            out.println(ans.get(i));
        }
    }
}
