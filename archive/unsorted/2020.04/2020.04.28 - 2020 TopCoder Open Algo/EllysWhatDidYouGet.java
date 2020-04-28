package contest;

public class EllysWhatDidYouGet {
    public int getCount(int N) {
        int ans = 0;
        for (int a = 1; a <= 50; a++) {
            for (int b = 1; b <= 50; b++) {
                for (int c = 1; c <= 50; c++) {
                    int x = check(1, a, b, c);
                    boolean valid = true;
                    for (int i = 2; i <= N; i++) {
                        if (x != check(i, a, b, c)) {
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        ans++;
                    }
                }
            }
        }
        return ans;
    }


    public int sum(int x) {
        return x == 0 ? 0 : (x % 10 + sum(x / 10));
    }

    public int check(int x, int a, int b, int c) {
        int ans = (x * a + b) * c;
        return sum(ans);
    }

}
