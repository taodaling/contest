package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.GCDs;
import template.math.MultiplicativeFunctionSieve;
import template.rand.RandomWrapper;

public class DPathsTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object... vals) {
        for (Object val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = testNum;
        long ans = solve(n);


        return new Test("" + n, "" + ans);
    }

    MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(100000, false, false, false);
    public long solve(int n) {
        long ans = 0;
        for (int i = 2; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if(GCDs.gcd(i, j) > 1){
                    ans += 1;
                }else if((long)sieve.smallestPrimeFactor[i] * sieve.smallestPrimeFactor[j] <= n){
                    ans += 2;
                }else if(sieve.smallestPrimeFactor[i] * 2 <= n && sieve.smallestPrimeFactor[j] * 2 <= n){
                    ans += 3;
                }
            }
        }

        return ans;
    }
}
