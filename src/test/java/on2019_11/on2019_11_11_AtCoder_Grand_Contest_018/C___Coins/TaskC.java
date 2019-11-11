package on2019_11.on2019_11_11_AtCoder_Grand_Contest_018.C___Coins;



import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import template.FastInput;
import template.FastOutput;
import template.PermutationUtils;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt();
        int y = in.readInt();
        int z = in.readInt();

        int n = x + y + z;
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            for (int j = 0; j < 3; j++) {
                nodes[i].vals[j] = in.readInt();
            }
            nodes[i].id = i;
        }

        List<Node> xlist = Arrays.asList(nodes).subList(0, x);
        List<Node> yList = Arrays.asList(nodes).subList(x, x + y);
        List<Node> zList = Arrays.asList(nodes).subList(x + y, x + y + z);;

        List<Node>[] lists = new List[] {xlist, yList, zList};
        Sets[] sets = new Sets[3];
        for (int i = 0; i < 3; i++) {
            sets[i] = new Sets();
            for (int j = 0; j < 3; j++) {
                int finalJ = j;
                int finalI = i;
                sets[i].sets[j] = new TreeSet<>((a, b) -> {
                    int ans = Long.compare(a.vals[finalJ] - a.vals[finalI], b.vals[finalJ] - b.vals[finalI]);
                    if (ans == 0) {
                        ans = a.id - b.id;
                    }
                    return ans;
                });
            }
            for(Node node : lists[i]){
                sets[i].add(node);
            }
        }

        // optimize
        int[][] allPerms = PermutationUtils.generateAllPermutations(3).toArray(new int[0][]);
        boolean optimize = true;
        Node[] changes = new Node[3];
        while (optimize) {
            optimize = false;

            for (int[] p : allPerms) {
                long profit = 0;
                for (int i = 0; i < 3; i++) {
                    Node last = sets[i].sets[p[i]].last();
                    changes[i] = last;
                    profit += last.vals[p[i]] - last.vals[i];

                }
                if (profit > 0) {
                    optimize = true;
                    for (int i = 0; i < 3; i++) {
                        sets[i].remove(changes[i]);
                        sets[p[i]].add(changes[i]);
                    }
                }
            }
        }

        long ans = 0;
        for (int i = 0; i < 3; i++) {
            for (Node node : sets[i].sets[0]) {
                ans += node.vals[i];
            }
        }

        out.println(ans);
    }
}


class Sets {
    TreeSet<Node>[] sets = new TreeSet[3];

    void remove(Node node) {
        for (TreeSet<Node> set : sets) {
            set.remove(node);
        }
    }

    void add(Node node) {
        for (TreeSet<Node> set : sets) {
            set.add(node);
        }
    }
}


class Node {
    long[] vals = new long[3];
    int id;
}
