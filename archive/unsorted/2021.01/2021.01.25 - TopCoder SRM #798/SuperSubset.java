package contest;

public class SuperSubset {
    int mod = (int) 1e9 + 7;
    long inv2 = (mod + 1) / 2;

    public int solve(int[] A, int Y) {
        long[] prev = new long[Y + 1];
        long[] next = new long[Y + 1];
        prev[0] = 1;
        for (int x : A) {
            for (int i = 0; i <= Y; i++) {
                next[i] = prev[i];
            }
            for (int i = 0; i + x <= Y; i++) {
                next[i + x] += prev[i] * inv2 % mod;
            }
            for (int i = 0; i <= Y; i++) {
                next[i] %= mod;
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }
        long ans = prev[Y];
        for (int i = 0; i < A.length; i++) {
            ans = ans * 2 % mod;
        }
        return (int) ans;
    }
}
