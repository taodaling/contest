package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.PreXor;
import template.RandomWrapper;
import template.SequenceUtils;

public class XORPartitioningTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        tests.add(new Test("8\n" +
                "5 4 3 2 2 2 5 5 ", "12"));
        for (int i = 0; i < 100; i++) {
            tests.add(create());
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create() {
        int n = random.nextInt(1, 10);
        int[] val = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            val[i] = random.nextInt(1, 500);
        }
        int xor = 0;
        for(int i = 1; i < n; i++){
            xor ^= val[i];
        }
        val[n] = xor;

        int ans = count(new PreXor(val), 1, 1, -1);
        StringBuilder builder = new StringBuilder();
        builder.append(n).append('\n');
        for (int i = 1; i <= n; i++) {
            builder.append(val[i]).append(' ');
        }

        return new Test(builder.toString(), Integer.toString(ans));
    }


    public int count(PreXor xor, int l, int r, int val) {
        if (r == xor.length() - 1) {
            if (val == -1 || xor.intervalSum(l, r) == val) {
                System.out.println(xor.intervalSum(l, r));
                return 1;
            }
            return 0;
        }
        int ans = count(xor, l, r + 1, val);
        if (val == -1 || xor.intervalSum(l, r) == val) {
            ans += count(xor, r + 1, r + 1, (int) xor.intervalSum(l, r));
        }
        return ans;
    }
}
