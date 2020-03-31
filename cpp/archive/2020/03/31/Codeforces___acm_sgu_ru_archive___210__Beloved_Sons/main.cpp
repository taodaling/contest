#include "../../libs/common.h"
#include "../../libs/km_algo.h"
#include "../../libs/debug.h"

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<int> prefs(n);
  for (int i = 0; i < n; i++) {
    int x;
    in >> x;
    prefs[i] = x * x;
  }
  vector<vector<int>> mat(n, vector<int>(n));
  for (int i = 0; i < n; i++) {
    int k;
    in >> k;
    while (k-- > 0) {
      int x;
      in >> x;
      x--;
      mat[i][x] = prefs[i];
    }
  }

  km_algo::KMAlgo<int> km(mat);
  km.solve();

  dbg(mat, km);
  for (int i = 0; i < n; i++) {
    int match = km.getXMatch(i);
    if(mat[i][match] == 0){
      out << 0;
    }else{
      out << match + 1;
    }
    out << ' ';
  }
}

RUN_ONCE