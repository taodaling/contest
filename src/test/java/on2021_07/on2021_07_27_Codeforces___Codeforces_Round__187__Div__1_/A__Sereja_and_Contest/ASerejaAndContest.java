package on2021_07.on2021_07_27_Codeforces___Codeforces_Round__187__Div__1_.A__Sereja_and_Contest;



import template.io.FastInput;
import template.io.FastOutput;

public class ASerejaAndContest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        long[] a = in.rl(n);
        long ps = 0;
        int delete = 0;
        for (int i = 1; i <= n; i++) {
            long x = a[i - 1];
            int rank = i - delete;
            long d = ps - (n - delete - rank) * x * (rank - 1);
            if (d < k) {
                //delete
                delete++;
                out.println(i);
                continue;
            }
            ps += x * (rank - 1);
        }
    }
}


