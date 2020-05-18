package on2020_05.on2020_05_18_Codeforces___Codeforces_Round__423__Div__1__rated__based_on_VK_Cup_Finals_.E__Rusty_String1;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.polynomial.FastFourierTransform;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SequenceUtils;

public class ERustyString {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[n];
        in.readString(s, 0);

        int[] v = new int[n];
        int[] k = new int[n * 2];
        for (int i = 0; i < n; i++) {
            if (s[i] == 'V') {
                v[i] = 1;
            } else if (s[i] == 'K') {
                k[i + n] = 1;
            }
        }

        SequenceUtils.reverse(k);

        int proper = Log2.ceilLog(3 * n);
        double[][] vPoly = new double[2][1 << proper];
        double[][] kPoly = new double[2][1 << proper];
        for (int i = 0; i < n; i++) {
            vPoly[0][i] = v[i];
        }
        for (int i = 0; i < 2 * n; i++) {
            kPoly[0][i] = k[i];
        }

        FastFourierTransform.dft(vPoly, proper);
        FastFourierTransform.dft(kPoly, proper);
        FastFourierTransform.dotMul(vPoly, kPoly, kPoly, proper);
        FastFourierTransform.idft(kPoly, proper);

        SequenceUtils.reverse(kPoly[0], 0, 2 * n - 1);

        boolean[] exists = new boolean[n];
        for (int i = 0; i < 2 * n; i++) {
            if (DigitUtils.roundToInt(kPoly[0][i]) == 0) {
                continue;
            }
            int diff = i - n;
            exists[Math.abs(diff)] = true;
        }

        for (int i = 1; i < n; i++) {
            for (int j = i; j < n; j += i) {
                exists[i] = exists[i] || exists[j];
            }
        }

        IntegerList ans = new IntegerList(n);
        for(int i = 1; i < n; i++){
            if(!exists[i]){
                ans.add(i);
            }
        }
        ans.add(n);

        out.println(ans.size());
        for(int i = 0; i < ans.size(); i++){
            out.append(ans.get(i)).append(' ');
        }
        out.println();
    }
}
