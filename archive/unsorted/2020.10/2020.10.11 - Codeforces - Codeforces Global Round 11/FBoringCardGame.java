package contest;

import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SequenceUtils;

import java.io.PrintWriter;
import java.util.*;

public class FBoringCardGame {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] belong = new int[1 + 6 * n];
        for (int i = 0; i < 6 * n; i++) {
            belong[in.readInt()] = 1;
        }

        IntegerArrayList[] collect = new IntegerArrayList[]{new IntegerArrayList(), new IntegerArrayList()};

        List<Node> nodes = new ArrayList<>();
        for (int i = 1; i <= 6 * n; i++) {
            collect[belong[i]].add(i);
            if (collect[belong[i]].size() == 3) {
                Node node = new Node();
                node.cards = collect[belong[i]].toArray();
                node.belong = belong[i];
                collect[belong[i]].clear();
                nodes.add(node);
            }
        }

        nodes.sort(Comparator.comparingInt(x -> x.cards[2] - x.cards[0]));
        TreeMap<Integer, Node> map = new TreeMap<>();
        for (Node node : nodes) {
            int l = node.cards[0];
            int r = node.cards[2];
            while (true) {
                Map.Entry<Integer, Node> ceil = map.ceilingEntry(l);
                if (ceil != null && l <= ceil.getKey() && r >= ceil.getKey()) {
                    map.remove(ceil.getKey());
                    node.adj.add(ceil.getValue());
                } else {
                    break;
                }
            }
            map.put(l, node);
        }

        Deque<Node>[][] dqs = new Deque[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                dqs[i][j] = new ArrayDeque<>();
            }
        }

        for (Node node : map.values()) {
            dqs[node.belong][Integer.signum(node.adj.size())].addLast(node);
        }

        List<Node> seq = new ArrayList<>();
        //1 start
        int now = 1;
        for (int i = 0; i < 2 * n; i++) {
            now ^= 1;
            Node pop = null;
            for (int j = 1; j >= 0; j--) {
                if (dqs[now][j].isEmpty()) {
                    continue;
                }
                pop = dqs[now][j].removeFirst();
                for (Node node : pop.adj) {
                    dqs[node.belong][Integer.signum(node.adj.size())].addLast(node);
                }
                break;
            }
            assert pop != null;
            seq.add(pop);
        }
        SequenceUtils.reverse(seq);
        for(Node pop : seq){
            for(int x : pop.cards){
                out.print(x);
                out.append(' ');
            }
            out.println();
        }
    }


}

class Node {
    List<Node> adj = new ArrayList<>();
    int[] cards;
    int belong;

    @Override
    public String toString() {
        return belong + ":[" + cards[0] + "," + cards[2] + "]";
    }
}
