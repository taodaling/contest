package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;

public class PredecessorProblemTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 0; i++){
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, int...vals){
        for(int val : vals){
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long...vals){
        for(long val : vals){
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
    public Test create(int testNum){
        int n = (int)1e7;//random.nextInt(1, 1000);
        int q = (int)1e6;//random.nextInt(1, 1000);
        String s = random.nextString('0', '1', n);
        int[][] op = new int[q][];
        for(int i = 0; i < q; i++){
            op[i] =new int[]{
                    random.nextInt(0, 4), random.nextInt(0, n - 1)
            };
        }
        //int[] ans = solve(n, s, op);
        StringBuilder in = new StringBuilder();
        printLine(in, n, q);
        printLineObj(in, s);
        for(int[] o : op){
            printLine(in, o);
        }
        StringBuilder out = new StringBuilder();
        //printLineObj(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int n, String T, int[][] qs){
        TreeSet<Integer> set = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            if (T.charAt(i) == '1') {
                set.add(i);
            }
        }
        IntegerArrayList ans = new IntegerArrayList();
        for (int i = 0; i < qs.length; i++) {
            int c = qs[i][0];
            int k = qs[i][1];
            if (c == 0) {
                set.add(k);
            } else if (c == 1) {
                set.remove(k);
            } else if (c == 2) {
                if (!set.contains(k)) {
                    ans.add(0);
                } else {
                    ans.add(1);
                }
            } else if (c == 3) {
                Integer ceil = set.ceiling(k);
                if (ceil == null) {
                    ceil = -1;
                }
                ans.add(ceil);
            } else if (c == 4) {
                Integer floor = set.floor(k);
                if (floor == null) {
                    floor = -1;
                }
                ans.add(floor);
            }
        }

        return ans.toArray();
    }
}
