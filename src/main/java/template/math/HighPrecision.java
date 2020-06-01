package template.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class HighPrecision {
    public final MathContext MATH_CONTEXT;
    public final BigDecimal BOT;
    public final BigDecimal TOP;

    public HighPrecision(MathContext math_context, BigDecimal bot, BigDecimal top) {
        MATH_CONTEXT = math_context;
        BOT = bot;
        TOP = top;
    }

    public HighPrecision() {
        this(new MathContext(30, RoundingMode.HALF_EVEN), new BigDecimal(-Double.MAX_VALUE), new BigDecimal(Double.MAX_VALUE));
    }

    public BigDecimal max(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) < 0 ? b : a;
    }

    public BigDecimal min(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    public BigDecimal clamp(BigDecimal x) {
        return min(max(x, BOT), TOP);
    }

    public BigDecimal mul(BigDecimal a, BigDecimal b) {
        BigDecimal ans = a.multiply(b, MATH_CONTEXT);
        return clamp(ans);
    }

    public BigDecimal plus(BigDecimal a, BigDecimal b) {
        BigDecimal ans = a.add(b, MATH_CONTEXT);
        return clamp(ans);
    }

    public BigDecimal divide(BigDecimal a, BigDecimal b) {
        BigDecimal ans = a.divide(b, MATH_CONTEXT);
        return clamp(ans);
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        BigDecimal ans = a.subtract(b, MATH_CONTEXT);
        return clamp(ans);
    }

    public BigDecimal pow(BigDecimal x, long n) {
        if (n == 0) {
            return BigDecimal.ONE;
        }
        BigDecimal val = pow(x, n / 2);
        val = mul(val, val);
        if (n % 2 == 1) {
            val = mul(val, x);
        }
        return val;
    }

    public BigDecimal sqrt(BigDecimal x, int time) {
        BigDecimal x0 = BigDecimal.ONE;
        while (time-- > 0) {
            x0 = subtract(x0, divide(subtract(mul(x0, x0), x), subtract(plus(x0, x0), x)));
        }
        return x0.abs();
    }
}
