package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerSparseTable;
import template.rand.RandomWrapper;
import template.rand.Randomized;

public class TaskTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            System.out.println("build  testcase " + i);
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 100);
        int[] perm = IntStream.range(0, n).toArray();
        Randomized.shuffle(perm);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for(int i = 0; i < n; i++){
            in.append(perm[i]).append(' ');
        }
        return new Test(in.toString(), "" + solve(perm));
    }

    public int solve(int[] perm) {
        int n = perm.length;
        IntegerSparseTable maxST = new IntegerSparseTable(i -> perm[i], n, Math::max);
        IntegerSparseTable minST = new IntegerSparseTable(i -> perm[i], n, Math::min);
        int ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (j - i == maxST.query(i, j) - minST.query(i, j)) {
                    ans++;
                }
            }
        }
        return ans;
    }
}
