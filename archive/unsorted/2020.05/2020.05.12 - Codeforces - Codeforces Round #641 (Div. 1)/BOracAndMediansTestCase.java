package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.rand.RandomWrapper;

public class BOracAndMediansTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 1; i <= 0; i++) {
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
        int n = random.nextInt(1, 100);
        int[] a = new int[n];
        int k = random.nextInt(1, 2);
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt(1, 2);
        }
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, n, k);
        for(int i = 0; i < n; i++){
            in.append(a[i]).append(' ');
        }
        return new Test(in.toString(), solve(a, k) ? "yes" : "no");
    }

    public boolean solve(int[] a, int k) {
        a = a.clone();
        int[] zero = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            if (a[i] == k) {
                zero[i] = 1;
            }
            a[i] = a[i] < k ? 1 : -1;
        }


        int[] finalA = a;
        IntegerPreSum aps = new IntegerPreSum(i -> finalA[i], a.length);
        IntegerPreSum bps = new IntegerPreSum(i -> zero[i], zero.length);

        if (bps.post(0) == a.length) {
            return true;
        }

        int n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (bps.intervalSum(i, j) > 0 && aps.intervalSum(i, j) < 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
