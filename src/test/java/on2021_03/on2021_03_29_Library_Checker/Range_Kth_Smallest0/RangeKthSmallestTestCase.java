package on2021_03.on2021_03_29_Library_Checker.Range_Kth_Smallest0;





import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;

public class RangeKthSmallestTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 1000; i++){
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
        int n = random.nextInt(1, 10);
        int m = random.nextInt(1, 10);
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = random.nextInt(1, 10);
        }
        int[][] qs = new int[m][];
        for(int i = 0; i < m; i++){
            int l = random.nextInt(0, n - 1);
            int r = random.nextInt(0, n - 1);
            if(r < l){
                int tmp = l;
                l = r;
                r = tmp;
            }
            int k = random.nextInt(1, r - l + 1);
            qs[i] = new int[]{l, r + 1, k - 1};
        }
        int[] ans = solve(n, m, a, qs);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        printLine(in, a);
        for(int[] q : qs){
            printLine(in, q);
        }
        StringBuilder out = new StringBuilder();
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int n, int m, int[] a, int[][] qs){
        IntegerArrayList ans = new IntegerArrayList();
        for(int[] q : qs){
            int l = q[0];
            int r = q[1] - 1;
            int k = q[2] + 1;
            int[] range = Arrays.copyOfRange(a, l, r + 1);
            Arrays.sort(range);
            ans.add(range[k - 1]);
        }
        return ans.toArray();
    }
}
