package contest;

import sun.misc.GC;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.math.GCDs;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerPriorityQueue;
import template.primitve.generated.datastructure.LongList;

public class AOracAndLCM {
    public int log(long x, long y) {
        int ans = 0;
        while (y % x == 0) {
            y /= x;
            ans++;
        }
        return ans;
    }

    public long pow(long x, long n) {
        long ans = 1;
        for (int i = 0; i < n; i++) {
            ans *= x;
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        long val = (long) a[0] * a[1] / GCDs.gcd(a[0], a[1]);
        LongList prime = Factorization.factorizeNumberPrime(val);
        long ans = 1;
        IntegerPriorityQueue ipq = new IntegerPriorityQueue(3, (x, y) -> -Integer.compare(x, y));
        for (int i = 0; i < prime.size(); i++) {
            ipq.clear();
            long p = prime.get(i);
            for (int x : a) {
                ipq.add(log(p, x));
                if (ipq.size() > 2) {
                    ipq.pop();
                }
            }
            ans *= pow(p, ipq.pop());
        }

        out.println(ans);
    }
}
