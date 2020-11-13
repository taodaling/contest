package contest;

import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.HashData;
import template.rand.HashOnTree;
import template.rand.IntRangeHash;

import java.util.List;

public class TreeIsomorphismI {
    int threshold = (int) 2e5;
    HashData hd1 = new HashData(threshold, (int)1e9 + 7, 31);
    HashData hd2 = new HashData(threshold, (int)1e9 + 7, 71);
//HashData hd1 = new HashData(threshold);
//    HashData hd2 = new HashData(threshold);
    IntRangeHash rh = new IntRangeHash(hd1, hd2, threshold);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        List<UndirectedEdge>[] t1 = Graph.createGraph(n);
        List<UndirectedEdge>[] t2 = Graph.createGraph(n);
        for(int i = 0; i < n - 1; i++){
            Graph.addUndirectedEdge(t1, in.readInt() - 1, in.readInt() - 1);
        }
        for(int i = 0; i < n - 1; i++){
            Graph.addUndirectedEdge(t2, in.readInt() - 1, in.readInt() - 1);
        }
        HashOnTree hot1 = new HashOnTree(t1, rh, 0);
        HashOnTree hot2 = new HashOnTree(t2, rh, 0);
        if(hot1.hashOnRoot(0) == hot2.hashOnRoot(0)){
            out.println("YES");
        }else{
            out.println("NO");
        }
    }
}
