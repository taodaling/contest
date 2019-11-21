package contest;

import template.FastInput;
import template.FastOutput;
import template.ModBIT;
import template.SequenceUtils;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long a = in.readLong();
        long b = in.readLong();

        long[] nums = new long[n + 2];
        for (int i = 2; i <= n + 1; i++) {
            nums[i] = in.readLong();
        }
        nums[1] = -(long) 1e18;


        int[] al = new int[n + 2];
        int[] bl = new int[n + 2];
        int[] ar = new int[n + 2];
        int[] br = new int[n + 2];

        al[1] = 1;
        bl[1] = 1;
        ar[1] = 1;
        br[1] = 1;
        for (int i = 2; i <= n + 1; i++) {
            if (nums[i] - nums[i - 1] >= a) {
                al[i] = al[i - 1];
            } else {
                al[i] = i;
            }
            if (nums[i] - nums[i - 1] >= b) {
                bl[i] = bl[i - 1];
            } else {
                bl[i] = i;
            }

            if (i + 1 <= n + 1) {
                ar[i] = SequenceUtils.floorIndex(nums, nums[i + 1] - b, 1, i - 1) + 1;
                br[i] = SequenceUtils.floorIndex(nums, nums[i + 1] - a, 1, i - 1) + 1;
            } else {
                ar[i] = i;
                br[i] = i;
            }
        }

        Modular mod = new Modular(1e9 + 7);
        ModBIT bita = new ModBIT(n + 1, mod);
        ModBIT bitb = new ModBIT(n + 1, mod);

        bita.update(1, 1);
        bitb.update(1, 1);

        for (int i = 2; i <= n + 1; i++) {
            int dpOfA = 0;
            if(ar[i] >= al[i]) {
                if (ar[i] - 1 >= 1) {
                    dpOfA = mod.plus(dpOfA, bitb.query(ar[i] - 1));
                }
                if (al[i] - 2 >= 1) {
                    dpOfA = mod.subtract(dpOfA, bitb.query(al[i] - 2));
                }
            }
            int dpOfB = 0;
            if(br[i] >= bl[i]) {
                if (br[i] - 1 >= 1) {
                    dpOfB = mod.plus(dpOfB, bita.query(br[i] - 1));
                }
                if (bl[i] - 2 >= 1) {
                    dpOfB = mod.subtract(dpOfB, bita.query(bl[i] - 2));
                }
            }

            bita.update(i, dpOfA);
            bitb.update(i, dpOfB);
        }

        int ans1 = mod.subtract(bita.query(n + 1), bita.query(n));
        int ans2 = mod.subtract(bitb.query(n + 1), bitb.query(n));
        int ans = mod.plus(ans1, ans2);

        out.println(ans);
    }
}
