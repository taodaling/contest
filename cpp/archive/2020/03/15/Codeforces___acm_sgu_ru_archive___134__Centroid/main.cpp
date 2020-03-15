#include "../../libs/common.h"

vector<vector<int>> g;
vector<int> sizes;
vector<int> centroidSizes;

void dfs1(int root, int p) {
  sizes[root] = 1;
  for (int node : g[root]) {
    if (node == p) {
      continue;
    }
    dfs1(node, root);
    sizes[root] += sizes[node];
  }
}

void dfs2(int root, int p, int total) {
  centroidSizes[root] = max(centroidSizes[root], total - sizes[root]);
  for (int node : g[root]) {
    if (node == p) {
      continue;
    }
    centroidSizes[root] = max(centroidSizes[root], sizes[node]);
    dfs2(node, root, total);
  }
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  g.resize(n);
  sizes.resize(n);
  centroidSizes.resize(n);
  for (int i = 0; i < n - 1; i++) {
    int u, v;
    in >> u >> v;
    u--;
    v--;
    g[u].push_back(v);
    g[v].push_back(u);
  }

  dfs1(0, -1);
  dfs2(0, -1, n);

  vector<int> vertices;
  int size = n;

  for (int i = 0; i < n; i++) {
    if (centroidSizes[i] < size) {
      size = centroidSizes[i];
      vertices.clear();
    }
    if (centroidSizes[i] == size) {
      vertices.push_back(i);
    }
  }

  out << size << ' ' << vertices.size() << endl;
  for (auto v : vertices) {
    out << v + 1 << ' ';
  }
}

RUN_ONCE