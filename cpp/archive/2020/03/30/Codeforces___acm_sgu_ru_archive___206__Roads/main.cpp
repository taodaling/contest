#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/km_algo.h"
struct Edge {
  int a, b;
  int w;
  int other(int x) { return a == x ? b : a; }
};

ostream &operator<<(ostream &os, const Edge &e) {
  os << "(" << e.a << "," << e.b << "," << e.w << ")";
  return os;
}

vector<vector<int>> g;
vector<Edge> edges;
vector<int> trace;

bool Collect(int root, int p, int target) {
  trace.push_back(p);
  if (root == target) {
    return true;
  }
  for (int e : g[root]) {
    if (e == p) {
      continue;
    }
    if (Collect(edges[e].other(root), e, target)) {
      return true;
    }
  }
  trace.pop_back();
  return false;
}

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  edges.resize(m);
  g.resize(n);
  vector<vector<int>> mat(m, vector<int>(m));
  for (int i = 0; i < m; i++) {
    in >> edges[i].a >> edges[i].b >> edges[i].w;
    edges[i].a--;
    edges[i].b--;

    if (i < n - 1) {
      g[edges[i].a].push_back(i);
      g[edges[i].b].push_back(i);
    }
  }

  for (int i = n - 1; i < m; i++) {
    trace.clear();
    assert(Collect(edges[i].a, -1, edges[i].b));
    for (int j = 1; j < trace.size(); j++) {
      int e = trace[j];
      mat[e][i] = max(mat[e][i], edges[e].w - edges[i].w);
    }
  }

  dbg(mat);
  km_algo::KMAlgo<int> km(mat);
  km.solve();

  dbg(edges);
  dbg(km);

  for (int i = 0; i < n - 1; i++) {
    out << edges[i].w - km.getXL(i) << endl;
  }
  for (int i = n - 1; i < m; i++) {
    out << edges[i].w + km.getYL(i) << endl;
  }
}

RUN_ONCE