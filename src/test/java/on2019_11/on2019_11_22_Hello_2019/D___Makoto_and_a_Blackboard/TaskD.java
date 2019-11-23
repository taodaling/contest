package on2019_11.on2019_11_22_Hello_2019.D___Makoto_and_a_Blackboard;



import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.InverseNumber;
import template.math.ModMatrix;
import template.math.Modular;
import template.math.Power;

import java.util.ArrayList;
import java.util.List;

public class TaskD {
    Modular mod = new Modular(1e9 + 7);
    Power pow = new Power(mod);
    InverseNumber inverseNumber = new InverseNumber(100, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        int k = in.readInt();

        if(n == 1){
            out.println(1);
            return;
        }

        List<Node> factors = new ArrayList<>();
        for(long i = 2; i * i <= n; i++){
            if(n % i != 0){
                continue;
            }
            Node node = new Node();
            node.val = i;
            node.cnt = 0;
            while(n % i == 0){
                n /= i;
                node.cnt++;
            }
            node.prob = dp(node.cnt, k);
            factors.add(node);
        }

        if(n > 1){
            Node node = new Node();
            node.val = n;
            node.cnt = 1;
            node.prob =  dp(node.cnt, k);
            factors.add(node);
        }

        int exp = count(factors.toArray(new Node[0]), 0, 1, 1);
        out.println(exp);
    }

    public int count(Node[] nodes, int i, long val, int prob) {
        if(i == nodes.length){
            return mod.mul(mod.valueOf(val), prob);
        }
        int exp = 0;
        for(int j = 0; j <= nodes[i].cnt; j++){
            exp = mod.plus(exp, count(nodes, i + 1, val, mod.mul(prob, nodes[i].prob[j])));
            val *= nodes[i].val;
        }
        return exp;
    }

    public int[] dp(int cnt, int k){
        int[] last = new int[cnt + 1];
        int[] next = new int[cnt + 1];
        last[cnt] = 1;
        for(int i = 1; i <= k; i++){
            int prefix = 0;
            for(int j = cnt; j >= 0; j--){
                prefix = mod.plus(prefix, mod.mul(last[j], inverseNumber.inverse(j + 1)));
                next[j] = prefix;
            }
            int[] tmp = last;
            last = next;
            next = tmp;
        }
        return last;
    }
}

class Node{
    long val;
    int cnt;
    int[] prob;
}
