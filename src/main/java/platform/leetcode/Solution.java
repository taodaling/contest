package platform.leetcode;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Solution {
    public static void main(String[] args) {
    }

    public int secondMinimum(int n, int[][] edges, int time, int change) {
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }
        for (int[] e : edges) {
            int a = e[0];
            int b = e[1];
            nodes[a].adj.add(nodes[b]);
            nodes[b].adj.add(nodes[a]);
        }
        insert(nodes[0], 0);
        while (!pq.isEmpty()) {
            Event e = pq.remove();
            Node root = e.node;
            long t = e.time;
            long nextTrigger = nextTrigger(t + time, change);
            for(Node node : root.adj){
                insert(node, nextTrigger);
            }
        }
        long ans = nodes[n - 1].times.get(1);
        return (int)ans;
    }

    public long nextTrigger(long cur, long change) {
        if (cur / change % 2 == 0) {
            return cur;
        }
        return (cur + change - 1) / change * change;
    }

    PriorityQueue<Event> pq = new PriorityQueue<>(Comparator.comparingLong(x -> x.time));

    public void insert(Node root, long t) {
        if (root.insert(t)) {
            Event e = new Event();
            e.node = root;
            e.time = t;
            pq.add(e);
        }
    }
}

class Event {
    Node node;
    long time;
}

class Node {
    List<Node> adj = new ArrayList<>();
    List<Long> times = new ArrayList<>(2);

    public boolean insert(long x) {
        if (times.isEmpty() || times.get(times.size() - 1) != x) {
            if (times.size() < 2) {
                times.add(x);
                return true;
            }
        }
        return false;
    }
}