package platform.leetcode;


import java.util.ArrayList;
import java.util.List;

class Solution {
    public static void main(String[] args){
        new Solution().numSimilarGroups(new String[]{"omv","ovm"});
    }

    public int numSimilarGroups(String[] strings){
        int n = strings.length;
        DSU dsu = new DSU(n);
        for(int i = 0; i < n; i++){
            for(int j = i + 1; j < n; j++){
                if(similar(strings[i], strings[j])){
                    dsu.merge(i, j);
                }
            }
        }
        int ans = 0;
        for(int i = 0; i < n; i++){
            if(dsu.find(i) == i){
                ans++;
            }
        }
        return ans;
    }

    public boolean similar(String a, String b){
        if(a.length() != b.length()){
            return false;
        }

        List<Integer> diffIndices = new ArrayList(3);
        for(int i = 0; i < a.length() && diffIndices.size() < 3; i++){
            if(a.charAt(i) != b.charAt(i)){
                diffIndices.add(i);
            }
        }

        if(diffIndices.size() > 2 || diffIndices.size() == 1){
            return false;
        }
        if(diffIndices.size() == 0){
            return true;
        }
        int first = diffIndices.get(0);
        int second = diffIndices.get(1);
        return a.charAt(first) == b.charAt(second) && a.charAt(second) == b.charAt(first);
    }
}

interface IntegerBinaryFunction {
    int apply(int a, int b);
}


class DSU{
    int[] p;
    int[] size;

    public DSU(int n){
        p = new int[n];
        size = new int[n];
        for(int i = 0; i < n; i++){
            p[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x){
        return p[x] == p[p[x]] ? p[x] : (p[x] = find(p[x]));
    }

    public void merge(int a, int b){
        a = find(a);
        b = find(b);
        if(a == b){
            return;
        }
        if(size[a] < size[b]){
            int tmp = a;
            a = b;
            b = tmp;
        }
        p[b] = a;
        size[a] += size[b];
    }
}
