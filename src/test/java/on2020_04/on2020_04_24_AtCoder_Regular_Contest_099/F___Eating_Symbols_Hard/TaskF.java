package on2020_04.on2020_04_24_AtCoder_Regular_Contest_099.F___Eating_Symbols_Hard;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Modular;
import template.math.Power;
import template.primitve.generated.datastructure.LongHashMap;
import template.utils.Debug;

public class TaskF {
    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);
    int[] xs = new int[]{31, 61};
    int[] invXs = new int[]{power.inverseByFermat(xs[0]), power.inverseByFermat(xs[1])};

    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] s = new char[n];
        in.readString(s, 0);

        LinearFunction[][] pos = new LinearFunction[2][n];
        LinearFunction[][] neg = new LinearFunction[2][n];


        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                switch (s[i]) {
                    case '+':
                        pos[j][i] = new LinearFunction(1, 1);
                        neg[j][i] = new LinearFunction(1, -1);
                        break;
                    case '-':
                        pos[j][i] = new LinearFunction(1, -1);
                        neg[j][i] = new LinearFunction(1, 1);
                        break;
                    case '>':
                        pos[j][i] = new LinearFunction(xs[j], 0);
                        neg[j][i] = new LinearFunction(invXs[j], 0);
                        break;
                    case '<':
                        pos[j][i] = new LinearFunction(invXs[j], 0);
                        neg[j][i] = new LinearFunction(xs[j], 0);
                        break;
                }
            }
        }

        debug.debug("pos", pos);
        debug.debug("neg", neg);
        for (int i = n - 2; i >= 0; i--) {
            for (int j = 0; j < 2; j++) {
                pos[j][i] = LinearFunction.merge(pos[j][i], pos[j][i + 1], mod);
                neg[j][i] = LinearFunction.merge(neg[j][i + 1], neg[j][i], mod);
            }
        }


        debug.debug("pos", pos);
        debug.debug("neg", neg);

        int[] cs = new int[2];
        for (int i = 0; i < 2; i++) {
            cs[i] = pos[i][0].apply(0, mod);
        }

        debug.debug("cs", cs);

        long ans = 0;
        LongHashMap map = new LongHashMap(n, false);
        map.put(0, 1);
        for (int i = n - 1; i >= 0; i--) {
            int c0 = neg[0][i].apply(cs[0], mod);
            int c1 = neg[1][i].apply(cs[1], mod);
            long c = DigitUtils.asLong(c0, c1);

            long local = map.getOrDefault(c, 0);
            ans += local;
            if(local > 0){
                debug.debug("i", i);
                debug.debug("local", local);
            }

            int d0 = neg[0][i].apply(0, mod);
            int d1 = neg[1][i].apply(0, mod);
            long d = DigitUtils.asLong(d0, d1);
            map.put(d, map.getOrDefault(d, 0) + 1);
        }

        out.println(ans);
    }
}

/**
 * y=ax+b
 */
class LinearFunction {
    public final int a;
    public final int b;

    public static final LinearFunction IDENTITY = new LinearFunction(1, 0);

    public LinearFunction(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int apply(int x, Modular mod) {
        return mod.valueOf((long) a * x + b);
    }

    //a.a(b.a x + b.b) + a.b
    public static LinearFunction merge(LinearFunction a, LinearFunction b, Modular mod) {
        return new LinearFunction(mod.valueOf((long) a.a * b.a), mod.valueOf((long)a.a * b.b + a.b));
    }

    //ax+b=y => x=(y-b)/a
    public static LinearFunction inverse(LinearFunction func, Modular mod, Power pow) {
        int invA = pow.inverseExtGCD(func.a);
        return new LinearFunction(invA, mod.valueOf((long) -func.b * invA));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LinearFunction)) {
            return false;
        }
        LinearFunction function = (LinearFunction) obj;
        return function.a == a && function.b == b;
    }

    @Override
    public int hashCode() {
        return a * 31 + b;
    }

    @Override
    public String toString() {
        if (b >= 0) {
            return a + "x+" + b;
        }
        return a + "x" + b;
    }
}