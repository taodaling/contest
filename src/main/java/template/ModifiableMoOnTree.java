package template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ModifiableMoOnTree {
    private static class Node {
        List<Node> next = new ArrayList(2);
        int id;
        int close;
        int open;
        boolean added;
        int dfn;

        @Override
        public String toString() {
            return "" + id;
        }
    }

    Node[] nodes;

    public ModifiableMoOnTree(int n) {
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
    }

    public void addEdge(int a, int b) {
        nodes[a].next.add(nodes[b]);
        nodes[b].next.add(nodes[a]);
    }

    private boolean preHandled = false;

    private void preHandle() {
        if (preHandled) {
            return;
        }
        preHandled = true;
        eulerTrace = new Node[nodes.length * 2];
        lcaTrace = new Node[nodes.length * 2 - 1];
        dfs(nodes[0], null);
        sparseTable = new SparseTable<Node>(lcaTrace, lcaTraceTail, (a, b) -> a.dfn < b.dfn ? a : b);
    }

    Node[] eulerTrace;
    int eulerTraceTail = 0;
    Node[] lcaTrace;
    int lcaTraceTail = 0;
    SparseTable<Node> sparseTable;

    private void dfs(Node root, Node father) {
        root.open = eulerTraceTail;
        eulerTrace[eulerTraceTail++] = root;
        root.dfn = lcaTraceTail;
        lcaTrace[lcaTraceTail++] = root;
        for (Node node : root.next) {
            if (node == father) {
                continue;
            }
            dfs(node, root);
            lcaTrace[lcaTraceTail++] = root;
        }
        root.close = eulerTraceTail;
        eulerTrace[eulerTraceTail++] = root;
    }

    public void solve(Query[] queries, Modification[] modifications, Assistant assistant, int now) {
        preHandle();

        final int blockSize = Math.max((int) Math.pow(nodes.length, 2.0 / 3), 1);

        for (Query q : queries) {
            Node u = nodes[q.u];
            Node v = nodes[q.v];
            if (u.open > v.open) {
                Node tmp = u;
                u = v;
                v = tmp;
            }
            if (u.close <= v.open) {
                q.l = u.close;
                q.r = v.open;
            } else {
                q.l = v.close;
                q.r = u.close - 1;
            }
            q.lca = sparseTable.query(Math.min(u.dfn, v.dfn), Math.max(u.dfn, v.dfn)).id;
        }

        Arrays.sort(queries, new Comparator<Query>() {
            @Override
            public int compare(Query a, Query b) {
                int r = a.l / blockSize - b.l / blockSize;
                if (r == 0) {
                    r = a.version / blockSize - b.version / blockSize;
                }
                if (r == 0) {
                    r = a.r - b.r;
                }
                return r;
            }
        });

        int l = 0;
        int r = -1;
        for (Node node : nodes) {
            node.added = false;
        }


        for (Query q : queries) {
            while (now < q.version) {
                Modification m = modifications[now];
                Node x = nodes[m.x];
                if (x.added) {
                    assistant.remove(x.id);
                }
                assistant.apply(m);
                if (x.added) {
                    assistant.add(x.id);
                }
                now++;
            }
            while (now > q.version) {
                now--;
                Modification m = modifications[now];
                Node x = nodes[m.x];
                if (x.added) {
                    assistant.remove(x.id);
                }
                assistant.revoke(m);
                if (x.added) {
                    assistant.add(x.id);
                }
            }
            while (r < q.r) {
                r++;
                Node x = eulerTrace[r];
                if (x.added) {
                    assistant.remove(x.id);
                } else {
                    assistant.add(x.id);
                }
                x.added = !x.added;
            }
            while (l > q.l) {
                l--;
                Node x = eulerTrace[l];
                if (x.added) {
                    assistant.remove(x.id);
                } else {
                    assistant.add(x.id);
                }
                x.added = !x.added;
            }
            while (r > q.r) {
                Node x = eulerTrace[r];
                if (x.added) {
                    assistant.remove(x.id);
                } else {
                    assistant.add(x.id);
                }
                x.added = !x.added;
                r--;
            }
            while (l < q.l) {
                Node x = eulerTrace[l];
                if (x.added) {
                    assistant.remove(x.id);
                } else {
                    assistant.add(x.id);
                }
                x.added = !x.added;
                l++;
            }

            Node lca = nodes[q.lca];
            if (lca.added) {
                assistant.remove(q.lca);
            } else {
                assistant.add(q.lca);
            }
            lca.added = !lca.added;
            assistant.query(q);
            if (lca.added) {
                assistant.remove(q.lca);
            } else {
                assistant.add(q.lca);
            }
            lca.added = !lca.added;
        }

    }

    public static class Query {
        int l;
        int r;
        int version;
        int u;
        int v;
        int answer;
        int lca;

        @Override
        public String toString() {
            return "(" + u + "," + v + ")[" + version + "]";
        }
    }

    public static class Modification {
        int x;
        int from;
        int to;

        @Override
        public String toString() {
            return x + "[" + from + "->" + to + "]";
        }
    }

    public interface Assistant {
        void apply(Modification m);

        void revoke(Modification m);

        void add(int i);

        void remove(int i);

        void query(Query q);
    }
}
