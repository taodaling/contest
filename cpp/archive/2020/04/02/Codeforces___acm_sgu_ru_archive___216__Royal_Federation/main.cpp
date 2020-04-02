#include "../../libs/common.h"
#include "../../libs/debug.h"

vector<vector<int>> g;
vector<int> subtree;
vector<int> colors;
vector<int> coloredBy;
int b;
int order;

void DfsForSize(int root, int p) {
  subtree[root] = 1;
  for (int node : g[root]) {
    if (node == p) {
      continue;
    }
    DfsForSize(node, root);
    subtree[root] += subtree[node];
  }
}

int DfsForCentroid(int root, int p, int total) {
  int size = total - subtree[root];
  for (int node : g[root]) {
    if (node == p) {
      continue;
    }
    size = max(size, subtree[node]);
    int ans = DfsForCentroid(node, root, total);
    if (ans >= 0) {
      return ans;
    }
  }
  if (size * 2 <= total) {
    return root;
  }
  return -1;
}

void DfsForPaint(int root, int p, int c, int by) {
  assert(colors[root] == 0);
  colors[root] = c;
  coloredBy[root] = by;
  for (int node : g[root]) {
    if (node == p) {
      continue;
    }
    DfsForPaint(node, root, c, by);
  }
}

void Erase(vector<int> &vec, int x) {
  for (int i = vec.size() - 1; i >= 0; i--) {
    if (vec[i] == x) {
      vec.erase(vec.begin() + i);
      return;
    }
  }
}

vector<int> pend;

void Dac(int root) {
  DfsForSize(root, -1);
  if (subtree[root] <= 3 * b) {
    DfsForPaint(root, -1, ++order, root);
    return;
  }

  root = DfsForCentroid(root, -1, subtree[root]);
  DfsForSize(root, -1);
  sort(g[root].begin(), g[root].end(),
       [&](auto a, auto b) { return subtree[a] < subtree[b]; });

  int sum = subtree[root];
  if (subtree[g[root].back()] >= b) {
    while (g[root].size()) {
      int back = g[root].back();
      if (subtree[back] < b || sum - subtree[back] < b) {
        break;
      }
      sum -= subtree[back];
      g[root].pop_back();
      Erase(g[back], root);
      Dac(back);
    }
  }

  if (sum <= 3 * b || g[root].size() && subtree[g[root].back()] >= b) {
    Dac(root);
    return;
  }

  pend.clear();
  sum = 0;
  for (int node : g[root]) {
    pend.push_back(node);
    sum += subtree[node];
    if (sum >= b) {
      ++order;
      for (int x : pend) {
        DfsForPaint(x, root, order, root);
      }
      pend.clear();
      sum = 0;
    }
  }
  if (pend.size() > 0) {
    for (int x : pend) {
      DfsForPaint(x, root, order, root);
    }
    pend.clear();
  }
  colors[root] = order;
  coloredBy[root] = root;
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n >> b;
  g.resize(n);
  colors.resize(n);
  subtree.resize(n);
  coloredBy.resize(n);
  for (int i = 0; i < n - 1; i++) {
    int a, b;
    in >> a >> b;
    a--;
    b--;
    g[a].push_back(b);
    g[b].push_back(a);
  }

  Dac(0);
  dbg(colors, order);
  dbg(coloredBy);
  out << order << endl;
  vector<int> centers(order + 1, -1);
  for (int i = 0; i < n; i++) {
    centers[colors[i]] = max(centers[colors[i]], i);
    out << colors[i] << ' ';
  }
  out << endl;
  for (int i = 1; i <= order; i++) {
    out << coloredBy[centers[i]] + 1 << ' ';
  }
}

RUN_ONCE