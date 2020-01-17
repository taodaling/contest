package contest;

import template.datastructure.SuffixTree;
import template.io.FastInput;
import template.io.FastOutput;

public class LongLongMessage {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] a = new int[100000 + 1];
        int[] b = new int[100000 + 1];
        int end = 'z' + 1;
        int aLen = in.readString(a, 0);
        int bLen = in.readString(b, 0);

        SuffixTree st = new SuffixTree(aLen + bLen + 1, 'a',  end);
        for(int i = 0; i < aLen; i++){
            st.append(a[i]);
        }
        for(int i = 0; i < bLen; i++){
            st.append(b[i]);
        }
        st.append(end);

        int ans = st.lcs(aLen - 1);
        out.println(ans);
    }
}
