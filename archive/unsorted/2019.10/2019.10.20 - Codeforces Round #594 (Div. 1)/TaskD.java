package contest;

import template.FastInput;
import template.FastOutput;
import template.TwoSat;

import java.util.ArrayList;
import java.util.List;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Node[] people = new Node[n + 1];
        Node[] cats = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            people[i] = new Node();
            people[i].id = i;
        }
        for (int i = 1; i <= n; i++) {
            cats[i] = new Node();
            cats[i].id = i;
        }
        for (int i = 0; i < m; i++) {
            Node a = people[in.readInt()];
            Node b = cats[in.readInt()];
            a.next.add(b);
            b.next.add(a);
        }

        List<List<Node>[]> groups = new ArrayList<>(n);
        for(int i = 1; i <= n; i++){
            if(people[i].visited){
                continue;
            }
            List<Node>[] lists = new List[]{new ArrayList(), new ArrayList()};
            dfs(people[i], 0, lists);
            groups.add(lists);
        }

        if(groups.size() == 1){
            out.println("No");
            return;
        }

        out.println("Yes");
        List<Node> selectedPeople = new ArrayList<>(n);
        List<Node> selectedCats = new ArrayList<>(n);
        selectedPeople.addAll(groups.get(0)[0]);
        for(int i = 1, until = groups.size(); i < until; i++){
            selectedCats.addAll(groups.get(i)[1]);
        }

        out.append(selectedPeople.size()).append(' ').append(selectedCats.size())
                .append('\n');
        for(Node p : selectedPeople){
            out.append(p.id).append(' ');
        }
        out.println();
        for(Node c : selectedCats){
            out.println(c.id).append(' ');
        }
        out.println();
    }

    public void dfs(Node root, int depth, List<Node>[] lists) {
        if (root.visited) {
            return;
        }
        root.visited = true;
        lists[depth % 2].add(root);
        for(Node node : root.next){
            dfs(node, depth + 1, lists);
        }
    }
}

class Node {
    List<Node> next = new ArrayList<>(2);
    boolean visited;
    int id;
}
