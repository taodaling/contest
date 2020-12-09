package on2020_11.on2020_11_30_Codeforces___Valentines_Day_Contest_2020.D__Equality;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.datastructure.SegTree;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class DEqualityTestCase {
    @TestCase
    public Collection<Test> createTests() {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public int[] gen(int n, int m){
        TreeSet<Integer> set = new TreeSet<>();
        while(set.size() < m){
            int x = random.nextInt(1, n);
            set.add(x);
        }
        return set.stream().mapToInt(Integer::intValue).toArray();
    }

    public Test create(int testNum) {
        int n = (int)1e9;
        int x = 300;
        int y = 300;
        int[] a = gen(n, x);
        int[] b = gen(n, y);

        StringBuilder in = new StringBuilder();
        printLine(in, n);
        printLine(in, x);
        for(int t : a){
            printLine(in, t, t);
        }
        printLine(in, y);
        for(int t : b){
            printLine(in, t, t);
        }
        return new Test(in.toString(), null);
    }

    public boolean contain(int[] interval, int x){
        return interval[0] <= x && x <= interval[1];
    }

    public int solve(int n, List<int[]> a, List<int[]> b) {
        int ans = 0;
        for (int i = 1; i <= n; i++) {
            boolean valid = true;
            for (int j = i; j <= n; j += 2 * i) {
                boolean find = false;
                for(int[] x : a){
                    if(contain(x, j)){
                        find = true;
                        break;
                    }
                }
                if(!find) {
                    valid = false;
                }
            }

            for (int j = i * 2; j <= n; j += 2 * i) {
                boolean find = false;
                for(int[] x : b){
                    if(contain(x, j)){
                        find = true;
                        break;
                    }
                }
                if(!find) {
                    valid = false;
                }
            }
            if(valid){
                ans++;
            }
        }
        return ans;
    }

    public int[] getInterval(int n) {
        int[] ans = new int[]{random.nextInt(1, n), random.nextInt(1, n)};
        if (ans[0] > ans[1]) {
            SequenceUtils.swap(ans, 0, 1);
        }
        return ans;
    }
}
