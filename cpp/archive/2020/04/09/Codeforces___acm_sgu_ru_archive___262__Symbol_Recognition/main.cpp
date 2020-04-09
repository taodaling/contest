#include "../../libs/bits.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"

using grid = bitset<100>;

void solve(int testId, istream &in, ostream &out) {
  int n, m, k;
  in >> n >> m >> k;
  vector<grid> pics(k);
  for (int t = 0; t < k; t++) {
    for (int i = 0; i < n * m; i++) {
      char c;
      in >> c;
      pics[t][i] = (c - '0');
    }
  }
  dbg(pics);
  vector<grid> xors;
  for (int i = 0; i < k; i++) {
    for (int j = 0; j < i; j++) {
      xors.push_back(pics[i] ^ pics[j]);
    }
  }

  int p = xors.size();
  int mask = (1 << p) - 1;

  vector<int> occupy(n * m);
  for (int i = 0; i < n * m; i++) {
    for (int j = 0; j < xors.size(); j++) {
      if (xors[j][i]) {
        occupy[i] = bits::SetBit(occupy[i], j);
      }
    }
  }

  dbg(xors);
  dbg(occupy);

  int inf = 1e9;
  vector<vector<int>> dp(n * m + 1, vector<int>(mask + 1, inf));
  vector<vector<int>> prev(n * m + 1, vector<int>(mask + 1, -1));

  dp[0][0] = 0;
  for (int i = 0; i < n * m; i++) {
    for (int j = 0; j <= mask; j++) {
      if (dp[i + 1][j] > dp[i][j]) {
        dp[i + 1][j] = dp[i][j];
        prev[i + 1][j] = j;
      }
      if (dp[i + 1][j | occupy[i]] > dp[i][j] + 1) {
        dp[i + 1][j | occupy[i]] = dp[i][j] + 1;
        prev[i + 1][j | occupy[i]] = j;
      }
    }
  }

  dbg(dp, prev);
  vector<int> pick;
  {
    for (int i = n * m, j = mask; i >= 1; i--) {
      if (prev[i][j] != j) {
        pick.push_back(i - 1);
      }
      j = prev[i][j];
    }
  }

  vector<vector<int>> ans(n, vector<int>(m));
  for (int x : pick) {
    ans[x / m][x % m] = 1;
  }

  out << pick.size() << endl;
  for (auto &row : ans) {
    for (int x : row) {
      out << x;
    }
    out << endl;
  }
}

RUN_ONCE