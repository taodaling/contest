#include "../../libs/common.h"

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  vector<vector<int>> edges(n, vector<int>(n));

  int inf = 1e8;
  vector<int> dists(n, inf);
  int s, t;
  in >> s >> t;
  s--;
  t--;

  for (int i = 0; i < m; i++) {
    int a, b;
    in >> a >> b;
    a--;
    b--;
    edges[a][b] = edges[b][a] = i + 1;
  }

  deque<int> dq;
  dq.push_back(s);
  dists[s] = 0;
  while (!dq.empty()) {
    int head = dq.front();
    dq.pop_front();
    for (int i = 0; i < n; i++) {
      if (!edges[head][i] || dists[i] <= dists[head] + 1) {
        continue;
      }
      dists[i] = dists[head] + 1;
      dq.push_back(i);
    }
  }

  // not connected
  if (dists[t] == inf) {
    out << m << endl;
    for (int i = 0; i < m; i++) {
      out << 1 << ' ' << i + 1 << endl;
    }
    return;
  }

  out << dists[t] << endl;
  int d = dists[t];
  vector<vector<int>> classify(d);
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (edges[i][j] && dists[i] == dists[j] - 1 && dists[i] < d) {
        classify[dists[i]].push_back(edges[i][j]);
      }
    }
  }

  for(int i = 0; i < d; i++){
    out << classify[i].size() << ' ';
    for(int e : classify[i]){
      out << e << ' ';
    }
    out << endl;
  }
}

RUN_ONCE