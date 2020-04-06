#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;

  int inf = 1e8;
  vector<vector<int>> dists(3, vector<int>(n, inf));
  vector<vector<pair<int, int>>> g(n);
  for (int i = 0; i < m; i++) {
    int a, b, c;
    in >> a >> b >> c;
    a--;
    b--;
    c--;
    g[a].emplace_back(b, c);
    //g[b].emplace_back(a, c);
  }

  deque<pair<int, int>> dq;
  for (int i = 0; i < 3; i++) {
    dists[i][0] = 0;
    dq.emplace_back(0, i);
  }

  while (!dq.empty()) {
    pair<int, int> head = dq.front();
    dq.pop_front();
    for (auto &e : g[X(head)]) {
      if (Y(e) == Y(head)) {
        continue;
      }
      if (dists[Y(e)][X(e)] <= dists[Y(head)][X(head)] + 1) {
        continue;
      }
      dists[Y(e)][X(e)] = dists[Y(head)][X(head)] + 1;
      dq.push_back(e);
    }
  }

  int ans = inf;
  for(int i = 0; i < 3; i++){
    ans = min(ans, dists[i][n - 1]);
  }

  if(ans == inf){
    ans = -1;
  }
  out << ans;
}

RUN_ONCE