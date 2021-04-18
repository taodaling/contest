package template.math;

import java.util.function.LongConsumer;

public class UnorderedDivisorVisitor {
    LongFactorization factorization;

    public UnorderedDivisorVisitor() {
    }

    public void init(LongFactorization factorization) {
        this.factorization = factorization;
    }

    LongConsumer consumer;

    /**
     * O(the number of factors of g)
     *
     * @param consumer
     */
    public void visit(LongConsumer consumer) {
        this.consumer = consumer;
        visit0(factorization.g, factorization.primes.length - 1);
    }

    private void visit0(long v, int i) {
        if (i < 0) {
            consumer.accept(v);
            return;
        }
        visit0(v, i - 1);
        while (v % factorization.primes[i] == 0) {
            v /= factorization.primes[i];
            visit0(v, i - 1);
        }
    }
}
