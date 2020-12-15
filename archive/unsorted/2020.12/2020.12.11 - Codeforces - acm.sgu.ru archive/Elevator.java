package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CollectionUtils;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class Elevator {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int now = in.ri();
        boolean[] pressed = new boolean[101];
        List<Integer> seq = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            seq.add(in.ri());
        }
        for (int x : seq) {
            pressed[x] = true;
        }
        SequenceUtils.reverse(seq);
        while (!seq.isEmpty()) {
            int back = CollectionUtils.pop(seq);
            if (!pressed[back]) {
                continue;
            }
            while (true) {
                if (pressed[now]) {
                    pressed[now] = false;
                    out.println(now);
                }
                if (now == back) {
                    break;
                } else if (now < back) {
                    now++;
                } else if (now > back) {
                    now--;
                }
            }
        }
        
    }
}
