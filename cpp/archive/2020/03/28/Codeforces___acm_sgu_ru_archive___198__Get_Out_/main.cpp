#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/geo2.h"

#define PREC 1e-8
using namespace geo2;

using pt = Point<double>;

struct Circle {
  pt center;
  double r;
};

ostream &operator<<(ostream &os, const Circle &c) {
  os << c.center << ":" << c.r;
  return os;
}

vector<vector<int>> g;
vector<Circle> circles;
vector<int> dq;
pt boat;
vector<bool> visited;
vector<bool> instk;
vector<pt> polygon;

bool Dfs(int root, int p) {
  if (visited[root]) {
    if (instk[root]) {
      polygon.clear();
      for (int i = dq.size() - 1;; i--) {
        polygon.push_back(circles[dq[i]].center);
        if (dq[i] == root) {
          break;
        }
      }
      if (geo2::InPolygon(polygon, boat)) {
        return true;
      }
    }
    return false;
  }
  visited[root] = true;
  instk[root] = true;
  dq.push_back(root);
  for (int node : g[root]) {
    if (node == p) {
      continue;
    }
    if (Dfs(node, root)) {
      return true;
    }
  }
  instk[root] = false;
  dq.pop_back();
  return false;
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  circles.resize(n);
  g.resize(n);
  dq.reserve(n);
  visited.resize(n);
  polygon.reserve(n);
  instk.resize(n);
  for (int i = 0; i < n; i++) {
    in >> circles[i].center >> circles[i].r;
  }

  double boatRadius;
  in >> boat >> boatRadius;

  for (auto &c : circles) {
    c.r += boatRadius;
  }

  dbg(circles);

  for (int i = 0; i < n; i++) {
    for (int j = 0; j < i; j++) {
      double dist = (circles[i].center - circles[j].center).abs();
      if (Sign(dist - (circles[i].r + circles[j].r)) < 0) {
        g[i].push_back(j);
        g[j].push_back(i);
      }
    }
  }

  dbg(g);

  for (int i = 0; i < n; i++) {
    if (Dfs(i, -1)) {
      out << "NO";
      return;
    }
  }

  out << "YES";
}

RUN_ONCE