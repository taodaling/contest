package on2021_05.on2021_05_25_AtCoder___Dwango_Programming_Contest_6th.D___Arrangement;



import com.sun.org.apache.xpath.internal.functions.FuncBoolean;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.PermutationUtils;

import java.util.*;
import java.util.stream.IntStream;

public class DArrangement {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();

        Node[] nodes = new Node[n];
        for(int i = 0; i < n; i++){
            nodes[i] = new Node();
        }
        for (int i = 0; i < n; i++) {
            nodes[i].val = i;
            nodes[i].to = nodes[in.ri() - 1];
            nodes[i].to.deg++;
        }
        if (n == 2) {
            out.println(-1);
            return;
        }
        TreeSet<Node> unhandled = new TreeSet<>(Comparator.comparingInt(x -> x.val));
        unhandled.addAll(Arrays.asList(nodes));
        List<Node> seq = new ArrayList<>();
        Node forbidden = null;
        for (int i = 0; i < n - 3; i++) {
            int remain = n - i;
            Node prefer = null;
            for (Node node : unhandled) {
                if (forbidden == node) {
                    continue;
                }
                prefer = node;
                break;
            }

            if (unhandled.contains(prefer.to) && prefer.to.deg == remain - 1) {
                prefer = prefer.to;
            }

            assert forbidden != prefer;
            seq.add(prefer);
            unhandled.remove(prefer);
            prefer.to.deg--;
            forbidden = prefer.to;
        }

        int[] remain = unhandled.stream().mapToInt(x -> x.val).toArray();
        do{
            boolean ok = true;
            if(forbidden == nodes[remain[0]]){
                ok = false;
            }
            for(int i = 0; i < 2; i++){
                if(nodes[remain[i]].to == nodes[remain[i + 1]]){
                    ok = false;
                }
            }
            if(ok){
                break;
            }
        }while(PermutationUtils.nextPermutation(remain));

        for(Node node : seq){
            out.append(node.val + 1).append(' ');
        }
        for(int x : remain){
            out.append(x + 1).append(' ');
        }
    }
}

class Node {
    int val;
    Node to;
    int deg;
}
