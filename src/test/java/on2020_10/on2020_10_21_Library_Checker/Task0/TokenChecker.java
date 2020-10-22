package on2020_10.on2020_10_21_Library_Checker.Task0;





import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.binary.Bits;
import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TokenChecker extends AbstractChecker {
    public TokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int n = stdin.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        stdin.populate(a);
        stdin.populate(b);
        int[] w = new int[n];
        stdin.populate(w);
        int[] size = solve(a, b, w);
        int sum = 0;
        if (actual.readInt() != size[0]) {
            return Verdict.WA;
        }
        Set<Integer> aColor = new HashSet<>();
        Set<Integer> bColor = new HashSet<>();
        for (int i = 0; i < size[0]; i++) {
            int x = actual.readInt();
            sum += w[x];
            aColor.add(a[x]);
            bColor.add(b[x]);
        }
        return aColor.size() == size[0] && bColor.size() == size[0] &&
                sum == size[1] ? Verdict.OK : Verdict.WA;
    }

    public int[] solve(int[] a, int[] b, int[] w) {
        int n = a.length;
        boolean[] exist1 = new boolean[n];
        boolean[] exist2 = new boolean[n];

        int ans = 0;
        int weight = 0;
        for (int i = 0; i < 1 << n; i++) {
            Arrays.fill(exist1, false);
            Arrays.fill(exist2, false);
            boolean valid = true;
            int sum = 0;
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 0) {
                    continue;
                }
                sum += w[j];
                if (exist1[a[j]] || exist2[b[j]]) {
                    valid = false;
                    break;
                }
                exist1[a[j]] = true;
                exist2[b[j]] = true;
            }
            if (valid) {
                if (ans < Integer.bitCount(i)) {
                    ans = Integer.bitCount(i);
                    weight = Integer.MIN_VALUE;
                }
                if (ans == Integer.bitCount(i)) {
                    weight = Math.max(weight, sum);
                }
            }
        }
        return new int[]{ans, weight};
    }
}
