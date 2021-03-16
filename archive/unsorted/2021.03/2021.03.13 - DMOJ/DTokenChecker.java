package contest;

import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;

public class DTokenChecker extends AbstractChecker {
    public DTokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int n = stdin.ri();
        int k = stdin.ri();
        int first = expect.ri();
        int first2 = actual.ri();
        if((first != 0) != (first2 != 0)){
            return Verdict.WA;
        }
        if(first == 0){
            return Verdict.OK;
        }
        int[] a = new int[n];
        a[0] = first2;
        for(int i = 1; i < n; i++){
            a[i] = actual.ri();
        }
        return DMOPC20Contest4P5CyclicCypherTestCase.check(a, k) ? Verdict.OK : Verdict.WA;
    }
}