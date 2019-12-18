package on2019_12.on2019_12_17_Codeforces_Global_Round_6.B___Dice_Tower;





import template.io.FastInput;
import template.io.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long x = in.readLong();
        long sum = 0;
        for (int i = 1; i <= 6; i++) {
            sum += i;
        }
        for (int i = 1; i <= 6; i++) {
            long y = x - sum + i;
            if (y >= 0 && y % (sum - 7) == 0) {
                out.println("YES");
                return;
            }
        }
        out.println("NO");
    }

    public boolean able(long x) {
        return x >= 0 && x % 7 == 0;
    }
}
