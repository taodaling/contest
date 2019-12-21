package contest;


import com.sun.org.apache.xpath.internal.operations.Mod;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;

public class FLunarNewYearAndARecursiveSequence {
    Modular mod = new Modular(998244353);
    Modular mod2 = new Modular(mod.getMod() - 1);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int[] bs = new int[k];
        for (int i = 0; i < k; i++) {
            bs[i] = in.readInt();
        }

        int n = in.readInt();
        int m = in.readInt();
        ModMatrix mat = new ModMatrix(k, k);
        for (int i = 0; i < k; i++) {
            mat.set(k - 1, i, bs[k - i - 1]);
        }
        for (int i = k - 2; i >= 0; i--) {
            mat.set(i, i + 1, 1);
        }

        ModMatrix tran = ModMatrix.pow(mat, n - 1, mod2);
        ModMatrix vec = new ModMatrix(k, 1);
        vec.set(k - 1, 0, 1);
        ModMatrix finalState = ModMatrix.mul(tran, vec, mod2);
        int b = finalState.get(0, 0);

        ModLog modLog = new ModLog(3, mod);
        int logm = modLog.log(m);
        ExtGCD extGCD = new ExtGCD();
        int g = (int) extGCD.extgcd(b, mod2.getMod());
        if (logm % g != 0) {
            out.println(-1);
            return;
        }

        int y = mod2.mul(logm / g, mod2.valueOf(extGCD.getX()));
        Power power = new Power(mod);
        int ans = power.pow(3, y);
        out.println(ans);
    }
}


