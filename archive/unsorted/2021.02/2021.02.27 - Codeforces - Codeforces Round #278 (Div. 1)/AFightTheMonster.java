package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class AFightTheMonster {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] Y = in.ri(3);
        int[] M = in.ri(3);
        int[] P = in.ri(3);
        int attck = 1;
        int hp = 0;
        int def = 2;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i <= 200; i++) {
            for (int j = 0; j <= 100; j++) {
                int yAttack = Y[attck] - M[def] + i;
                int mAttack = M[attck] - Y[def] - j;
                yAttack = Math.max(yAttack, 0);
                mAttack = Math.max(mAttack, 0);
                if (yAttack == 0) {
                    continue;
                }
                int round = DigitUtils.ceilDiv(M[hp], yAttack);
                int hpAtLeast = mAttack * round + 1;
                int addHp = Math.max(0, hpAtLeast - Y[hp]);
                int cost = addHp * P[hp] + i * P[attck] + j * P[def];
                min = Math.min(min, cost);
            }
        }
        out.println(min);
    }
}
