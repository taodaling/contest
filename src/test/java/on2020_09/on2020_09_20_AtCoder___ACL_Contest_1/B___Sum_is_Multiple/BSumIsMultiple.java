package on2020_09.on2020_09_20_AtCoder___ACL_Contest_1.B___Sum_is_Multiple;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;
import template.primitve.generated.datastructure.LongArrayList;

public class BSumIsMultiple {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        LongArrayList primeFactors = Factorization.factorizeNumberPrime(n);
        LongArrayList powFactors = new LongArrayList();
        for (long x : primeFactors.toArray()) {
            long pow = 1;
            while (n / pow % x == 0) {
                pow *= x;
            }
            powFactors.add(pow);
        }
        this.powFactors = powFactors.toArray();

        ans = (long)1e18;
        dfs(0, 1, 1);
        out.println(ans);
    }

    long ans = 0;
    long[] powFactors;

    LongExtGCDObject extgcd = new LongExtGCDObject();

    public void test(long a, long b) {
        if (a % 2 == 0 && b % 2 == 0) {
            return;
        }
        if (a == 1) {
            ans = Math.min(ans, b);
            return;
        }
        if(b == 1){
            ans = Math.min(ans, a - 1);
            return;
        }
        extgcd.extgcd(a, b);
        long x = extgcd.getX();
        x = DigitUtils.mod(x, b);
        ans = Math.min(ans, DigitUtils.mul(x, a, (long) 1e18) - 1);
    }

    public void dfs(int i, long a, long b) {
        if (i == powFactors.length) {
            test(a * 2, b);
            test(a, b * 2);
            return;
        }
        dfs(i + 1, a * powFactors[i], b);
        dfs(i + 1, a, b * powFactors[i]);
    }

}
