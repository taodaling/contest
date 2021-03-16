package template.math;

public class FractionComparator {
    static LongProduct p1 = new LongProduct();
    static LongProduct p2 = new LongProduct();

    /**
     * compare a / b and c / d
     */
    public static int compare(long a, long b, long c, long d) {
        if (b < 0) {
            a = -a;
            b = -b;
        }
        if (d < 0) {
            c = -c;
            d = -d;
        }
        p1.mul(a, d);
        p2.mul(b, c);
        return p1.compareTo(p2);
    }


}
