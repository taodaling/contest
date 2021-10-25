package on2021_09.on2021_09_28_CS_Academy___Virtual_Round__43.Coprime_Pairs;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;
import template.math.GCDs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CoprimePairs {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int kBackUp = k;
        EulerSieve sieve = new EulerSieve((int) 1e6 / 6);
        Deque<Integer> cand = new ArrayDeque(sieve.getPrimeCount());
        for (int i = 0; i < sieve.getPrimeCount(); i++) {
            cand.add(sieve.get(i));
        }
        cand.removeFirst();
        cand.removeFirst();
        List<Integer> ans = new ArrayList<>(n);

        while (k > 0 && k >= n - 1) {
            k -= n - 1;
            n--;
            ans.add(cand.removeFirst());
        }

        for (int i = 0; i < k; i++) {
            ans.add(3 * cand.removeFirst());
        }
        for (int i = k; i < n - 1; i++) {
            ans.add(6 * cand.removeFirst());
        }
        ans.add(2);

        assert check(ans.stream().mapToInt(Integer::intValue).toArray()) == kBackUp;
        for(int x : ans){
            out.println(x);
        }


    }

    public int check(int[] a) {
        int ans = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (GCDs.gcd(a[i], a[j]) == 1) {
                    ans++;
                }
            }
        }
        return ans;
    }

    public int choose2(int n) {
        return n * (n - 1) / 2;
    }
}
