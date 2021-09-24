package on2021_08.on2021_08_03_AtCoder___AtCoder_Beginner_Contest_209.E___Shiritori;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class EShiritori {
    int charset = 'z' - 'a' + 1;

    public int encode(char c) {
        if (c >= 'a' && c <= 'z') {
            return c - 'a';
        }
        return c - 'A' + charset;
    }

    public int encodePrefix(String s) {
        return (encode(s.charAt(0)) * charset * 2 + encode(s.charAt(1))) * charset * 2 + encode(s.charAt(2));
    }

    public int encodeSuffix(String s) {
        return (encode(s.charAt(s.length() - 3)) * charset * 2 + encode(s.charAt(s.length() - 2))) * charset * 2 + encode(s.charAt(s.length() - 1));
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = charset * charset * charset * 8;
        Node[] nodes = new Node[m];
        for (int i = 0; i < m; i++) {
            nodes[i] = new Node();
        }
        String[] words = new String[n];
        for (int i = 0; i < n; i++) {
            words[i] = in.rs();
        }
        for (String s : words) {
            int pref = encodePrefix(s);
            int suf = encodeSuffix(s);
            nodes[suf].adj.add(nodes[pref]);
            nodes[pref].outdeg++;
        }
        Deque<Node> dq = new ArrayDeque<>();
        for (Node node : nodes) {
            if (node.outdeg == 0) {
                node.state = -1;
                dq.addLast(node);
            }
        }
        while (!dq.isEmpty()) {
            Node head = dq.removeFirst();
            for (Node node : head.adj) {
                if (head.state == -1 && node.state != 1) {
                    node.state = 1;
                    dq.addLast(node);
                }
                if (head.state == 1) {
                    node.outdeg--;
                    if (node.outdeg == 0) {
                        node.state = -1;
                        dq.addLast(node);
                    }
                }
            }
        }

        for (String s : words) {
            int to = encodeSuffix(s);
            int curState = -nodes[to].state;
            if(curState == -1){
                out.println("Aoki");
            }else if(curState == 1){
                out.println("Takahashi");
            }else{
                out.println("Draw");
            }
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    int outdeg;
    int state;
}

