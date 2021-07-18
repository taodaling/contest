package on2021_07.on2021_07_17_Codeforces___Codeforces_Round__733__Div__1___Div__2__based_on_VK_Cup_2021___Elimination__Engine__.E__Minimax;



import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;
import template.string.KMPAutomaton;

import java.util.Arrays;

public class TokenChecker extends AbstractChecker {
    public TokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        stdin.ri();
        String s = expect.rs();
        String res = actual.rs();
        return s.equals(res) ? Verdict.OK : Verdict.WA;
//        boolean same = true;
//        for (int i = 0; i < s.length(); i++) {
//            if (s.charAt(i) != s.charAt(0)) {
//                same = false;
//            }
//        }
//        char[] sA = s.toCharArray();
//        char[] resA = res.toCharArray();
//        Arrays.sort(sA);
//        Arrays.sort(resA);
//        if (!Arrays.equals(sA, resA)) {
//            return Verdict.WA;
//        }
//        if (same) {
//            return s.equals(res) ? Verdict.OK : Verdict.WA;
//        }
//        KMPAutomaton kmp = new KMPAutomaton(res.length());
//        for (char c : res.toCharArray()) {
//            kmp.build(c);
//        }
//        for (int i = 1; i < res.length(); i++) {
//            if (kmp.maxBorder(i) > 1){
//                return Verdict.WA;
//            }
//        }
//        return Verdict.OK;
    }
}