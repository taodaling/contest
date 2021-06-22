package platform.leetcode;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public static void main(String[] args){
        new Solution().earliestAndLatest(11, 2, 4);
    }
        int[][][] max;
        int[][][] min;

        int max(int a, int b, int c){
            if(a < c){
                return max(c, b, a);
            }
            if(max[a][b][c] == -1){
                max[a][b][c] = (int)0;
                if(a == c){
                    return max[a][b][c] = 1;
                }
                int n = a + b + c + 2;
                int nextRound = (n + 1) / 2;
                if(a >= b + c + 1){
                    for(int i = 0; i <= c; i++){
                        for(int j = 0; j <= b; j++){
                            int k = nextRound - i - j - 2;
                            max[a][b][c] = Math.max(max[a][b][c], max(k, j, i));
                        }
                    }
                }else{
                    for(int i = 0; i <= c; i++){
                        for(int j = 0; j <= a - c - 1; j++){
                            int k = nextRound - c - j - 2;
                            max[a][b][c] = Math.max(max[a][b][c], max(c - i + j, k, i));
                        }
                    }
                }
            }
            return max[a][b][c];
        }

        int min(int a, int b, int c){
            if(a < c){
                return min(c, b, a);
            }
            if(min[a][b][c] == -1){
                min[a][b][c] = (int)1e9;
                if(a == c){
                    return min[a][b][c] = 1;
                }
                int n = a + b + c + 2;
                int nextRound = (n + 1) / 2;
                if(a >= b + c + 1){
                    for(int i = 0; i <= c; i++){
                        for(int j = 0; j <= b; j++){
                            int k = nextRound - i - j - 2;
                            min[a][b][c] = Math.min(min[a][b][c], min(k, j, i));
                        }
                    }
                }else{
                    for(int i = 0; i <= c; i++){
                        for(int j = 0; j <= a - c - 1; j++){
                            int k = nextRound - c - j - 2;
                            min[a][b][c] = Math.min(min[a][b][c], min(c - i + j, k, i));
                        }
                    }
                }
            }
            return min[a][b][c];
        }

        public int[] earliestAndLatest(int n, int firstPlayer, int secondPlayer) {
            max = new int[n + 1][n + 1][n + 1];
            min = new int[n + 1][n + 1][n + 1];
            for(int i = 0; i <= n; i++){
                for(int j = 0; j <= n; j++){
                    for(int k = 0; k <= n; k++){
                        max[i][j][k] = min[i][j][k] = -1;
                    }
                }
            }

            int a = max(firstPlayer - 1, secondPlayer - firstPlayer - 1, n - secondPlayer);
            int b = min(firstPlayer - 1, secondPlayer - firstPlayer - 1, n - secondPlayer);
            System.out.println(Arrays.deepToString(max));
            System.out.println(Arrays.deepToString(min));
            return new int[]{a, b};
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
