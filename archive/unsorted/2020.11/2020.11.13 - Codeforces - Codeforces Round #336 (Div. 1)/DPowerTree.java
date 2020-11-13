package contest;

import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Power;
import template.utils.CloneSupportObject;
import template.utils.Debug;
import template.utils.Sum;
import template.utils.Update;

import java.util.ArrayList;
import java.util.List;

public class DPowerTree {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int rootV = in.readInt();
        int q = in.readInt();
        int[][] qs = new int[q][3];
        List<Node> nodes = new ArrayList<>(q);
        nodes.add(new Node(nodes.size()));
        for (int i = 0; i < q; i++) {
            qs[i][0] = in.readInt();
            qs[i][1] = in.readInt();
            if (qs[i][0] == 1) {
                qs[i][2] = in.readInt();
                Node node = new Node(nodes.size());
                nodes.add(node);
                Node p = nodes.get(qs[i][1] - 1);
                p.adj.add(node);
            }
        }

        dfs(nodes.get(0));
        debug.debug("nodes", nodes);

        int mod = (int) 1e9 + 7;
        Power pow = new Power(mod);

        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(1, order, SumImpl::new,
                UpdateImpl::new, i -> {
            SumImpl ans = new SumImpl();
            ans.path = 1;
            return ans;
        });
        debug.debug("st", st);
        UpdateImpl upd = new UpdateImpl();
        SumImpl sum1 = new SumImpl();
        SumImpl sum2 = new SumImpl();
        upd.mul = 1;
        upd.plus = rootV;
        st.update(1, 1, 1, order, upd);
        int lastNode = 0;
        int[] size = new int[nodes.size()];
        size[0]++;
        debug.debug("st", st);
        for (int[] query : qs) {
            if (query[0] == 1) {
                lastNode++;
                int p = query[1] - 1;
                int v = query[2];
                upd.plus = v;
                upd.mul = 1;
                size[lastNode]++;
                st.update(nodes.get(lastNode).dfnOpen,
                        nodes.get(lastNode).dfnOpen,
                        1, order, upd);
                upd.plus = 0;
                upd.mul = (long) (size[p] + 1) * pow.inverse(size[p]) % mod;
                st.update(nodes.get(p).dfnOpen,
                        nodes.get(p).dfnClose, 1, order, upd);
                size[p]++;
            } else {
                Node node = nodes.get(query[1] - 1);
                sum1.sum = 0;
                sum1.path = 0;
                st.query(node.dfnOpen, node.dfnClose, 1, order, sum1);
                sum2.sum = 0;
                sum2.path = 0;
                st.query(node.dfnOpen, node.dfnOpen, 1, order, sum2);
                long ans = sum1.sum;
                ans = ans * pow.inverse((int)sum2.path) % mod * size[node.id] % mod;
                out.println(ans);
            }
            debug.debug("st", st);
        }
    }

    int order = 0;

    public void dfs(Node root) {
        root.dfnOpen = ++order;
        for (Node node : root.adj) {
            dfs(node);
        }
        root.dfnClose = order;
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    long plus;
    long mul;
    static int mod = (int) 1e9 + 7;


    @Override
    public void update(UpdateImpl update) {
        plus = plus * update.mul % mod;
        mul = mul * update.mul % mod;
        plus = DigitUtils.modplus(plus, update.plus, mod);
    }

    @Override
    public void clear() {
        plus = 0;
        mul = 1;
    }

    @Override
    public boolean ofBoolean() {
        return !(mul == 1 && plus == 0);
    }

}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    long path;
    long sum;

    static int mod = (int) 1e9 + 7;

    @Override
    public void add(SumImpl right) {
        sum = DigitUtils.modplus(sum, right.sum, mod);
        path = DigitUtils.modplus(path, right.path, mod);
    }

    @Override
    public void update(UpdateImpl update) {
        path = path * update.mul % mod;
        sum = (sum * update.mul + update.plus * path) % mod;
    }

    @Override
    public void copy(SumImpl right) {
        sum = right.sum;
        path = right.path;
    }

    @Override
    public SumImpl clone() {
        SumImpl s = new SumImpl();
        s.copy(this);
        return s;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", path, sum);
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int id;
    int dfnOpen;
    int dfnClose;

    public Node(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", id + 1, dfnOpen, dfnClose);
    }
}

