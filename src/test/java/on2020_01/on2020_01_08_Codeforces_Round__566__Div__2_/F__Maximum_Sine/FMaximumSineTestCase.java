package on2020_01.on2020_01_08_Codeforces_Round__566__Div__2_.F__Maximum_Sine;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class FMaximumSineTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int a = random.nextInt(1, 10);
        int b = random.nextInt(1, 10);
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        int p = 2;
        int q = 3;
        int ans = solve(a, b, p, q);
        StringBuilder in = new StringBuilder();
        in.append(1).append('\n');
        in.append(a).append(' ').append(b).append(' ')
                .append(p).append(' ').append(q).append('\n');

        return new Test(in.toString(), "" + ans);
    }

    public int solve(int a, int b, int p, int q) {
        int index = a;
        for (int i = a; i <= b; i++) {
            if (Math.abs(Math.sin((double) p / q * Math.PI * i))
                    > Math.abs(Math.sin((double) p / q * Math.PI * index)) + 1e-10) {
                index = i;
            }
        }
        return index;
    }
}
