#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  vector<int> deg(n + 1);
  for(int i = 0; i < m; i++){
    int u, v;
    in >> u >> v;
    deg[u]++;
    deg[v]++;
  }

  ll sum = 0;
  for(int i = 1; i <= n; i++){
    sum += (ll)deg[i] * deg[i];
  }

  out << sum;
}

RUN_ONCE