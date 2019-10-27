package on2019_10.on2019_10_26_Codeforces_Round__596__Div__1__based_on_Technocup_2020_Elimination_Round_2_.D___Tree_Factory;



import template.FastInput;
import template.FastOutput;
import template.LeftSideTree;

import java.util.*;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i < n; i++) {
            nodes[i].p = nodes[in.readInt()];
            nodes[i].p.next.add(nodes[i]);
            nodes[i].p.childNum++;
        }
        dfs(nodes[0], 0);
        handle(nodes[0]);
        outputNo(nodes[0], out);
        out.println();
        out.println(seq.size());
        for(Node node : seq){
            out.append(node.id).append(' ');
        }
    }

    List<Node> seq = new ArrayList<>(1000000);

    public void outputNo(Node root, FastOutput os){
        os.append(root.id).append(' ');
        if(root.childNum == 0){
            return;
        }
        outputNo(root.set.first(), os);
    }

    public void handle(Node root) {
        if(root.childNum == 0){
            return;
        }

        while(root.childNum > 1){
            Node min = root.set.pollFirst();
            Node second = root.set.pollLast();
            min.set.add(second);
            min.childNum += 1;
            if(min.childNum == 1){
                min.nearest = second.nearest + 1;
            }
            root.childNum--;
            root.set.add(min);
            seq.add(second);
        }

        handle(root.set.first());
    }

    public void dfs(Node root, int depth) {
        if (root.next.size() == 0) {
            root.nearest = 0;
        } else {
            root.nearest = (int) 1e9;
        }
        for (Node node : root.next) {
            dfs(node, depth + 1);
            root.nearest = Math.min(node.nearest + 1, root.nearest);
            root.set.add(node);
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>();
    int childNum;
    TreeSet<Node> set = new TreeSet<>(sortByNearest);
    Node p;
    int nearest;
    int id;
    static Comparator<Node> sortByNearest = (a, b) -> a.nearest == b.nearest ? a.id - b.id : a.nearest - b.nearest;

    @Override
    public String toString() {
        return "" + id;
    }
}
