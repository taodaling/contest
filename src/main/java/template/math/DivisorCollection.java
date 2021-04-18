package template.math;

import template.primitve.generated.datastructure.LongHashMap;
import template.rand.Randomized;

import java.util.Arrays;

public class DivisorCollection {
    public long[] divisor;
    public LongHashMap inverseMap;
    LongFactorization factorization;
    private int wpos;

    public DivisorCollection(UnorderedDivisorVisitor visitor, boolean sort) {
        this.factorization = visitor.factorization;
        wpos = 0;
        divisor = new long[visitor.factorization.numberOfFactors()];
        visitor.visit(x -> {
            divisor[wpos++] = x;
        });
        if (sort) {
            Randomized.shuffle(divisor);
            Arrays.sort(divisor);
        }
        inverseMap = new LongHashMap(divisor.length, false);
        for (int i = 0; i < divisor.length; i++) {
            inverseMap.put(divisor[i], i);
        }
    }
}
