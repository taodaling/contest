package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class GorgeousSequenceTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 1000);
        int[] data = new int[n + 1];
        for(int i = 1; i <= n; i++){
            data[i] = random.nextInt(1, 1000);
        }
        Machine machine = new Machine(n, data);
        StringBuilder in = new StringBuilder();

        StringBuilder out = new StringBuilder();

        int m = random.nextInt(1, 1000);
        printLine(in, 1);
        printLine(in, n, m);
        for(int i = 1; i <= n; i++){
            in.append(data[i]).append(' ');
        }
        in.append('\n');
        for (int i = 0; i < m; i++) {
            int t = random.nextInt(0, 2);
            int l = random.nextInt(1, n);
            int r = random.nextInt(1, n);
            if(l > r){
                int tmp = l;
                l = r;
                r = tmp;
            }
            int val = random.nextInt(1, 1000);

            if(t == 0){
                printLine(in, t, l, r, val);
                machine.setMin(l, r, val);
            }else if(t == 1){
                printLine(in, t, l, r);
                printLine(out, machine.queryMax(l, r));
            }else{
                printLine(in, t, l, r);
                printLine(out, machine.querySum(l, r));
            }
        }

        return new Test(in.toString(), out.toString());
    }

    private void printLine(StringBuilder builder, Object...vals){
        for(Object val : vals){
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private static class Machine {
        long[] data;

        public Machine(int n, int[] val) {
            data = new long[n + 1];
            for (int i = 1; i <= n; i++) {
                data[i] = val[i];
            }
        }

        public void setMin(int l, int r, long x) {
            for (int i = l; i <= r; i++) {
                data[i] = Math.min(data[i], x);
            }
        }

        public long queryMax(int l, int r) {
            long max = data[l];
            for (int i = l; i <= r; i++) {
                max = Math.max(max, data[i]);
            }
            return max;
        }

        public long querySum(int l, int r) {
            long sum = 0;
            for (int i = l; i <= r; i++) {
                sum = sum + data[i];
            }
            return sum;
        }
    }
}
