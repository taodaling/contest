package contest;

import template.datastructure.BinaryTree;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.HashSet;
import java.util.Set;

public class SetXorMin {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int height = 29;
        int q = in.readInt();
        BinaryTree bt = new BinaryTree();
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            int x = in.readInt();
            if (t == 0) {
                if (bt.find(x, height) == 0) {
                    bt.add(x, height, 1);
                }
            }else if(t == 1){
                if(bt.find(x, height) == 1){
                    bt.add(x, height, -1);
                }
            }else{
                int ans = bt.minXor(x, height);
                out.println(ans);
            }
        }
    }
}
