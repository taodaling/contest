package on2021_05.on2021_05_19_DMOJ.DMOPC__20_Contest_5_P6___Top_Row0;





import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.IntMath;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongArrayList;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class DMOPC20Contest5P6TopRowTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
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
        char[] s = random.nextString('a', 'z', 10000).toCharArray();
        int n = s.length;
        int q = 500000;
        int[][] qs = new int[q][2];
        for (int i = 0; i < q; i++) {
            qs[i][0] = random.nextInt(1, n);
            qs[i][1] = random.nextInt(1, n);
            if (qs[i][0] > qs[i][1]) {
                SequenceUtils.swap(qs[i], 0, 1);
            }
        }

//        long[] res = solve(s, qs);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLineObj(in, new String(s));
        printLine(in, q);
        for(int[] query : qs){
            printLine(in, query);
        }
//        printLine(out, res);

        return new Test(in.toString(), null);
    }

    public long[] solve(char[] s, int[][] qs) {
        LongArrayList list = new LongArrayList();
        for (int[] lr : qs) {
            int l = lr[0] - 1;
            int r = lr[1] - 1;
            SuffixAutomaton sa = new SuffixAutomaton('a', 'z');
            for (char c : Arrays.copyOfRange(s, l, r + 1)) {
                sa.build(c);
            }
            long ans = 0;
            for (SuffixAutomaton.SANode node : sa.all) {
                ans += IntMath.sumOfInterval(node.minLength() + 1, node.maxlen + 1);
            }
            list.add(ans);
        }
        return list.toArray();
    }
}
