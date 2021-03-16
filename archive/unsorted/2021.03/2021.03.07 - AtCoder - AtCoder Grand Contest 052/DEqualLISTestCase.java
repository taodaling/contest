package contest;

import java.util.*;
import java.util.stream.IntStream;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.algo.LIS;
import template.binary.Bits;
import template.math.Permutation;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;
import template.rand.Randomized;

public class DEqualLISTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, int... vals) {
        for (int val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long... vals) {
        for (long val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 6);
        int[] perm = IntStream.range(1, n + 1).toArray();
        Randomized.shuffle(perm);
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, n);
        printLine(in, perm);
        String ans = solve(perm);
        return new Test(in.toString(), ans);
    }


    public String solve(int[] perm) {
        perm = perm.clone();
        int n = perm.length;
        for (int i = 0; i < 1 << n; i++) {
            IntegerArrayList a = new IntegerArrayList(n);
            IntegerArrayList b = new IntegerArrayList(n);
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 0) {
                    a.add(perm[j]);
                } else {
                    b.add(perm[j]);
                }
            }
            if (LIS.<Integer>strictLISLength(j -> a.get(j), a.size(), Comparator.naturalOrder()) ==
                    LIS.<Integer>strictLISLength(j -> b.get(j), b.size(), Comparator.naturalOrder())) {
                return "YES";
            }
        }
        return "NO";
    }
}
