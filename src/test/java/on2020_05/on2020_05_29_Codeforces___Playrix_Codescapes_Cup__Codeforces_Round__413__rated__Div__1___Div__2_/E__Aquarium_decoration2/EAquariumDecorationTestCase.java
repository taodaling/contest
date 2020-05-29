package on2020_05.on2020_05_29_Codeforces___Playrix_Codescapes_Cup__Codeforces_Round__413__rated__Div__1___Div__2_.E__Aquarium_decoration2;





import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.rand.RandomWrapper;

public class EAquariumDecorationTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 0; i++) {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 20);
        int m = random.nextInt(1, n);
        int k = random.nextInt(1, m);
        int[] c = new int[n];
        int[] a = new int[random.nextInt(0, n)];
        int[] b = new int[random.nextInt(0, n)];
        for (int i = 0; i < n; i++) {
            c[i] = random.nextInt(1, (int) 1e9);
        }
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextInt(1, n);
        }
        for (int i = 0; i < b.length; i++) {
            b[i] = random.nextInt(1, n);
        }

        long ans = solve(n, m, k, c, a, b);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m, k);
        for (int i = 0; i < n; i++) {
            in.append(c[i]).append(' ');
        }
        printLine(in);
        printLine(in, a.length);
        for(int x : a){
            in.append(x).append(' ');
        }
        printLine(in);
        printLine(in, b.length);
        for(int x : b){
            in.append(x).append(' ');
        }

        return new Test(in.toString(), "" + ans);
    }

    public long solve(int n, int m, int k, int[] c, int[] a, int[] b) {
        int[] ta = new int[n];
        int[] tb = new int[n];
        for (int x : a) {
            ta[x - 1] = 1;
        }
        for (int x : b) {
            tb[x - 1] = 1;
        }

        long ans = Long.MAX_VALUE;
        for (int i = 0; i < (1 << n); i++) {
            if (Integer.bitCount(i) != m) {
                continue;
            }
            int aCnt = 0;
            long sum = 0;
            int bCnt = 0;
            for (int j = 0; j < n; j++) {
                if (Bits.bitAt(i, j) == 0) {
                    continue;
                }
                aCnt += ta[j];
                bCnt += tb[j];
                sum += c[j];
            }

            if (aCnt >= k && bCnt >= k) {
                ans = Math.min(sum, ans);
            }
        }

        return ans == Long.MAX_VALUE ? -1 : ans;
    }
}
