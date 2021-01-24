package contest;

import template.datastructure.Treap;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.function.Consumer;

public class CutAndPaste {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Treap root = Treap.NIL;
        for (int i = 0; i < n; i++) {
            Treap node = new Treap();
            node.key = in.rc();
            root = Treap.merge(root, node);
        }
        for (int i = 0; i < m; i++) {
            int l = in.ri();
            int r = in.ri();
            Treap[] a = Treap.splitByRank(root, l - 1);
            Treap[] b = Treap.splitByRank(a[1], r - l + 1);
            SequenceUtils.swap(b, 0, 1);
            a[1] = Treap.merge(b[0], b[1]);
            root = Treap.merge(a[0], a[1]);
        }
        dfs(root, x -> out.append((char) x.key));
    }

    public void dfs(Treap root, Consumer<Treap> consumer) {
        if (root == Treap.NIL) {
            return;
        }
        dfs(root.left, consumer);
        consumer.accept(root);
        dfs(root.right, consumer);
    }
}
