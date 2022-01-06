package template.math;

import template.polynomial.Polynomials;
import template.primitve.generated.datastructure.IntegerIterator;
import template.utils.SequenceUtils;

/**
 * https://codeforces.com/blog/entry/96199?#comment-852652
 */
public class KthTermOfLinearRecurrence {
    int[] Q;

    public KthTermOfLinearRecurrence(int[] recurrence) {
        int rank = Polynomials.rankOf(recurrence);
        SequenceUtils.reverse(recurrence, 0, rank);
        Q = recurrence;
    }

    public int kth(IntegerIterator n) {
    }

    int rec(IntegerIterator n) {
        if (!n.hasNext()) {

        }
    }
}
