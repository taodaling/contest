#include "../../libs/common.h"
#include "../../libs/debug.h"

int dirs[][2]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

struct Edge {
  int a;
  int b;
  // 0 for equal, 1 a -> b, 2 b -> a
  int type;

  Edge() {}
  Edge(int aa, int ab, int atype) : a(aa), b(ab), type(atype) {}

  int other(int x) { return a == x ? b : a; }
  int getTo() { return type == 1 ? b : a; }
};

vector<Edge> edges;
vector<vector<int>> g;
vector<int> req;
int n;
vector<int> instk;
vector<bool> visited;
vector<int> values;
vector<int> sorted;

void collect(int root, vector<int> &ans) {
  if (visited[root]) {
    return;
  }
  visited[root] = true;
  ans.push_back(root);
  for (int e : g[root]) {
    if (edges[e].type == 0) {
      collect(edges[e].other(root), ans);
    }
  }
}

void TopoSort(int root) {
  if (visited[root]) {
    return;
  }
  vector<int> all;
  collect(root, all);
  dbg(root, all);
  for (int node : all) {
    for (int e : g[node]) {
      if (edges[e].type == 0 || edges[e].getTo() == node) {
        continue;
      }
      TopoSort(edges[e].getTo());
    }
  }

  int ans = sorted.size() + 1;
  for (int node : all) {
    values[node] = ans;
    sorted.push_back(node);
  }
}

bool CheckCircle(int root, int depth) {
  if (visited[root]) {
    if (instk[root] && depth != instk[root]) {
      return true;
    }
    return false;
  }
  instk[root] = depth;
  visited[root] = true;

  for (int e : g[root]) {
    if (edges[e].type) {
      continue;
    }
    CheckCircle(edges[e].other(root), depth);
  }

  for (int e : g[root]) {
    if (!edges[e].type) {
      continue;
    }
    if (edges[e].getTo() == root) {
      continue;
    }
    if (CheckCircle(edges[e].getTo(), depth + 1)) {
      return true;
    }
  }

  instk[root] = 0;
  return false;
}

int CellId(int i, int j) { return i * n + j; }

bool Dfs(int j) {
  if (j == -1) {
    for (int i = 0; i < g.size(); i++) {
      int cnt = 0;
      for (auto &e : g[i]) {
        if (edges[e].type == 0) {
        }
        if (edges[e].type == 1 && edges[e].b == i) {
          cnt++;
        }
        if (edges[e].type == 2 && edges[e].a == i) {
          cnt++;
        }
      }
      if (cnt != req[i]) {
        return false;
      }
    }

    fill(visited.begin(), visited.end(), false);
    fill(instk.begin(), instk.end(), 0);
    for (int i = 0; i < g.size(); i++) {
      if (CheckCircle(i, 1)) {
        return false;
      }
    }
    return true;
  }

  for (int i = 0; i < 3; i++) {
    edges[j].type = i;
    if (Dfs(j - 1)) {
      return true;
    }
  }
  return false;
}

void solve(int testId, istream &in, ostream &out) {
  in >> n;
  g.resize(n * n);
  req.resize(n * n);
  visited.resize(n * n);
  instk.resize(n * n);
  values.resize(n * n);

  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      in >> req[CellId(i, j)];
    }
  }

  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      for (int k = 0; k < n; k++) {
        for (int t = 0; t < n; t++) {
          if (make_tuple(i, j) >= make_tuple(k, t) ||
              abs(i - k) + abs(j - t) != 1) {
            continue;
          }
          g[CellId(i, j)].push_back(edges.size());
          g[CellId(k, t)].push_back(edges.size());
          edges.emplace_back(CellId(i, j), CellId(k, t), 0);
        }
      }
    }
  }

  if (!Dfs(edges.size() - 1)) {
    out << "NO SOLUTION";
    return;
  }

  fill(visited.begin(), visited.end(), false);
  for (int i = 0; i < g.size(); i++) {
    TopoSort(i);
  }
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      out << values[CellId(i, j)] << ' ';
    }
    out << endl;
  }
}

RUN_ONCE