package contest;





import chelper.AbstractChecker;
import net.egork.chelper.tester.Verdict;
import template.datastructure.DSU;
import template.io.FastInput;

public class TokenChecker extends AbstractChecker {
    public TokenChecker(String parameters) {
        super(parameters);
    }

    @Override
    public Verdict check(FastInput stdin, FastInput expect, FastInput actual) {
        int n = stdin.readInt();
        int m = stdin.readInt();
        int[] a = new int[m];
        int[] b = new int[m];
        for(int i = 0; i < m; i++){
            a[i] = stdin.ri();
            b[i] = stdin.ri();
        }
        DSU d = new DSU(n);
        boolean[] used = new boolean[m];
        for(int t = 0; t < 3; t++) {
            d.init();
            for (int i = 0; i < n - 1; i++) {
                int e = actual.ri();
                if(used[e]){
                    return Verdict.WA;
                }
                used[e] = true;
                int x = a[e];
                int y = b[e];
                if (d.find(x) == d.find(y)){
                    return Verdict.WA;
                }
                d.merge(x, y);
            }
        }
        return Verdict.OK;
    }
}