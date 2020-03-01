package on2020_02.on2020_02_29_CodeChef___February_Lunchtime_2020_Division_2.Not_All_Flavours;



import template.io.FastInput;
import template.io.FastOutput;

public class NotAllFlavours {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        Machine machine = new Machine(k + 1);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int ans = 0;
        for (int i = 0, l = 0, r = -1; i < n; i++) {
            while (l < i) {
                machine.remove(a[l]);
                l++;
            }
            while (r + 1 < n && machine.total < k) {
                machine.add(a[r + 1]);
                r++;
            }
            if (machine.total == k) {
                ans = Math.max(ans, r - i);
            } else {
                ans = Math.max(ans, r - i + 1);
            }
        }
        out.println(ans);
    }
}

class Machine {
    int[] cnts;
    int total = 0;

    public Machine(int n) {
        cnts = new int[n];
    }

    public void add(int x) {
        cnts[x]++;
        if (cnts[x] == 1) {
            total++;
        }
    }

    public void remove(int x) {
        cnts[x]--;
        if (cnts[x] == 0) {
            total--;
        }
    }
}