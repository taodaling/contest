package on2020_04.on2020_04_21_Codeforces___Codeforces_Round__449__Div__1_.A__Nephren_gives_a_riddle;



import template.io.FastInput;
import template.io.FastOutput;

public class ANephrenGivesARiddle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Machine machine = new Machine((int) 1e5);
        int q = in.readInt();
        for(int i = 0; i < q; i++){
            int n = in.readInt();
            long k = in.readLong();
            out.append(machine.get(n, k));
        }
    }
}

class Machine {
    long[] length;
    String s0 = "What are you doing at the end of the world? Are you busy? Will you save us?";
    String prefix = "What are you doing while sending \"";
    String middle = "\"? Are you busy? Will you send \"";
    String suffix = "\"?";
    static long inf = (long) (1e18 + 100);

    public Machine(int n) {
        length = new long[n + 1];
        length[0] = s0.length();
        for (int i = 1; i <= n; i++) {
            length[i] = Math.min(inf, length[i - 1] * 2 + prefix.length() + middle.length() + suffix.length());
        }
    }

    public char get(int n, long k) {
        return get0(n, k - 1);
    }

    private char get0(int n, long k) {
        if (k >= length[n]) {
            return '.';
        }
        if (n == 0) {
            return s0.charAt((int)k);
        }
        if (k < prefix.length()) {
            return prefix.charAt((int)k);
        }
        k -= prefix.length();
        if (k < length[n - 1]) {
            return get0(n - 1, k);
        }
        k -= length[n - 1];
        if (k < middle.length()) {
            return middle.charAt((int)k);
        }
        k -= middle.length();
        if (k < length[n - 1]) {
            return get0(n - 1, k);
        }
        k -= length[n - 1];
        return suffix.charAt((int)k);
    }
}