package template.math;

public interface ILongModular {
    long getMod();

    default long plus(long a, long b) {
        return DigitUtils.modplus(a, b, getMod());
    }

    default long subtract(long a, long b){
        return DigitUtils.modsub(a, b, getMod());
    }

    default long valueOf(long x) {
        return DigitUtils.mod(x, getMod());
    }

    long mul(long a, long b);

    default ILongModular getModularForPowerComputation() {
        return ILongModular.getInstance(getMod() - 1);
    }

    static ILongModular getInstance(long mod) {
        if (mod <= (1L << 50)) {
            return new LongModularDanger(mod);
        }
        if (mod == (1L << 61) - 1) {
            return LongModular2305843009213693951.getInstance();
        }
        return new LongModular(mod);//new BigLongModular(mod);//new LongModular(mod);
    }
}
