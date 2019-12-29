package on2019_12.on2019_12_29_Good_Bye_2019.E__Divide_Points;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerList;
import template.primitve.generated.LongObjectHashMap;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class EDividePoints {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].x = in.readInt();
            nodes[i].y = in.readInt();
        }

        IntegerList ans = new IntegerList(n);
        int[][] cnts = new int[2][2];
        while (true) {
            SequenceUtils.deepFill(cnts, 0);
            for (int i = 0; i < n; i++) {
                cnts[nodes[i].x & 1][nodes[i].y & 1]++;
            }
            if (cnts[0][0] == n) {
                for (int i = 0; i < n; i++) {
                    nodes[i].x /= 2;
                    nodes[i].y /= 2;
                }
                continue;
            }
            if (cnts[0][1] == n) {
                for (int i = 0; i < n; i++) {
                    nodes[i].x /= 2;
                    nodes[i].y = (nodes[i].y - 1) / 2;
                }
                continue;
            }
            if (cnts[1][0] == n) {
                for (int i = 0; i < n; i++) {
                    nodes[i].x = (nodes[i].x - 1) / 2;
                    nodes[i].y = nodes[i].y / 2;
                }
                continue;
            }
            if (cnts[1][1] == n) {
                for (int i = 0; i < n; i++) {
                    nodes[i].x = (nodes[i].x - 1) / 2;
                    nodes[i].y = (nodes[i].y - 1) / 2;
                }
                continue;
            }

            if (cnts[0][0] + cnts[1][1] == n) {
                for (int i = 0; i < n; i++) {
                    if((nodes[i].x & 1) == 0 && (nodes[i].y & 1) == 0){
                        ans.add(i);
                    }
                }
                break;
            }
            if(cnts[1][0] + cnts[0][1] == n){
                for (int i = 0; i < n; i++) {
                    if((nodes[i].x & 1) == 1 && (nodes[i].y & 1) == 0){
                        ans.add(i);
                    }
                }
                break;
            }
            for (int i = 0; i < n; i++) {
                if((nodes[i].x & 1) == (nodes[i].y & 1)){
                    ans.add(i);
                }
            }
            break;
        }

        out.println(ans.size());
        for(int i = 0; i < ans.size(); i++){
            out.append(ans.get(i) + 1).append(' ');
        }
    }
}

class Node {
    int x;
    int y;
}
