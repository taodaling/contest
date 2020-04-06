#ifndef TWO_SAT_H
#define TWO_SAT_H

#include "common.h"

namespace two_sat {
class TwoSat {
 private:
  vector<vector<int>> edges;
  vector<bool> values;
  vector<int> sets;
  vector<int> dfns;
  vector<int> lows;
  vector<bool> instk;
  deque<int> dq;
  int n;
  int dfn;

  friend ostream& operator<<(ostream& os, TwoSat ts);

 public:
  TwoSat(int n) {
    values.resize(n * 2);
    sets.resize(n * 2);
    edges.resize(n * 2);
    dfns.resize(n * 2);
    lows.resize(n * 2);
    instk.resize(n * 2);
    this->n = n;
    dfn = 0;
  }

  void clear() {
    for (auto& e : edges) {
      e.clear();
    }
  }

  bool valueOf(int x) { return values[sets[elementId(x)]]; }

  bool solve(bool fetchValue) {
    fill(values.begin(), values.end(), false);
    fill(dfns.begin(), dfns.end(), 0);
    dq.clear();
    dfn = 0;

    for (int i = 0; i < sets.size(); i++) {
      tarjan(i);
    }
    for (int i = 0; i < n; i++) {
      if (sets[elementId(i)] == sets[negateElementId(i)]) {
        return false;
      }
    }

    if (!fetchValue) {
      return true;
    }

    fill(dfns.begin(), dfns.end(), 0);
    for (int i = 0; i < sets.size(); i++) {
      assign(i);
    }
    return true;
  }

  void assign(int root) {
    if (dfns[root] > 0) {
      return;
    }
    dfns[root] = 1;
    for (int node : edges[root]) {
      assign(node);
    }
    if (sets[root] == root) {
      values[root] = !values[sets[negate(root)]];
    }
  }

  void tarjan(int root) {
    if (dfns[root] > 0) {
      return;
    }
    lows[root] = dfns[root] = ++dfn;
    instk[root] = true;
    dq.push_back(root);
    for (int node : edges[root]) {
      tarjan(node);
      if (instk[node] && lows[node] < lows[root]) {
        lows[root] = lows[node];
      }
    }
    if (lows[root] == dfns[root]) {
      int last;
      do {
        last = dq.back();
        dq.pop_back();
        sets[last] = root;
        instk[last] = false;
      } while (last != root);
    }
  }

  int elementId(int x) { return x << 1; }

  int negateElementId(int x) { return (x << 1) | 1; }

  int negate(int x) { return x ^ 1; }

  void deduce(int a, int b) {
    edges[a].push_back(b);
    edges[negate(b)].push_back(negate(a));
  }

  void orOp(int a, int b) { deduce(negate(a), b); }

  void isTrue(int a) { edges[negate(a)].push_back(a); }

  void isFalse(int a) { edges[a].push_back(negate(a)); }

  void same(int a, int b) {
    deduce(a, b);
    deduce(b, a);
  }

  void xorOp(int a, int b) { same(a, negate(b)); }

  void atLeastOneIsFalse(int a, int b) { deduce(a, negate(b)); }

  void atLeastOneIsTrue(int a, int b) { orOp(a, b); }
};

ostream& operator<<(ostream& os, TwoSat ts) {
  int n = ts.n;
  os << "State {";
  for (int i = 0; i < n; i++) {
    os << (int)ts.valueOf(i) << ' ';
  }
  os << "}";
  os << endl;
  os << "Edge {" << endl;
  for (auto& e : ts.edges) {
    for (int next : e) {
      os << next << " ";
    }
    os << endl;
  }
  os << "}" << endl;
  return os;
}
}  // namespace two_sat

#endif