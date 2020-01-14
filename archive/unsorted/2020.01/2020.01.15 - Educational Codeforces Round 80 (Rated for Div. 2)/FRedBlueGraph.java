package contest;

import template.graph.MinCostMaxFlow;
import template.io.FastInput;
import template.io.FastOutput;

public class FRedBlueGraph {
    int n1;
    int n2;
    int idOfLeft(int i){
        return i;
    }
    int idOfRight(int i){
        return n1 + i;
    }
    int idOfSrc(){
        return n1 + n2;
    }
    int idOfDst(){
        return idOfSrc() + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n1 = in.readInt();
        n2 = in.readInt();
        int m = in.readInt();
        int r = in.readInt();
        int b = in.readInt();

        MinCostMaxFlow mcmf = new MinCostMaxFlow(idOfDst() + 1, idOfSrc(), idOfDst());
        
    }
}
