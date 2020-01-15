package template.graph;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * O(E\sqrt{V}) time to find bipartite matching
 */
public class BipartiteMatching {
    LongDinicBeta dinic;
    int l;
    int r;
    List<LongDinicBeta.ChannelImpl> channels;

    private int left(int i) {
        return i;
    }

    private int right(int i) {
        return l + i;
    }

    private int src() {
        return l + r;
    }

    private int sink() {
        return src() + 1;
    }

    public BipartiteMatching(int l, int r, int edgeNum) {
        this.l = l;
        this.r = r;
        dinic = new LongDinicBeta(sink() + 1, edgeNum + l + r, src(), sink());
        for (int i = 0; i < l; i++) {
            dinic.addChannel(src(), left(i)).expand(1);
        }
        for (int i = 0; i < r; i++) {
            dinic.addChannel(right(i), sink()).expand(1);
        }
        channels = new ArrayList<>(edgeNum);
    }

    public void addEdge(int lId, int rId) {
        LongDinicBeta.ChannelImpl channel = dinic.addChannel(left(lId), right(rId));
        channels.add(channel);
        channel.expand(1);
    }

    private void solve() {
        flow = (int) dinic.send(Math.min(l, r));
    }

    public int maxMatch() {
        if (flow == -1) {
            solve();
        }
        return flow;
    }

    boolean[][] minVertexCover;
    boolean[] leftVisited;
    boolean[] rightVisited;

    public boolean[][] minVertexCover() {
        if (minVertexCover == null) {
            minVertexCover = new boolean[][]{new boolean[l], new boolean[r]};
            leftVisited = new boolean[l];
            rightVisited = new boolean[r];
            boolean[] matched = new boolean[r];
            for (int x : match()) {
                if (x == -1) {
                    continue;
                }
                matched[x] = true;
            }
            for (int i = 0; i < r; i++) {
                if (!matched[i]) {
                    dfsRight(i);
                }
            }

            for (int i = 0; i < l; i++) {
                minVertexCover[0][i] = leftVisited[i];
            }
            for (int i = 0; i < r; i++) {
                minVertexCover[1][i] = !rightVisited[i];
            }
        }
        return minVertexCover;
    }

    private void dfsRight(int node) {
        if (rightVisited[node]) {
            return;
        }
        rightVisited[node] = true;
        for (LongDinicBeta.Channel left : dinic.getChannelFrom(right(node))) {
            if (left.getDst() < l) {
                dfsLeft(left.getDst());
            }
        }
    }

    private void dfsLeft(int node) {
        if (match[node] == -1 || leftVisited[node]) {
            return;
        }
        leftVisited[node] = true;
        dfsRight(match[node]);
    }

    int[] match;
    int flow = -1;

    public int[] match() {
        if (match == null) {
            maxMatch();
            match = new int[l];
            Arrays.fill(match, -1);
            for (LongDinicBeta.ChannelImpl channel : channels) {
                if (channel.getFlow() == 1) {
                    match[channel.getSrc()] = channel.getDst() - l;
                }
            }
        }
        return match;
    }
}
