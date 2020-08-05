package contest;

import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.checkers.Checker;
import template.io.FastInput;

import java.util.HashSet;
import java.util.Set;

public class DTokenChecker extends AbstractChecker {
    public DTokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int n = stdin.readInt();
        long limit = (long) 1e15;
        Set<Long> set = new HashSet<>(n * n);
        long[][] mat = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                long x = actual.readLong();
                if (x < 0 || x > limit || set.contains(x)) {
                    return Verdict.WA;
                }
                mat[i][j] = x;
                set.add(x);
            }
        }

        long m = -1;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n - 1; j++){
                long a = Math.min(mat[i][j], mat[i][j + 1]);
                long b = Math.max(mat[i][j], mat[i][j + 1]);
                if(m == -1){
                    m = b % a;
                }else if(b % a != m){
                    return Verdict.WA;
                }
            }
        }
        for(int i = 0; i < n - 1; i++){
            for(int j = 0; j < n; j++){
                long a = Math.min(mat[i][j], mat[i + 1][j]);
                long b = Math.max(mat[i][j], mat[i + 1][j]);
                if(m == -1){
                    m = b % a;
                }else if(b % a != m){
                    return Verdict.WA;
                }
            }
        }

        return Verdict.OK;
    }
}
