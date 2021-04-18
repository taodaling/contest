package template.math;

import template.primitve.generated.datastructure.LongComparator;
import template.primitve.generated.datastructure.LongHashSet;
import template.primitve.generated.datastructure.LongIterator;
import template.primitve.generated.datastructure.LongPriorityQueue;

public class OrderedDivisorIterator implements LongIterator {
    LongFactorization factorization;
    LongPriorityQueue pq = new LongPriorityQueue((int) 2e5, LongComparator.REVERSE_ORDER);
    LongHashSet set = new LongHashSet((int) 2e5, false);
    boolean inc;

    public OrderedDivisorIterator(boolean inc) {
        this.inc = inc;
    }

    public void init(LongFactorization factorization) {
        this.factorization = factorization;
        set.clear();
        pq.clear();
        pq.add(factorization.g);
    }

    @Override
    public boolean hasNext() {
        return !pq.isEmpty();
    }

    /**
     * Overall time complexity is O(n\log_2n) while n is the number of factors of g.
     * But each call will be atmost O(15\log_2n), which is fast enough to support top k.
     */
    @Override
    public long next() {
        long head = pq.pop();
        for (long p : factorization.primes) {
            long cand = head / p;
            if (cand * p != head || set.contain(cand)) {
                continue;
            }
            set.add(cand);
            pq.add(cand);
        }
        return inc ? factorization.g / head : head;
    }
}
