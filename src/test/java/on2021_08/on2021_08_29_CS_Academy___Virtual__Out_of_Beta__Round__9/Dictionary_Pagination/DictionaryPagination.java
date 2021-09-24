package on2021_08.on2021_08_29_CS_Academy___Virtual__Out_of_Beta__Round__9.Dictionary_Pagination;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class DictionaryPagination {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        String[] ss = new String[n];
        for(int i = 0; i < n; i++){
            ss[i] = in.rs();
        }
        Arrays.sort(ss);
        for(int i = 0; i < q; i++){
            String w = in.rs();
            int offset = Arrays.binarySearch(ss, w);
            int x = in.ri();
            int ans = offset / x + 1;
            out.println(ans);
        }
    }
}
