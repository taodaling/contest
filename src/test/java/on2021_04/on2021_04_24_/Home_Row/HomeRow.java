package on2021_04.on2021_04_24_.Home_Row;



import template.io.FastInput;
import template.io.FastOutput;

public class HomeRow {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int limit = (int) 1e6;
        char[] a = new char[limit];
        char[] b = new char[limit];
        int n = in.rs(a);
        int m = in.rs(b);
        int lcp = 0;
        for (int i = 0; i < n && i < m; i++) {
            if (a[i] == b[i]) {
                lcp++;
            } else {
                break;
            }
        }
        int ans = n - lcp + m - lcp;
        out.println(ans);
    }
}
