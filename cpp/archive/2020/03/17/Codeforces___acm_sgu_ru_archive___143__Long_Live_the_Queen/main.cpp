#include "../../libs/common.h"
#include "../../libs/debug.h"

vector<int> weights;
vector<vector<int>> dp;
vector<vector<int>> g;

void dfs(int root, int p) {
  dp[root][1] = weights[root];
  for (int node : g[root]) {
    if (node == p) {
      continue;
    }
    dfs(node, root);
    dp[root][1] += max(0, dp[node][1]);
    dp[root][0] = max(dp[root][0], dp[node][0]);
  }
  dp[root][0] = max(dp[root][0], dp[root][1]);
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  weights.resize(n);
  g.resize(n);
  dp.resize(n, vector<int>(2, -1e9));
  for (int i = 0; i < n; i++) {
    in >> weights[i];
  }

  for (int i = 0; i < n - 1; i++) {
    int u, v;
    in >> u >> v;
    u--;
    v--;
    g[u].push_back(v);
    g[v].push_back(u);
  }


  dfs(0, -1);
  dbg(dp);
  out << dp[0][0];
}

RUN_ONCE