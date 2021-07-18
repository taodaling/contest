package on2021_07.on2021_07_09_Library_Checker.BracketTreeTest;



import template.datastructure.BracketTree;
import template.io.FastInput;
import template.io.FastOutput;

public class BracketTreeTest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        char[] s = new char[n];
        in.rs(s);
        BracketTree bt = new BracketTree(n, i -> s[i] == '(' ? 1 : -1);
        for (int i = 0; i < m; i++) {
            int l = in.ri();
            int r = in.ri();
            long[] res0 = bt.query(l, r, null);
            int[] res1 = bt.minValidSequence(l, r, null);
            out.append(res0[0]).append(' ').append(res0[1]).append(' ');
            if(res1 == null){
                out.append(-1);
            }else{
                out.append(res1[0]).append(' ').append(res1[1]);
            }
            out.println();
        }
    }
}
