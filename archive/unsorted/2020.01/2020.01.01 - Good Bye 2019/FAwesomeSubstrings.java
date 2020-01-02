package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.ExpressionSolver;
import template.primitve.generated.IntegerHashMap;
import template.primitve.generated.IntegerList;
import template.primitve.generated.IntegerPreSum;
import template.primitve.generated.IntegerVersionArray;

public class FAwesomeSubstrings {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] s = new int[200000];
        int n = in.readString(s, 0);
        for (int i = 0; i < n; i++) {
            s[i] -= '0';
        }
        IntegerPreSum ps = new IntegerPreSum(s);
        IntegerList oneIndexList = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            if (s[i] == 1) {
                oneIndexList.add(i);
            }
        }

        int blockSize = 500;//(int) Math.ceil(Math.sqrt(n));
        int invBlockSize = DigitUtils.ceilDiv(n, blockSize);

//        //bf
//        long pseudo = 0;
//        for (int i = 0; i < n; i++) {
//            for (int j = i; j < n; j++) {
//                int sum = ps.prefix(j) - ps.prefix(i - 1);
//                if (sum >= 1 && (j - i + 1) % sum == 0) {
//                    pseudo++;
//                    System.err.printf("%d,%d,%d\n", i, j, sum);
//                }
//            }
//        }

        long ans = 0;
        int m = oneIndexList.size();
        int[] oneIndex = oneIndexList.toArray();
        for (int i = 0; i < m; i++) {
            for (int j = i; j < m && j - i + 1 < blockSize; j++) {
                int a = i == 0 ? oneIndex[0] : oneIndex[i] - oneIndex[i - 1] - 1;
                int b = oneIndex[j] - oneIndex[i] + 1;
                int c = j == m - 1 ? n - 1 - oneIndex[j] : oneIndex[j + 1] - oneIndex[j] - 1;
                int x = j - i + 1;
                ans += ExpressionSolver.sumOfImmutableDenominator(b + c, x, a + b + c);
                ans -= ExpressionSolver.sumOfImmutableDenominator(b - 1, x, a + b - 1);
            }
        }

        int zero = invBlockSize * m;
        IntegerVersionArray va = new IntegerVersionArray(zero + n + 1, 0);
        for (int k = 1; k <= invBlockSize; k++) {
            va.clear();
            for (int i = 0, j = 0; i < n; i++) {
                while (ps.prefix(i) - ps.prefix(j - 1) >= blockSize) {
                    int sl = ps.prefix(j - 1);

                    int v = (j - 1) - k * sl;
                    va.inc(v + zero);

                    j++;
                }
                int si = ps.prefix(i);
                int v = i - k * si;
                ans += va.get(v + zero);
            }
        }

        out.println(ans);
    }

}
