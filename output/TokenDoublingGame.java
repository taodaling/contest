
public class TokenDoublingGame {
    public int expectation(int N) {
        LinearFunction[] dp = new LinearFunction[N * 2 + 1];
        Modular mod = new Modular(1e9 + 7);
        Power power = new Power(mod);
        CachedPow cp = new CachedPow(2, mod);
        int half = cp.inverse(1);
        dp[1] = LinearFunction.IDENTITY;
        dp[0] = LinearFunction.ZERO;
        for (int i = 2; i <= 2 * N; i++) {
            dp[i] = dp[i - 1];
            int prob = half;
            for (int j = 0; ; j++) {
                prob = mod.mul(prob, half);
                int step = 2 + j;
                if (i - 1 - ((1 << (j + 1)) - 1) <= 0) {
                    step = mod.subtract(step, 1);
                    prob = mod.mul(prob, 2);
                    //dp[0]
                    dp[i] = LinearFunction.subtract(dp[i], 0, mod.mul(step, prob), mod);
                    break;
                } else {
                    int k = i - 1 - ((1 << (j + 1)) - 1) + 1;
                    dp[i] = LinearFunction.subtract(dp[i],
                            LinearFunction.mul(
                                    LinearFunction.plus(dp[k], 0, step, mod),
                                    prob, mod),
                            mod);
                }
            }
            dp[i] = LinearFunction.mul(dp[i], 2, mod);
            dp[i] = LinearFunction.subtract(dp[i], 0, 1, mod);
        }

        //dp[2n] = 0
        LinearFunction lf = LinearFunction.inverse(dp[2 * N], mod, power);
        int x = lf.apply(0, mod);
        int ans = dp[N].apply(x, mod);
        // debug.debug("dp", dp);
        // debug.debug("x", x);
//        for(int i = 0; i <= 2 * N; i++){
//         //   debug.debug("i", i);
//           // debug.debug("dp[i]", dp[i].apply(x, mod));
//        }
        return ans;
    }

}

class CachedPow {
    private int[] first;
    private int[] second;
    private Modular mod;
    private Modular powMod;
    private static int step = 16;
    private static int limit = 1 << step;
    private static int mask = limit - 1;

    public CachedPow(int x, Modular mod) {
        this.mod = mod;
        this.powMod = mod.getModularForPowerComputation();
        first = new int[limit];
        second = new int[Integer.MAX_VALUE / limit + 1];
        first[0] = 1;
        for (int i = 1; i < first.length; i++) {
            first[i] = mod.mul(x, first[i - 1]);
        }
        second[0] = 1;
        int step = mod.mul(x, first[first.length - 1]);
        for (int i = 1; i < second.length; i++) {
            second[i] = mod.mul(second[i - 1], step);
        }
    }

    public int pow(int exp) {
        return mod.mul(first[exp & mask], second[exp >> step]);
    }

    public int inverse(int exp) {
        return pow(powMod.valueOf(-exp));
    }

}

interface InverseNumber {
}

class LinearFunction {
    public final int a;
    public final int b;
    public static final LinearFunction IDENTITY = new LinearFunction(1, 0);
    public static final LinearFunction ZERO = new LinearFunction(0, 0);

    public LinearFunction(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public int apply(int x, Modular mod) {
        return mod.valueOf((long) a * x + b);
    }

    public static LinearFunction plus(LinearFunction a, int ba, int bb, Modular mod) {
        return new LinearFunction(mod.plus(a.a, ba), mod.plus(a.b, bb));
    }

    public static LinearFunction subtract(LinearFunction a, LinearFunction b, Modular mod) {
        return new LinearFunction(mod.subtract(a.a, b.a), mod.subtract(a.b, b.b));
    }

    public static LinearFunction subtract(LinearFunction a, int ba, int bb, Modular mod) {
        return new LinearFunction(mod.subtract(a.a, ba), mod.subtract(a.b, bb));
    }

    public static LinearFunction mul(LinearFunction a, int b, Modular mod) {
        return new LinearFunction(mod.mul(a.a, b), mod.mul(a.b, b));
    }

    public static LinearFunction inverse(LinearFunction func, Modular mod, Power pow) {
        int invA = pow.inverseExtGCD(func.a);
        return new LinearFunction(invA, mod.valueOf((long) -func.b * invA));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof LinearFunction)) {
            return false;
        }
        LinearFunction function = (LinearFunction) obj;
        return function.a == a && function.b == b;
    }

    public int hashCode() {
        return a * 31 + b;
    }

    public String toString() {
        if (b >= 0) {
            return a + "x+" + b;
        }
        return a + "x" + b;
    }

}

class Modular {
    int m;

    public int getMod() {
        return m;
    }

    public Modular(int m) {
        this.m = m;
    }

    public Modular(long m) {
        this.m = (int) m;
        if (this.m != m) {
            throw new IllegalArgumentException();
        }
    }

    public Modular(double m) {
        this.m = (int) m;
        if (this.m != m) {
            throw new IllegalArgumentException();
        }
    }

    public int valueOf(int x) {
        x %= m;
        if (x < 0) {
            x += m;
        }
        return x;
    }

    public int valueOf(long x) {
        x %= m;
        if (x < 0) {
            x += m;
        }
        return (int) x;
    }

    public int mul(int x, int y) {
        return valueOf((long) x * y);
    }

    public int plus(int x, int y) {
        return valueOf(x + y);
    }

    public int subtract(int x, int y) {
        return valueOf(x - y);
    }

    public Modular getModularForPowerComputation() {
        return new Modular(m - 1);
    }

    public String toString() {
        return "mod " + m;
    }

}

class ExtGCD {
    private long x;
    private long y;
    private long g;

    public long getX() {
        return x;
    }

    public long extgcd(long a, long b) {
        if (a >= b) {
            g = extgcd0(a, b);
        } else {
            g = extgcd0(b, a);
            long tmp = x;
            x = y;
            y = tmp;
        }
        return g;
    }

    private long extgcd0(long a, long b) {
        if (b == 0) {
            x = 1;
            y = 0;
            return a;
        }
        long g = extgcd0(b, a % b);
        long n = x;
        long m = y;
        x = m;
        y = n - m * (a / b);
        return g;
    }

}

class Power implements InverseNumber {
    static ExtGCD extGCD = new ExtGCD();
    final Modular modular;

    public Power(Modular modular) {
        this.modular = modular;
    }

    public int inverseExtGCD(int x) {
        if (extGCD.extgcd(x, modular.getMod()) != 1) {
            throw new IllegalArgumentException();
        }
        return modular.valueOf(extGCD.getX());
    }

}
