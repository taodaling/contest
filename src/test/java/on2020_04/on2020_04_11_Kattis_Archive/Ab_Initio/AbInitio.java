package on2020_04.on2020_04_11_Kattis_Archive.Ab_Initio;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.rand.HashData;

public class AbInitio {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int V = in.readInt();
        int E = in.readInt();
        int Q = in.readInt();
        Node[] nodes = new Node[V + Q];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node();
        }
        for (int i = 0; i < E; i++) {
            int u = in.readInt();
            int v = in.readInt();
            nodes[u].setOut(v, true);
            nodes[v].setIn(u, true);
        }
        for (int i = 0; i < Q; i++) {
            int t = in.readInt();
            if (t == 1) {
                V++;
                for(int j = 0; j < V; j++){
                    nodes[j].setOut(V - 1, false);
                    nodes[j].setIn(V - 1, false);
                }
            } else if (t == 2) {
                int u = in.readInt();
                int v = in.readInt();
                nodes[u].setOut(v, true);
                nodes[v].setIn(u, true);
            } else if (t == 3) {
                int x = in.readInt();
                for (int j = 0; j < V; j++) {
                    nodes[j].setOut(x, false);
                    nodes[j].setIn(x, false);
                    nodes[x].setIn(j, false);
                    nodes[x].setOut(j, false);
                }
            } else if (t == 4) {
                int u = in.readInt();
                int v = in.readInt();
                nodes[u].setOut(v, false);
                nodes[v].setIn(u, false);
            } else if (t == 5) {
                for (int j = 0; j < V; j++) {
                    nodes[j].transpose();
                }
            } else if (t == 6) {
                for (int j = 0; j < V; j++) {
                    nodes[j].complement();
                }
            }
        }

        out.println(V);
        Modular mod = new Modular(1e9 + 7);
        HashData hd = new HashData(V, mod.getMod(), 7);

        for (int i = 0; i < V; i++) {
            Node node = nodes[i];
            int deg = 0;
            int hash = 0;
            for (int j = 0; j < V; j++) {
                if (j == i || !node.getOut(j)) {
                    continue;
                }
                hash = mod.plus(hash, mod.mul(j, hd.pow[deg]));
                deg++;
            }

            out.append(deg).append(' ').append(hash).println();
        }
    }
}

class Node {
    boolean[] out = new boolean[4000];
    boolean[] in = new boolean[4000];
    boolean complement;

    public void transpose() {
        boolean[] tmp = out;
        out = in;
        in = tmp;
    }

    public void complement() {
        complement = !complement;
    }

    private boolean translate(boolean x) {
        return x != complement;
    }

    public void setOut(int i, boolean e) {
        out[i] = translate(e);
    }

    public void setIn(int i, boolean e) {
        in[i] = translate(e);
    }

    public boolean getOut(int i) {
        return translate(out[i]);
    }

    public boolean getIn(int i) {
        return translate(in[i]);
    }


}
