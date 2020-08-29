package on2020_08.on2020_08_17_Codeforces___Codeforces_Global_Round_10.F__Omkar_and_Landslide;



import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class FOmkarAndLandslideTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
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
        int n = random.nextInt(1, 10);
        TreeSet<Integer> set = new TreeSet<>();
        while (set.size() < n) {
            set.add(random.nextInt(0, (int) 1000));
        }
        int[] data = set.stream().mapToInt(Integer::intValue).toArray();
        int[] ans = solve(data);

        return new Test(n + "\n" + toString(data), toString(ans));
    }

    public String toString(int[] data) {
        StringBuilder builder = new StringBuilder();
        for (int x : data) {
            builder.append(x).append(' ');
        }
        return builder.toString();
    }

    public int[] solve(int[] data) {
        int n = data.length;
        int[] last = data.clone();
        int[] next = new int[n];
        for (;;) {
            System.arraycopy(last, 0, next, 0, n);
            boolean occur = false;
            for (int j = 1; j < n; j++) {
                if (last[j] >= last[j - 1] + 2) {
                    next[j - 1]++;
                    next[j]--;
                    occur = true;
                }
            }
            int[] tmp = last;
            last = next;
            next = tmp;

            if (!occur) {
                break;
            }
        }

        return last;
    }
}
