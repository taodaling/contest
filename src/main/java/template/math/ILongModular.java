package template.math;

public interface ILongModular {
    long getMod();

    long plus(long a, long b);

    long subtract(long a, long b);

    long valueOf(long x);

    long mul(long a, long b);

    default ILongModular getModularForPowerComputation() {
        return ILongModular.getInstance(getMod() - 1);
    }


    static ILongModular getInstance(long mod) {
        //return new HandyLongModular(mod);
        if (mod <= (1L << 54)) {
            return new LongModularDanger(mod);
        }
        if (mod == (1L << 61) - 1) {
            return LongModular2305843009213693951.getInstance();
        }
        return new HandyLongModular(mod);//new BigLongModular(mod);//new LongModular(mod);
    }
}
