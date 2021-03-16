package contest;

import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.FastFourierTransform;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.IntegerIterator;

import java.util.Arrays;

public class AGoingHome {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int lim = (int) (2.5e6);
        int log = Log2.ceilLog(lim * 2 + 1);
        double[][] poly = new double[2][1 << log];
        int[] occur = new int[lim + 1];
        Arrays.fill(occur, -1);
        for (int i = 0; i < n; i++) {
            poly[0][a[i]]++;
            occur[a[i]] = i;
        }


        IntegerArrayList list = new IntegerArrayList(n);
        for (int i = 0; i <= lim; i++) {
            if (poly[0][i] > 1) {
                list.add(i);
            }
        }
        if (list.size() >= 2) {
            int x = list.get(0);
            int y = list.get(1);
            IntegerArrayList xList = new IntegerArrayList(n);
            IntegerArrayList yList = new IntegerArrayList(n);
            for (int i = 0; i < n; i++) {
                if (a[i] == x) {
                    xList.add(i);
                }
                if (a[i] == y) {
                    yList.add(i);
                }
            }
            out.println("YES");
            out.append(xList.get(0) + 1).append(' ').append(yList.get(0)).append(' ');
            out.append(xList.get(1) + 1).append(' ').append(yList.get(1)).append(' ');
            return;
        }
        for (int i = 0; i <= lim; i++) {
            poly[0][i] = Math.min(poly[0][i], 1);
        }
        FastFourierTransform.fft(poly, false);
        FastFourierTransform.dotMulFast(poly, poly);
        FastFourierTransform.fft(poly, true);
        for (int i = 0; i <= lim; i++) {
            if (occur[i] != -1) {
                poly[0][i + i]--;
            }
        }
        int findSum = -1;
        for (int i = 0; i <= 2 * lim; i++) {
            if (Math.round(poly[0][i]) >= 4) {
                findSum = i;
                break;
            }
        }
        if (findSum == -1) {
            out.println("NO");
            return;
        }
        out.println("YES");
        int findTime = 0;
        for (int i = 0; i <= lim && i <= findSum && findTime < 2; i++) {
            if (occur[i] != -1 && occur[findSum - i] != -1 && i != findSum - i) {
                out.append(occur[i] + 1).append(' ').append(occur[findSum - i] + 1).append(' ');
                occur[i] = -1;
                occur[findSum - i] = -1;
                findTime++;
            }
        }
        assert findTime == 2;
    }
}
