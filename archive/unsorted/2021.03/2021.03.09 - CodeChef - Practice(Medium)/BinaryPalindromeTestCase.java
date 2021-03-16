package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;
import template.string.Manacher;

public class BinaryPalindromeTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
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
        int n = random.nextInt(1, 10);
        int[][] op = new int[n][];
        int remain = 0;
        for (int i = 0; i < n; i++) {
            int t = random.nextInt(1, 2);
            if (remain == 0) {
                t = 1;
            }
            if (t == 2) {
                remain--;
                op[i] = new int[]{t};
            } else {
                remain++;
                op[i] = new int[]{t, random.nextInt(0, 1)};
            }
        }

        int[] ans = solve(op);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n);
        for(int[] x : op){
            printLine(in, x);
        }
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    private boolean palindrome(int[] data, int l, int r){
        while(l < r){
            if(data[l] != data[r]){
                return false;
            }
            l++;
            r--;
        }
        return true;
    }

    public int solve(int[] data) {
        int ans = 0;
        for(int i = 0; i < data.length; i++){
            for(int j = i; j < data.length; j++){
                if (j - i + 1 > ans && palindrome(data, i, j)) {
                    ans = Math.max(ans, j - i + 1);
                }
            }
        }
        return ans;
    }

    public int[] solve(int[][] ops) {
        IntegerArrayList list = new IntegerArrayList();
        IntegerArrayList ans = new IntegerArrayList();
        for (int[] x : ops) {
            if (x[0] == 2) {
                list.pop();
            } else {
                list.add(x[1]);
            }
            ans.add(solve(list.toArray()));
        }
        return ans.toArray();
    }
}
