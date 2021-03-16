package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.string.*;
import template.utils.SequenceUtils;

public class P2870USACO07DECBestCowLineG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] s = new int[n * 2 + 1];
        for (int i = 0; i < n; i++) {
            s[i] = in.rc();
        }
        s[n] = 0;
        System.arraycopy(s, 0, s, n + 1, n);
        SequenceUtils.reverse(s, n + 1, 2 * n);
        //SAIS sais = new SAIS(s);
        int[] sa = SuffixArrayDC3.suffixArray(new IntArrayIntSequenceAdapter(s, 0, 2 * n));
        int[] rank = SuffixArrayDC3.rank(sa);
        //int[] rank = sais.index2Rank;
        int l = 0;
        int r = n + 1;
        for (int i = 0; i < n; i++) {
            if (rank[l] < rank[r]) {
                out.append((char)s[l]);
                l++;
            }else{
                out.append((char)s[r]);
                r++;
            }
            if(i % 80 == 79){
                out.append('\n');
            }
        }
    }
}
