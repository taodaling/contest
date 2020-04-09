package contest;

import template.graph.StableMarriage;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Travel {
    HashMap<String, Integer> boyNames = new HashMap<>(800);
    HashMap<String, Integer> girlNames = new HashMap<>(800);
    List<String> boys = new ArrayList<>(800);
    List<String> girls = new ArrayList<>(800);


    public int readBoyIndex(FastInput in) {
        String name = in.readString();
        Integer index = boyNames.get(name);
        if (index == null) {
            index = boys.size();
            boys.add(name);
            boyNames.put(name, index);
        }
        return index;
    }

    public int readGirlIndex(FastInput in) {
        String name = in.readString();
        Integer index = girlNames.get(name);
        if (index == null) {
            index = girls.size();
            girls.add(name);
            girlNames.put(name, index);
        }
        return index;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] boyPref = new int[n][n];
        int[][] girlPref = new int[n][n];
        for (int i = 0; i < n; i++) {
            int index = readBoyIndex(in);
            for (int j = 0; j < n; j++) {
                boyPref[index][readGirlIndex(in)] = n - j;
            }
        }
        for (int i = 0; i < n; i++) {
            int index = readGirlIndex(in);
            for (int j = 0; j < n; j++) {
                girlPref[index][readBoyIndex(in)] = n - j;
            }
        }

        out.println("YES");
        StableMarriage sm = new StableMarriage(boyPref, girlPref);
        for(int i = 0; i < n; i++){
            out.append(boys.get(i)).append(' ').append(girls.get(sm.wifeOf(i))).println();
        }
    }
}
