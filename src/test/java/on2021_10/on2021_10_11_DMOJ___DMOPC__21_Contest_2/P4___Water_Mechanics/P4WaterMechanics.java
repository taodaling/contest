package on2021_10.on2021_10_11_DMOJ___DMOPC__21_Contest_2.P4___Water_Mechanics;



import template.graph.DescartesNode;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class P4WaterMechanics {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        k = in.ri() * 2 + 1;
        move = (k + 1) / 2;
        int[] a = in.ri(n);
        List<Item> itemList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int l = i;
            int r = i;
            while (r + 1 < n && a[r + 1] == a[r]) {
                r++;
            }
            i = r;
            itemList.add(new Item(a[l], r - l + 1));
        }
        items = itemList.toArray(new Item[0]);
        int m = items.length;
        DescartesNode dn = DescartesNode.build(0, m - 1, (i, j) -> -Integer.compare(items[i].x, items[j].x), DescartesNode::new);
        int[][] ans = dfs(dn, 0, m - 1);
        int res = ans[0][0];
        out.println(res);
    }

    Item[] items;

    int[][] empty = new int[2][2];
    int k;
    int move;
    int inf = (int) 1e9;

    public int[][] dfs(DescartesNode root, int L, int R) {
        if (root == null) {
            return empty;
        }
        Item item = items[root.index];
        int[][] ans = new int[2][2];
        int[][] left = dfs(root.left, L, root.index - 1);
        int[][] right = dfs(root.right, root.index + 1, R);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                ans[i][j] = inf;
                for (int l = 0; l < 2; l++) {
                    for (int r = 0; r < 2; r++) {
                        int len = item.cnt;
                        if (left != empty || i != l) {
                            len += l;
                        }else if(i == 1){
                            len -= move;
                        }
                        if (right != empty || j != r) {
                            len += r;
                        }else if(j == 1){
                            len -= move;
                        }
                        len = Math.max(len, 0);
                        int req = DigitUtils.ceilDiv(len, k);
                        ans[i][j] = Math.min(ans[i][j], left[i][l] + right[r][j] + req);
                    }
                }
            }
        }
        debug.debug("L", L);
        debug.debug("R", R);
        debug.debugMatrix("ans", ans);
        return ans;
    }
    Debug debug = new Debug(false);
}

class Item {
    int x;
    int cnt;

    public Item(int x, int cnt) {
        this.x = x;
        this.cnt = cnt;
    }
}
