#include "../../libs/common.h"

bool Conflict(pair<int, int> &a, pair<int, int> &b) {
  return X(a) == X(b) || Y(a) == Y(b) || abs(X(a) - X(b)) == abs(Y(a) - Y(b));
}

ll Dfs(vector<pair<int, int>> &vec, int target, int n) {
  if (vec.size() == target) {
    return 1;
  }
  ll ans = 0;
  int i = 0;
  int j = 0;
  if (vec.size()) {
    i = X(vec.back());
    j = Y(vec.back()) + 1;
  }
  for (; i < n; i++) {
    for (; j < n; j++) {
      pair<int, int> ij(i, j);
      bool conflict = false;
      for (auto &p : vec) {
        conflict = conflict || Conflict(p, ij);
      }
      if (conflict) {
        continue;
      }
      vec.push_back(ij);
      ans += Dfs(vec, target, n);
      vec.pop_back();
    }
    j = 0;
  }
  return ans;
}

void solve(int testId, istream &in, ostream &out) {
  int n, k;
  in >> n >> k;
  if (k > n) {
    out << 0;
    return;
  }
  // vector<pair<int, int>> vec;

  // for (int i = 1; i <= 10; i++) {
  //   for (int j = 0; j <= 10; j++) {
  //     out << "ans[" << i << "][" << j << "]=" << Dfs(vec, j, i) << ";" <<
  //     endl;
  //   }
  // }

  vector<vector<ll>> ans(10 + 1, vector<ll>(11));
  ans[1][0] = 1;
  ans[1][1] = 1;
  ans[1][2] = 0;
  ans[1][3] = 0;
  ans[1][4] = 0;
  ans[1][5] = 0;
  ans[1][6] = 0;
  ans[1][7] = 0;
  ans[1][8] = 0;
  ans[1][9] = 0;
  ans[1][10] = 0;
  ans[2][0] = 1;
  ans[2][1] = 4;
  ans[2][2] = 0;
  ans[2][3] = 0;
  ans[2][4] = 0;
  ans[2][5] = 0;
  ans[2][6] = 0;
  ans[2][7] = 0;
  ans[2][8] = 0;
  ans[2][9] = 0;
  ans[2][10] = 0;
  ans[3][0] = 1;
  ans[3][1] = 9;
  ans[3][2] = 8;
  ans[3][3] = 0;
  ans[3][4] = 0;
  ans[3][5] = 0;
  ans[3][6] = 0;
  ans[3][7] = 0;
  ans[3][8] = 0;
  ans[3][9] = 0;
  ans[3][10] = 0;
  ans[4][0] = 1;
  ans[4][1] = 16;
  ans[4][2] = 44;
  ans[4][3] = 24;
  ans[4][4] = 2;
  ans[4][5] = 0;
  ans[4][6] = 0;
  ans[4][7] = 0;
  ans[4][8] = 0;
  ans[4][9] = 0;
  ans[4][10] = 0;
  ans[5][0] = 1;
  ans[5][1] = 25;
  ans[5][2] = 140;
  ans[5][3] = 204;
  ans[5][4] = 82;
  ans[5][5] = 10;
  ans[5][6] = 0;
  ans[5][7] = 0;
  ans[5][8] = 0;
  ans[5][9] = 0;
  ans[5][10] = 0;
  ans[6][0] = 1;
  ans[6][1] = 36;
  ans[6][2] = 340;
  ans[6][3] = 1024;
  ans[6][4] = 982;
  ans[6][5] = 248;
  ans[6][6] = 4;
  ans[6][7] = 0;
  ans[6][8] = 0;
  ans[6][9] = 0;
  ans[6][10] = 0;
  ans[7][0] = 1;
  ans[7][1] = 49;
  ans[7][2] = 700;
  ans[7][3] = 3628;
  ans[7][4] = 7002;
  ans[7][5] = 4618;
  ans[7][6] = 832;
  ans[7][7] = 40;
  ans[7][8] = 0;
  ans[7][9] = 0;
  ans[7][10] = 0;
  ans[8][0] = 1;
  ans[8][1] = 64;
  ans[8][2] = 1288;
  ans[8][3] = 10320;
  ans[8][4] = 34568;
  ans[8][5] = 46736;
  ans[8][6] = 22708;
  ans[8][7] = 3192;
  ans[8][8] = 92;
  ans[8][9] = 0;
  ans[8][10] = 0;
  ans[9][0] = 1;
  ans[9][1] = 81;
  ans[9][2] = 2184;
  ans[9][3] = 25096;
  ans[9][4] = 131248;
  ans[9][5] = 310496;
  ans[9][6] = 312956;
  ans[9][7] = 119180;
  ans[9][8] = 13848;
  ans[9][9] = 352;
  ans[9][10] = 0;
  ans[10][0] = 1;
  ans[10][1] = 100;
  ans[10][2] = 3480;
  ans[10][3] = 54400;
  ans[10][4] = 412596;
  ans[10][5] = 1535440;
  ans[10][6] = 2716096;
  ans[10][7] = 2119176;
  ans[10][8] = 636524;
  ans[10][9] = 56832;
  ans[10][10] = 724;

  out << ans[n][k];
  // ll ans = Dfs(vec, k, n);
  // out << ans;
}

RUN_ONCE