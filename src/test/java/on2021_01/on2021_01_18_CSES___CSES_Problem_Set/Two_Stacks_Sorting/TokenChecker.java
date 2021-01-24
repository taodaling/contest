package on2021_01.on2021_01_18_CSES___CSES_Problem_Set.Two_Stacks_Sorting;



import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import template.io.FastInput;

import java.util.ArrayDeque;
import java.util.Deque;

public class TokenChecker extends AbstractChecker {
    public TokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int n = stdin.ri();
        int[] a = stdin.ri(n);
        Deque<Integer>[] dqs = new Deque[]{
                new ArrayDeque(), new ArrayDeque()
        };
        int first = actual.ri();
        if (first == 0) {
            return expect.ri() == 0 ? Verdict.OK : Verdict.WA;
        }
        int next = 1;
        if (a[0] == next) {
            next++;
        } else {
            dqs[first - 1].add(a[0]);
        }
        for (int i = 1; i < n; i++) {
            int x = actual.ri() - 1;
            if (!dqs[x].isEmpty() && dqs[x].peekLast() < a[i]) {
                return Verdict.WA;
            }
            dqs[x].add(a[i]);
            while (true) {
                boolean pop = false;
                for (Deque<Integer> dq : dqs) {
                    if (!dq.isEmpty() && dq.peekLast() == next) {
                        next++;
                        dq.removeLast();
                        pop = true;
                    }
                }
                if (!pop) {
                    break;
                }
            }
        }
        return Verdict.OK;
    }
}