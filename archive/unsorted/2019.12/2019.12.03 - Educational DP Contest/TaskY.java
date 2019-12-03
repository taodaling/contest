package contest;

import jdk.internal.org.objectweb.asm.tree.FrameNode;
import template.datastructure.DiscreteMap;
import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Composite;
import template.math.Modular;

import java.util.Arrays;

public class TaskY {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Modular mod = new Modular(1e9 + 7);
        Composite comp = new Composite(200000, mod);
        int h = in.readInt();
        int w = in.readInt();
        int n = in.readInt();
        Block[] blocks = new Block[n];
        for (int i = 0; i < n; i++) {
            blocks[i] = new Block();
            blocks[i].r = in.readInt() - 1;
            blocks[i].c = in.readInt() - 1;
        }
        Arrays.sort(blocks, (a, b) -> Integer.compare(a.r + a.c, b.r + b.c));
        for (int i = 0; i < n; i++) {
            blocks[i].dp[1] = comp.composite(blocks[i].r + blocks[i].c,
                    blocks[i].r);
            for (int j = 0; j < i; j++) {
                if (blocks[j].r > blocks[i].r ||
                        blocks[j].c > blocks[i].c) {
                    continue;
                }
                for (int t = 0; t < 2; t++) {
                    blocks[i].dp[t] = mod.plus(blocks[i].dp[t],
                            mod.mul(blocks[j].dp[1 - t],
                                    comp.composite(blocks[i].r - blocks[j].r
                                                    + blocks[i].c - blocks[j].c,
                                            blocks[i].r - blocks[j].r)));
                }
            }
        }

        int sum = comp.composite(h + w - 2, h - 1);
        for (Block b : blocks) {
            int remain = comp.composite(h + w - 2 - b.r - b.c, h - 1 - b.r);
            sum = mod.plus(sum, mod.mul(remain, b.dp[0]));
            sum = mod.subtract(sum, mod.mul(remain, b.dp[1]));
        }

        out.println(sum);
    }
}

class Block {
    int r;
    int c;
    int[] dp = new int[2];
}