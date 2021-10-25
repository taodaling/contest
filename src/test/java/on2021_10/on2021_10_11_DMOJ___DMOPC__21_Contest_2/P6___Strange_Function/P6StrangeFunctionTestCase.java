package on2021_10.on2021_10_11_DMOJ___DMOPC__21_Contest_2.P6___Strange_Function;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;
import template.rand.Randomized;
import template.string.SAIS;
import template.utils.SequenceUtils;
import template.utils.SortUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class P6StrangeFunctionTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
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
        int n = random.nextInt(1, 20);
        int[] p = IntStream.range(1, n + 1).toArray();
        Randomized.shuffle(p);
        int q = random.nextInt(1,  20);
        int[][] cmds = new int[q][];
        for (int i = 0; i < q; i++) {
            int c = random.nextInt(1, 3);
            if (c == 1) {
                cmds[i] = new int[]{c};
            } else {
                int x = random.nextInt(1, n);
                cmds[i] = new int[]{c, x};
            }
        }

        int[] ans = solve(p, cmds);

        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, q);
        printLine(in, p);
        for(int[] cmd : cmds){
            printLine(in, cmd);
        }
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public void apply(int[] p, int l, int r) {
        if(l >= r){
            return;
        }
        int min = SortUtils.argmin(p, l, r);
        if (l < min) {
            SequenceUtils.rotate(p, l, min, min);
        }
        apply(p, min + 1, r);
    }

    public int[] solve(int[] p, int[][] cmds) {
        p = p.clone();
        IntegerArrayList ans = new IntegerArrayList();
        for (int[] cmd : cmds) {
            if (cmd[0] == 1) {
                apply(p, 0, p.length - 1);
            } else if (cmd[0] == 2) {
                ans.add(p[cmd[1] - 1]);
            } else {
                int index = -1;
                for (int i = 0; i < p.length; i++) {
                    if (cmd[1] == p[i]) {
                        index = i + 1;
                    }
                }
                ans.add(index);
            }
        }
        return ans.toArray();
    }
}
