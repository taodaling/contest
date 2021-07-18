package on2021_07.on2021_07_09_Library_Checker.BracketTreeTest;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;
import template.string.SAIS;
import template.utils.SequenceUtils;

public class BracketTreeTestTestCase {
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
        int n = random.nextInt(1, 100);
        int m = random.nextInt(1, 100);
        char[] s = new char[n];
        for (int i = 0; i < n; i++) {
            s[i] = random.range('(', ')');
        }
        int[][] qs = new int[m][2];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                qs[i][j] = random.nextInt(0, n - 1);
            }
            if (qs[i][0] > qs[i][1]) {
                SequenceUtils.swap(qs[i], 0, 1);
            }
        }
        int[][] ans = solve(s, qs);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, m);
        printLineObj(in, new String(s));
        for(int[] q : qs){
            printLine(in, q);
        }
        for(int[] res : ans){
            printLine(out, res);
        }
        return new Test(in.toString(), out.toString());
    }

    public boolean ok(char[] s, int l, int r) {
        int ps = 0;
        for (int i = l; i <= r; i++) {
            int val = s[i] == '(' ? 1 : -1;
            ps += val;
            if (ps < 0) {
                return false;
            }
        }
        return ps == 0;
    }

    public int[][] solve(char[] s, int[][] qs) {
        List<int[]> ans = new ArrayList<>();
        for (int[] q : qs) {
            int l = q[0];
            int r = q[1];
            int largest = 0;
            int L = -1;
            int R = -1;
            int minCover = Integer.MAX_VALUE;
            int way = 0;
            for (int i = 0; i < s.length; i++) {
                for (int j = i; j < s.length; j++) {
                    if(!ok(s, i, j)){
                        continue;
                    }
                    int len = j - i + 1;
                    if(i >= l && j <= r){
                        way++;
                        if(len >= largest){
                            largest = len;
                        }
                    }
                    if(i <= l && r <= j){
                        if(minCover > len){
                            minCover = len;
                            L = i;
                            R = j;
                        }
                    }
                }
            }
            IntegerArrayList res = new IntegerArrayList();
            res.add(largest);
            res.add(way);
            if(minCover == Integer.MAX_VALUE){
                res.add(-1);
            }else{
                res.add(L);
                res.add(R);
            }
            ans.add(res.toArray());
        }
        return ans.toArray(new int[0][]);
    }


}
