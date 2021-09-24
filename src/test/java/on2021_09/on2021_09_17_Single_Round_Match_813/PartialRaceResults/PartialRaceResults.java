package on2021_09.on2021_09_17_Single_Round_Match_813.PartialRaceResults;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartialRaceResults {
    Map<Character, Node> map = new HashMap<>();

    Node get(char c) {
        return map.computeIfAbsent(c, x -> {
            Node ans = new Node();
            ans.s = c;
            return ans;
        });
    }

    public String reconstruct(String[] memories) {
        for (String s : memories) {
            Node b = get(s.charAt(0));
            Node a = get(s.charAt(2));
            Node c = get(s.charAt(3));
            b.adj.add(a);
            c.adj.add(b);
        }

        for(Node node : map.values()){
            dfs(node);
        }
        if(circle){
            return "";
        }
        StringBuilder ans = new StringBuilder();
        for(Node node : trace){
            ans.append(node.s);
        }
        return ans.toString();
    }

    List<Node> trace = new ArrayList<>();
    boolean circle = false;

    public void dfs(Node root) {
        if (root.visited) {
            if (root.inq) {
                circle = true;
            }
            return;
        }
        root.visited = root.inq = true;
        for (Node node : root.adj) {
            dfs(node);
        }
        root.inq = false;
        trace.add(root);
    }
}

class Node {
    boolean visited;
    boolean inq;
    List<Node> adj = new ArrayList<>();
    char s;
}
