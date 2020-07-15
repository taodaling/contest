package on2020_07.on2020_07_12_AtCoder___CODE_FESTIVAL_2017_Final.F___Distribute_Numbers;



import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.datastructure.BitSet;
import template.io.FastInput;

public class CTokenChecker extends AbstractChecker {


    public CTokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int n = actual.readInt();
        int k = actual.readInt();
        BitSet[] bs = new BitSet[n];
        for (int i = 0; i < n; i++) {
            bs[i] = new BitSet(n);
        }
        int[] cnts = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                int x = actual.readInt() - 1;
                bs[i].set(x);
                cnts[x]++;
            }
        }
        BitSet tmp = new BitSet(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                tmp.copy(bs[i]);
                tmp.and(bs[j]);
                if (tmp.size() != 1) {
                    return Verdict.WA;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (cnts[i] != k) {
                return Verdict.WA;
            }
        }
        return Verdict.OK;
    }
}
