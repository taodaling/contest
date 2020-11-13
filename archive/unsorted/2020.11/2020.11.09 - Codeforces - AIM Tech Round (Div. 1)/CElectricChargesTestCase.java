package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.rand.RandomWrapper;

public class CElectricChargesTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 10000; i++){
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

    RandomWrapper random = new RandomWrapper(new Random(0));
    public Test create(int testNum){
        int n = random.nextInt(1, 5);
        int[][] pts = new int[n][2];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < 2; j++){
                pts[i][j] = random.nextInt(-5, 5);
            }
        }
        long ans = solve(pts);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for(int[] pt : pts){
            printLine(in, pt);
        }
        return new Test(in.toString(), "" + ans);
    }

    public long dist2(long a, long b){
        return a * a + b * b;
    }

    public long solve(int[][] pts){
        int n = pts.length;
        long inf = (long) 1e18;
        long ans = inf;
        for(int i = 0; i < 1 << n; i++){
            long lx = inf;
            long rx = -inf;
            long up = -inf;
            long bot = inf;
            for(int j = 0; j < n; j++){
                 if(Bits.get(i, j) == 1){
                     up = Math.max(pts[j][1], up);
                     bot = Math.min(pts[j][1], bot);
                 }else{
                     lx = Math.min(pts[j][0], lx);
                     rx = Math.max(pts[j][0], rx);
                 }
            }
            long local = 0;
            if(up >= bot){
                local = Math.max(local, dist2(up - bot, 0));
            }
            if(rx >= lx){
                local = Math.max(local, dist2(rx - lx, 0));
            }
            if(up >= bot && rx >= lx){
                local = Math.max(local, dist2(Math.max(Math.abs(up), Math.abs(bot)), Math.max(Math.abs(rx), Math.abs(lx))));
            }
            ans = Math.min(ans, local);
        }
        return ans;
    }
}
