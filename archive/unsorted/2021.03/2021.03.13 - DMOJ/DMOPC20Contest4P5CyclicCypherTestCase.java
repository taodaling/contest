package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.rand.RandomWrapper;
import template.string.MaximumRepresentation;
import template.utils.SequenceUtils;

public class DMOPC20Contest4P5CyclicCypherTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
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
        int n = 20;//random.nextInt(12, 20);
        int k = 8;//random.nextInt(1, n);
        return new Test("" + n + " " + k, solve(n, k));
    }

    public static boolean check(int[] arr, int k) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            int prod = 1;
            for (int j = 0; j < k; j++) {
                prod *= arr[(i + j) % arr.length];
            }
            sum += prod;
        }
        return sum == 0;
    }

    public String solve(int n, int k) {
        Set<String> set = new TreeSet<>();
        for (int i = 0; i < 1 << n; i++) {
            int[] arr = new int[n];
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 1) {
                    arr[j] = 1;
                } else {
                    arr[j] = -1;
                }
            }
            if (check(arr, k)) {
                int first = MaximumRepresentation.minimumRepresentation(j -> arr[j], n);
                SequenceUtils.rotate(arr, 0, n - 1, (n - first) % n);
                StringBuilder in = new StringBuilder();
                printLine(in, arr);
                set.add(in.toString());
            }
        }
        if (set.size() > 0) {
            StringBuilder in = new StringBuilder();
            for (String x : set) {
                in.append(x);
            }
            return in.toString();
        }
        return "0";
    }
}
