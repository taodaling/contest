package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Comparator;

public class BonnieAndClyde {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int d = in.ri();
        Bank[] banks = new Bank[n];
        for (int i = 0; i < n; i++) {
            banks[i] = new Bank(i, in.ri(), in.ri());
        }
        Arrays.sort(banks, Comparator.comparingInt(x -> x.x));
        int maxChoice = -1;
        int bestI = -1;
        int bestJ = -1;

        for (int i = 0, l = -1; i < n; i++) {
            while (l + 1 < i && banks[i].x - banks[l + 1].x >= d) {
                l++;
                if (maxChoice == -1 || banks[l].w > banks[maxChoice].w) {
                    maxChoice = l;
                }
            }
            if (maxChoice != -1 && (bestI == -1 || banks[maxChoice].w + banks[i].w >
                    banks[bestI].w + banks[bestJ].w)) {
                bestI = i;
                bestJ = maxChoice;
            }
        }
        if(bestI == -1){
            out.println("-1 -1");
            return;
        }
        out.append(banks[bestI].id + 1).append(' ').append(banks[bestJ].id + 1);
    }
}

class Bank {
    int id;
    int x;
    int w;

    public Bank(int id, int x, int w) {
        this.id = id;
        this.x = x;
        this.w = w;
    }
}