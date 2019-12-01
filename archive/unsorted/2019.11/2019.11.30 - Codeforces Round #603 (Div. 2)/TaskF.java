package contest;

import template.datastructure.SparseTable;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int a = in.readInt();
        Node[] top = new Node[a + 1];
        for (int i = 1; i <= a; i++) {
            top[i] = new Node();
            top[i].id = i;
        }
        for (int i = 2; i <= a; i++) {
            Node p = top[in.readInt()];
            top[i].p = p;
            p.next.add(top[i]);
        }
        Node[] topRelation = new Node[n];
        for (int i = 0; i < n; i++) {
            topRelation[i] = top[in.readInt()];
        }
        int b = in.readInt();
        Node[] bot = new Node[b + 1];
        for (int i = 1; i <= b; i++) {
            bot[i] = new Node();
            bot[i].id = i;
        }
        for (int i = 2; i <= b; i++) {
            Node p = bot[in.readInt()];
            bot[i].p = p;
            p.next.add(bot[i]);
        }
        Node[] botRelation = new Node[n];
        for (int i = 0; i < n; i++) {
            botRelation[i] = bot[in.readInt()];
        }

        Node topRoot = null;
        for (int i = 1; i <= a; i++) {
            if (top[i].p == null) {
                topRoot = top[i];
            }
        }
        Node botRoot = null;
        for (int i = 1; i <= b; i++) {
            if (bot[i].p == null) {
                botRoot = bot[i];
            }
        }
        List<Node> topTrace = new ArrayList<>(2 * a);
        List<Node> botTrace = new ArrayList<>(2 * b);
        dfs(topRoot, topTrace);
        dfs(botRoot, botTrace);

        SparseTable<Node> topST = new SparseTable<>(topTrace.toArray(), topTrace.size(), (x, y) -> x.dfn <
                y.dfn ? x : y);
        SparseTable<Node> botST = new SparseTable<>(botTrace.toArray(), botTrace.size(), (x, y) -> x.dfn <
                y.dfn ? x : y);


        int[][] topReq = new int[n][n];
        int[][] botReq = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                Node l = topRelation[i];
                Node r = topRelation[j];
                if (i == j) {
                    topReq[i][j] = l.depth;
                } else {
                    topReq[i][j] = topReq[i][j - 1];
                    Node lca = lca(topST, topRelation[j - 1], topRelation[j]);
                    topReq[i][j] += r.depth - lca.depth;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                Node l = botRelation[i];
                Node r = botRelation[j];
                if (i == j) {
                    botReq[i][j] = l.depth;
                } else {
                    botReq[i][j] = botReq[i][j - 1];
                    Node lca = lca(botST, botRelation[j - 1], botRelation[j]);
                    botReq[i][j] += r.depth - lca.depth;
                }
            }
        }

        int[][] dp = new int[n][2];
        SequenceUtils.deepFill(dp, (int) 1e8);
        dp[0][0] = topReq[0][0];
        dp[0][1] = botReq[0][0];

        for (int i = 1; i < n; i++) {

            for (int j = 0; j <= i - 1; j++) {
                if (j == 0) {
                    dp[i][0] = Math.min(dp[i][0], botReq[j][i - 1] + topReq[i][i]);
                } else {
                    Node topLca = lca(topST, topRelation[i], topRelation[j - 1]);
                    Node botLca = lca(botST, botRelation[j], botRelation[j + 1]);
                    int challenger = dp[j][1];
                    if (j + 1 <= i - 1) {
                        challenger += botReq[j + 1][i - 1] - botLca.depth;
                    }
                    challenger += topRelation[i].depth - topLca.depth;
                    dp[i][0] = Math.min(dp[i][0], challenger);
                }
            }

            for (int j = 0; j <= i - 1; j++) {
                if (j == 0) {
                    dp[i][1] = Math.min(dp[i][1], topReq[j][i - 1] + botReq[i][i]);
                } else {
                    Node botLca = lca(botST, botRelation[i], botRelation[j - 1]);
                    Node topLca = lca(topST, topRelation[j], topRelation[j + 1]);
                    int challenger = dp[j][0];
                    if (j + 1 <= i - 1) {
                        challenger += topReq[j + 1][i - 1] - topLca.depth;
                    }
                    challenger += botRelation[i].depth - botLca.depth;
                    dp[i][1] = Math.min(dp[i][1], challenger);
                }
            }

        }

        int ans = (int) 1e8;
        for (int i = 0; i < n; i++) {
            int topC = dp[i][0];
            if(i < n - 1){
                Node lca = lca(topST, topRelation[i + 1], topRelation[i]);
                topC += topReq[i + 1][n - 1] - lca.depth;
            }
            int botC = dp[i][1];
            if(i < n - 1){
                Node lca = lca(botST, botRelation[i + 1], botRelation[i]);
                botC += botReq[i + 1][n - 1] - lca.depth;
            }
            ans = Math.min(topC, ans);
            ans = Math.min(botC, ans);
        }

        out.println(a + b - 2 - ans);
    }

    public Node lca(SparseTable<Node> st, Node a, Node b) {
        return st.query(Math.min(a.dfn, b.dfn), Math.max(a.dfn, b.dfn));
    }

    public void dfs(Node root, List<Node> trace) {
        if (root.p == null) {
            root.depth = 0;
        } else {
            root.depth = root.p.depth + 1;
        }
        root.dfn = trace.size();
        trace.add(root);
        for (Node node : root.next) {
            dfs(node, trace);
            trace.add(root);
        }
    }
}

class Node {
    Node p;
    int depth;
    int dfn;
    List<Node> next = new ArrayList<>();
    int id;
}