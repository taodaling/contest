package on2020_05.on2020_05_06_POJ___POJ_Founder_Monthly_Contest___2008_03_16.Light_Switching_Game;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Nimber;

public class LightSwitchingGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }
        int n = in.readInt();
        int sum = 0;
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            int z = in.readInt();
            sum ^= Nimber.product(Nimber.product(x, y), z);
        }
        if (sum == 0) {
            out.println("Yes");
        } else {
            out.println("No");
        }
    }
}
