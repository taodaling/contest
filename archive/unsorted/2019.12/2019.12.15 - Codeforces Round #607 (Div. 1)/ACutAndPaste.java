package contest;

import com.sun.org.apache.xpath.internal.operations.Mod;
import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

public class ACutAndPaste {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt();
        int[] s = new int[Math.max(x, 500)];
        int tail = in.readString(s, 0) - 1;
        for(int i = 0; i <= tail; i++){
            s[i] -= '0';
        }
        int head = 0;
        Modular mod = new Modular(1e9 + 7);
        int ans = tail + 1;
        while (head <= x - 1) {
            int part1 = head + 1;
            int part2 = mod.subtract(ans, part1);
            ans = mod.plus(part1, mod.mul(part2, s[head]));
            tail = paste(s, head + 1, tail, s[head]);
            head++;
        }

        out.println(ans);
    }

    public int paste(int[] s, int l, int r, int time) {
        if (time == 1) {
            return r;
        }
        int index = r + 1;
        int length = r - l + 1;
        while (index < s.length && index - r <= length * (time - 1)) {
            s[index] = s[l + (index - r - 1) % length];
            index++;
        }
        return index - 1;
    }
}
