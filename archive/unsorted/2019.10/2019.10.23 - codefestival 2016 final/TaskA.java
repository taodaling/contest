package contest;

import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.readInt();
        int w = in.readInt();

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if(in.readString().equals("snuke")){
                    out.append((char)('A' + j)).append(i + 1);
                    return;
                }
            }
        }
    }
}
