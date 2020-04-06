#include "../../libs/common.h"

vector<vector<int>> g;
vector<bool> instk;
vector<bool> visited;

bool DfsForCircle(int root) {
  if (visited[root]) {
    return instk[root];
  }
  visited[root] = instk[root] = true;

  for (int node : g[root]) {
    if (DfsForCircle(node)) {
      return true;
    }
  }

  instk[root] = false;
  return false;
}

vector<int> trace;
void TopologicSort(int root) {
  if (visited[root]) {
    return;
  }
  visited[root] = true;
  for (int node : g[root]) {
    TopologicSort(node);
  }
  trace.push_back(root);
}

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  g.resize(n);
  instk.resize(n);
  visited.resize(n);

  for (int i = 0; i < m; i++) {
    int a, b;
    in >> a >> b;
    a--;
    b--;
    g[b].push_back(a);
  }

  for (int i = 0; i < n; i++) {
    if (DfsForCircle(i)) {
      out << "No solution";
      return;
    }
  }

  fill(visited.begin(), visited.end(), false);
  trace.reserve(n);
  for (int i = 0; i < n; i++) {
    TopologicSort(i);
  }

  vector<int> ranks(n);
  for(int i = 0; i < n; i++){
    ranks[trace[i]] = i;
  }
  for(int i = 0; i < n; i++){
    out << ranks[i] + 1 << ' ';
  }
}

RUN_ONCE