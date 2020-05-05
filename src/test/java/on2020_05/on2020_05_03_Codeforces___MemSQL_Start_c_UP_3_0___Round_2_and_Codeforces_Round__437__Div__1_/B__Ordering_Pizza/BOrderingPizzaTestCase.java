package on2020_05.on2020_05_03_Codeforces___MemSQL_Start_c_UP_3_0___Round_2_and_Codeforces_Round__437__Div__1_.B__Ordering_Pizza;



import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.LongPreSum;
import template.rand.RandomWrapper;

public class BOrderingPizzaTestCase {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 3);
        int s = random.nextInt(1, 10);
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item();
            items[i].s = random.nextInt(1, 10);
            items[i].a = random.nextInt(1, 10);
            items[i].b = random.nextInt(1, 10);
        }

        long ans = solve(items, s);

        StringBuilder in = new StringBuilder();
        printLine(in, n, s);
        for (int i = 0; i < n; i++) {
            printLine(in, items[i].s, items[i].a, items[i].b);
        }

        return new Test(in.toString(), "" + ans);
    }

    public long solve(Item[] items, int s) {
        long sum = 0;
        long sumB = 0;
        List<Integer> sorted = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[i].s; j++) {
                sorted.add(items[i].a - items[i].b);
                sumB += items[i].b;
            }
            sum += items[i].s;
        }
        while (sorted.size() % s != 0) {
            sorted.add(0);
        }
        sorted.sort((a, b) -> -a.compareTo(b));
        long req = (sum + s - 1) / s;

        LongPreSum lps = new LongPreSum(i -> sorted.get(i), sorted.size());
        long ans = 0;
        for (int i = 0; i <= req; i++) {
            ans = Math.max(ans, sumB + lps.prefix(i * s - 1));
        }

        return ans;
    }

    static class Item {
        int s;
        int a;
        int b;
    }
}
