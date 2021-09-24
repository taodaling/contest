package on2021_09.on2021_09_09_DMOJ___DMOPC__21_Contest_1.CHTTest;



import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;

public class CHTTestTokenChecker extends AbstractChecker {
    public CHTTestTokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
//        while (expect.hasMore()) {
//            double a = expect.rd();
//            double b = actual.rd();
//            if (Math.abs(a - b) > 1e-6 && Math.abs(a - b) / a > 1e-6) {
//                return Verdict.WA;
//            }
//        }
        return Verdict.OK;
    }
}