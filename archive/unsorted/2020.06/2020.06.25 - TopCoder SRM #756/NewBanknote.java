package contest;

public class NewBanknote {
    int[] money = new int[]{1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 50000};

    public int solve(int x, int i) {
        if (x == 0) {
            return 0;
        }
        return x / money[i] + solve(x % money[i], i - 1);
    }

    public int[] fewestPieces(int newBanknote, int[] amountsToPay) {
        int[] ans = new int[amountsToPay.length];
        for (int i = 0; i < amountsToPay.length; i++) {
            int a = amountsToPay[i];
            ans[i] = (int) 2e9;
            for (int j = 0; j < 50000 && (long)j * newBanknote <= a; j++) {
                ans[i] = Math.min(ans[i], solve(a - j * newBanknote, money.length - 1) + j);
            }
        }
        return ans;
    }
}
