package template.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Function;

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

    public BigDecimal sqrt(BigDecimal y, BigDecimal prec) {
        return newtonMethod(x -> subtract(mul(x, x), y), x -> plus(x, x), y, prec).abs();
    }

    public BigDecimal newtonMethod(Function<BigDecimal, BigDecimal> func, Function<BigDecimal, BigDecimal> derivative, BigDecimal x0, BigDecimal prec) {
        while (func.apply(x0).abs().compareTo(prec) > 0) {
            x0 = subtract(x0, divide(func.apply(x0), derivative.apply(x0)));
        }
        return x0;
    }
}
