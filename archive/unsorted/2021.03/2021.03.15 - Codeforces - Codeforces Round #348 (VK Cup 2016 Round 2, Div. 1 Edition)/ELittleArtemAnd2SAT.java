package contest;

import template.datastructure.BitSet;
import template.graph.TwoSatDense;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;


public class ELittleArtemAnd2SAT {
    int n;

    private void output(TwoSatDense A, FastOutput out) {
        boolean[] sol = A.solve();
        for (int i = 0; i < n; i++) {
            out.append(sol[A.elementId(i)] ? 1 : 0).append(' ');
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int m1 = in.ri();
        int m2 = in.ri();
        TwoSatDense A = new TwoSatDense(n);
        TwoSatDense B = new TwoSatDense(n);
        for (int i = 0; i < m1; i++) {
            int a = in.ri();
            int b = in.ri();
            int aId = a < 0 ? A.negateElementId(-a - 1) : A.elementId(a - 1);
            int bId = b < 0 ? A.negateElementId(-b - 1) : B.elementId(b - 1);
            A.or(aId, bId);
        }
        for (int i = 0; i < m2; i++) {
            int a = in.ri();
            int b = in.ri();
            int aId = a < 0 ? B.negateElementId(-a - 1) : A.elementId(a - 1);
            int bId = b < 0 ? B.negateElementId(-b - 1) : B.elementId(b - 1);
            B.or(aId, bId);
        }
        if (A.solve() == null) {
            TwoSatDense tmp = A;
            A = B;
            B = tmp;
        }
        if (A.solve() == null) {
            //ok
            out.println("SIMILAR");
            return;
        }
        if (B.solve() == null) {
            output(A, out);
            return;
        }
        BitSet tmp = new BitSet(2 * n);
        BitSet must = new BitSet(2 * n);
        for (int i = 0; i < n * 2; i++) {
            if (A.mustTrue(i) != B.mustTrue(i) || A.mustFalse(i) != B.mustFalse(i)) {
                if (!A.onlyOneChoice(i)) {
                    TwoSatDense tmpSet = A;
                    A = B;
                    B = tmpSet;
                }
                if (A.mustTrue(i)) {
                    B.isFalse(i);
                } else {
                    B.isTrue(i);
                }
                output(B, out);
                return;
            }
            if (A.onlyOneChoice(i)) {
                must.set(i);
            }
        }
        must.flip(0, must.capacity() - 1);
        for (int i = 0; i < n * 2; i++) {
            if(!must.get(i)){
                continue;
            }
            tmp.copy(A.getRelyClosure()[i]);
            tmp.xor(B.getRelyClosure()[i]);
            tmp.and(must);
            if (tmp.size() > 0) {
                int firstBit = tmp.nextSetBit(0);
                if (!A.getRelyClosure()[i].get(firstBit)) {
                    TwoSatDense tmpSet = A;
                    A = B;
                    B = tmpSet;
                }

                assert B.test(new IntegerArrayList(new int[]{i, firstBit ^ 1}));
                B.isTrue(i);
                B.isFalse(firstBit);
                output(B, out);
                return;
            }
        }

        out.println("SIMILAR");
    }
}
