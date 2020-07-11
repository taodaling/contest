package contest;

import template.binary.Bits;
import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class CHongcowBuysADeckOfCards {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Card[] cards = new Card[n];
        for (int i = 0; i < n; i++) {
            cards[i] = new Card();
            cards[i].type = in.readChar() == 'R' ? 0 : 1;
            cards[i].r = in.readInt();
            cards[i].b = in.readInt();
        }

        int[][] save = new int[150][1 << n];
        SequenceUtils.deepFill(save, -(int) 1e9);
        save[0][0] = 0;
        for (int i = 0; i < 1 << n; i++) {
            int rCnt = 0;
            int bCnt = 0;
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 0) {
                    continue;
                }
                if (cards[j].type == 0) {
                    rCnt++;
                } else {
                    bCnt++;
                }
            }
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 1) {
                    continue;
                }
                int next = Bits.set(i, j);
                int rSave = Math.min(cards[j].r, rCnt);
                int bSave = Math.min(cards[j].b, bCnt);
                for (int k = 0; k < 150; k++) {
                    if (k + rSave < 150) {
                        save[k + rSave][next] = Math.max(save[k + rSave][next], save[k][i] + bSave);
                    }
                }
            }
        }

        int rSum = 0;
        int bSum = 0;
        for (int i = 0; i < n; i++) {
            rSum += cards[i].r;
            bSum += cards[i].b;
        }

        int ans = (int) 1e9;
        int mask = (1 << n) - 1;
        for (int i = 0; i < 150; i++) {
            int time = Math.max(Math.abs(rSum - i), Math.abs(bSum - save[i][mask]));
            ans = Math.min(ans, time);
        }
        ans += n;

        out.println(ans);
    }
}

class Card {
    int type;
    int r;
    int b;
}