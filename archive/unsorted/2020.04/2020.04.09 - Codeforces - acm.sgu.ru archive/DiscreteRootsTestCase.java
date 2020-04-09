package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.EulerSieve;
import template.math.Modular;
import template.math.Power;
import template.rand.RandomWrapper;

public class DiscreteRootsTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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

    EulerSieve sieve = new EulerSieve(100);
    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int p = sieve.get(random.nextInt(0, sieve.getPrimeCount() - 1));
        int a = random.nextInt(0, p - 1);
        int k = random.nextInt(0, 100000);

        StringBuilder in = new StringBuilder();
        printLine(in, p, k, a);
        StringBuilder out = new StringBuilder();
        List<Integer> ans = solve(p, a, k);
        printLine(out, ans.size());
        for(int x : ans){
            out.append(x).append(' ');
        }
        return new Test(in.toString(), out.toString());
    }

    public List<Integer> solve(int p, int a, int k){
        Modular mod = new Modular(p);
        Power power = new Power(mod);
        List<Integer> ans = new ArrayList<>();
        for(int i = 0; i < p; i++){
            if(power.pow(i, k) == a){
                ans.add(i);
            }
        }
        return ans;
    }
}
