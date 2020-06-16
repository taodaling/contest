package contest;

import template.datastructure.Range2DequeAdapter;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;
import template.rand.Randomized;

import java.util.Arrays;
import java.util.PriorityQueue;

public class SpaciousOffice {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Room[] rooms = new Room[n];
        for (int i = 0; i < n; i++) {
            rooms[i] = new Room();
            rooms[i].id = i;
            rooms[i].area = in.readInt();
        }
        Arrays.sort(rooms, (a, b) -> Integer.compare(a.area, b.area));
        Req[] reqs = new Req[n];
        for (int i = 0; i < n; i++) {
            reqs[i] = new Req();
            reqs[i].l = in.readInt();
            reqs[i].r = in.readInt();
        }
        Req[] originalReqs = reqs.clone();
        Arrays.sort(reqs, (x, y) -> Integer.compare(x.l, y.l));
        Range2DequeAdapter<Req> dq = new Range2DequeAdapter<>(i -> reqs[i], 0, n - 1);
        PriorityQueue<Req> pq = new PriorityQueue<>(n, (x, y) -> Integer.compare(x.r, y.r));
        for (int i = 0; i < n; i++) {
            while (!dq.isEmpty() && dq.peekFirst().l <= rooms[i].area) {
                pq.add(dq.removeFirst());
            }
            if (pq.isEmpty() || pq.peek().r < rooms[i].area) {
                out.println("Let's search for another office.");
                return;
            }
            pq.remove().room = rooms[i];
        }

        edges = new boolean[n][n];
        visited = new boolean[n];
        instk = new boolean[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                if (reqs[j].room.area >= reqs[i].l && reqs[j].room.area <= reqs[i].r) {
                    edges[i][j] = true;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (dfs(i)) {
                out.println("Ask Shiftman for help.");
                return;
            }
        }

        out.println("Perfect!");
        for(int i = 0; i < n; i++){
            out.append(originalReqs[i].room.id + 1).append(' ');
        }
    }

    public boolean dfs(int root) {
        if (visited[root]) {
            return instk[root];
        }
        visited[root] = instk[root] = true;
        for (int i = 0; i < edges.length; i++) {
            if (!edges[root][i]) {
                continue;
            }
            if (dfs(i)) {
                return true;
            }
        }
        instk[root] = false;
        return false;
    }

    boolean[][] edges;
    boolean[] visited;
    boolean[] instk;
}

class Room {
    int area;
    int id;
}

class Req {
    int l;
    int r;
    Room room;
}