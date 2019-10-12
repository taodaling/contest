package on2019_10.on2019_10_11_Atcoder_codefestival_2016.TaskC;



import java.io.PrintWriter;

import template.FastInput;

public class TaskC {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        char[] s = new char[100000];
        int n = in.readString(s, 0);
        int k = in.readInt();

        for (int i = 0; i < n && k > 0; i++) {
            if(s[i] == 'a'){
                continue;
            }
            int atLeast = 'z' + 1 - s[i];
            if (k < atLeast) {
                continue;
            }
            s[i] = 'a';
            k -= atLeast;
        }

        s[n - 1] = (char) ((s[n - 1] - 'a' + k) % ('z' - 'a' + 1) + 'a');
        out.println(String.valueOf(s, 0, n));
    }
}
