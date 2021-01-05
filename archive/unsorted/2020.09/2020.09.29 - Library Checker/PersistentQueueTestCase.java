package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class PersistentQueueTestCase {
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int q = testNum;
        int[][] qs = new int[q][];
        for (int i = 0; i < q; i++) {
            int prev = random.nextInt(-1, i - 1);
            int type = random.nextInt(0, 1);
            if (type == 0) {
                int x = random.nextInt(1, 100);
                qs[i] = new int[]{type, prev, x};
            } else {
                qs[i] = new int[]{type, prev};
            }
        }

        StringBuilder in = new StringBuilder();
        printLine(in, q);
        for(int i = 0; i < q; i++){
            for(int x : qs[i]){
                in.append(x).append(' ');
            }
            printLine(in);
        }

        String ans = solve(qs);
        return new Test(in.toString(), ans);
    }

    public String solve(int[][] qs){
        int q = qs.length;
        StringBuilder out = new StringBuilder();
        Deque<Integer>[] dqs = new Deque[q + 1];
        dqs[0] = new ArrayDeque<>();
        for(int i = 1; i <= q; i++){
            int[] query = qs[i - 1];
            dqs[i] = new ArrayDeque<>(dqs[query[1] + 1]);
            if(query[0] == 0){
                //add edge
                dqs[i].addLast(query[2]);
            }else{
                //remove edge
                int ans = dqs[i].isEmpty() ? -1 : dqs[i].removeFirst();
                printLine(out, "" + ans);
            }
        }
        return out.toString();
    }
}
