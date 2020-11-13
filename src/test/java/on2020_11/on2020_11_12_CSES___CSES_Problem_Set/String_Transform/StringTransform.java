package on2020_11.on2020_11_12_CSES___CSES_Problem_Set.String_Transform;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CompareUtils;
import template.utils.Debug;

import java.util.stream.IntStream;

public class StringTransform {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e6 + 10];
        int n = in.readString(s, 0);
        int base = (int) 1e7;
        int[] indices = IntStream.range(0, n).toArray();
        CompareUtils.radixSort(indices, 0, n - 1, i -> s[i] * base + i);
        int start = indices[0];
        debug.debug("indices", indices);
        for (int i = 0; i < n - 1; i++) {
            start = indices[start];
            out.append(s[start]);
        }
    }
}
