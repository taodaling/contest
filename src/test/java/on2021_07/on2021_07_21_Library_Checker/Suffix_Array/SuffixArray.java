package on2021_07.on2021_07_21_Library_Checker.Suffix_Array;



import template.io.FastInput;
import template.io.FastOutput;
import template.string.SuffixBalancedTree;
import template.utils.SequenceUtils;

public class SuffixArray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = (int)5e5;
        int[] a = new int[n];
        int m = in.rs(a);
        SequenceUtils.reverse(a, 0, m - 1);
        SuffixBalancedTree sbt = new SuffixBalancedTree(n);
        for(int i = 0; i < m; i++){
            sbt.addPrefix(a[i]);
        }
        int[] sa = sbt.sa();
        for(int x : sa){
            out.append(m - 1 - x).append(' ');
        }
    }
}
