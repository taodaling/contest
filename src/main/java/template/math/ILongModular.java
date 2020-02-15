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
        //return new LongModularDanger(mod);
        return mod <= (1L << 54) ? new LongModularDanger(mod) : new LongModular(mod);
    }
}
