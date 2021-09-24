package on2021_08.on2021_08_23_CS_Academy___Virtual_Round__21.Prime_Distance;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.PollardRho;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PrimeDistance {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.ri();
        int b = in.ri();
        Map<Integer, Integer> fa = factorize(a);
        Map<Integer, Integer> fb = factorize(b);
        int total = 0;
        for(int v : fa.values()){
            total += v;
        }
        for(int v : fb.values()){
            total += v;
        }
        for(int k : fa.keySet()){
            int numA = fa.getOrDefault(k, 0);
            int numB = fb.getOrDefault(k, 0);
            total -= 2 * Math.min(numA, numB);
        }
        out.println(total);
    }

    public Map<Integer, Integer> factorize(int a){
        Set<Integer> primes = PollardRho.findAllFactors(a);
        Map<Integer, Integer> ans = new HashMap<>();
        for(int p : primes){
            int x = a;
            int cnt = 0;
            while(x % p == 0){
                x /= p;
                cnt++;
            }
            ans.put(p, cnt);
        }
        return ans;
    }
}
