package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.IntegerList;
import template.NumberTheoryTransform;

public class LUOGU4705 {
    Modular mod = new Modular(998244353);
    NumberTheoryTransform ntt = new NumberTheoryTransform(mod);
    Log2 log2 = new Log2();
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        int[] a = new int[n];
        int[] b = new int[m];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 0; i < m; i++) {
            b[i] = in.readInt();
        }

        int t = in.readInt();
        IntegerList A = generate(a, t);
        IntegerList B = generate(b, t);

        Factorial fact = new Factorial(t, mod);
        for(int i = 0; i <= t; i++){
            A.set(i, mod.mul(A.get(i), fact.invFact(i)));
            B.set(i, mod.mul(B.get(i), fact.invFact(i)));
        }

        int proper = log2.ceilLog(t + 1) + 1;
        A.expandWith(0, 1 << proper);
        B.expandWith(0, 1 << proper);
        ntt.dft(A.getData(), proper);
        ntt.dft(B.getData(), proper);
        ntt.dotMul(A.getData(), B.getData(), A.getData(), proper);
        ntt.idft(A.getData(), proper);

        int invnm = pow.inverse(mod.mul(n, m));
        for(int i = 1; i <= t; i++){
            int ans = A.get(i);
            ans = mod.mul(ans, fact.fact(i));
            ans = mod.mul(ans, invnm);
            out.println(ans);
        }
    }

    public IntegerList generate(int[] a, int m) {
        int n = a.length;
        IntegerList[] segs = new IntegerList[n];
        for (int i = 0; i < n; i++) {
            segs[i] = new IntegerList(2);
            segs[i].add(1);
            segs[i].add(mod.valueOf(-a[i]));
        }

        IntegerList f = NumberTheoryTransform.listBuffer.alloc();
        ntt.dacMul(segs, f);

        IntegerList g = new IntegerList(f.size());
        for (int i = 0; i < f.size(); i++) {
            g.add(mod.mul(f.get(i), n - i));
        }

        int proper = log2.ceilLog(m + 1) + 1;
        f.retain(m + 1);
        g.retain(m + 1);
        f.expandWith(0, 1 << proper);
        g.expandWith(0, 1 << proper);
        IntegerList inv = NumberTheoryTransform.listBuffer.alloc();
        inv.expandWith(0, 1 << proper);
        ntt.inverse(f.getData(), inv.getData(), proper - 1);
        ntt.dft(g.getData(), proper);
        ntt.dft(inv.getData(), proper);
        ntt.dotMul(g.getData(), inv.getData(), g.getData(), proper);
        ntt.idft(g.getData(), proper);
        g.retain(m + 1);
        NumberTheoryTransform.listBuffer.release(inv);
        NumberTheoryTransform.listBuffer.release(f);
        return g;
    }
}
