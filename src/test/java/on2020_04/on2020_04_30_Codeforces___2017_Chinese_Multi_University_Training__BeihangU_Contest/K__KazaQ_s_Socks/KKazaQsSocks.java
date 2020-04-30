package on2020_04.on2020_04_30_Codeforces___2017_Chinese_Multi_University_Training__BeihangU_Contest.K__KazaQ_s_Socks;



import template.io.FastInput;
import template.io.FastOutput;

public class KKazaQsSocks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }
        out.printf("Case #%d: ", testNumber);
        int n = in.readInt();
        long k = in.readLong();
        if (k <= n) {
            out.println(k);
            return;
        }
        Machine machine = new Machine(n, n);
        int ans = machine.process(k - n);
        out.println(ans);
    }
}


class Machine {
    int n;
    int cur;

    public Machine(int n, int cur) {
        this.n = n;
        this.cur = cur;
    }

    int process(long k) {
        //period n
        if (k < n - 1) {
            if (k == 0) {
                return cur;
            }
            return (int) k;
        }
        long jump = k / (n - 1);
        if (jump % 2 == 1) {
            cur = n + (n - 1) - cur;
        }
        return process(k % (n - 1));
    }
}