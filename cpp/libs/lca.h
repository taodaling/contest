#ifndef LCA_H
#define LCA_H

#include "bits.h"
#include "common.h"

namespace lca {
using namespace bits;
class Lca {
 private:
  vector<int> parent;
  vector<unsigned> pre_order;
  vector<unsigned> I;
  vector<int> head;
  vector<unsigned> A;
  unsigned time;
  unsigned highestOneBit(unsigned x) const {
    if(x == 0){
      return 0;
    }
    return 1u << (31 - __builtin_clz(x));
  }
  unsigned lowestOneBit(unsigned x){
    return x & -x;
  }
  void dfs1(const vector<vector<int>> &tree, int u, int p) {
    parent[u] = p;
    I[u] = pre_order[u] = time++;
    for (int v : tree[u]) {
      if (v == p) continue;
      dfs1(tree, v, u);
      if (lowestOneBit(I[u]) < lowestOneBit(I[v])) {
        I[u] = I[v];
      }
    }
    head[I[u]] = u;
  }

  void dfs2(const vector<vector<int>> &tree, int u, int p, unsigned up) {
    A[u] = up | lowestOneBit(I[u]);
    for (int v : tree[u]) {
      if (v == p) continue;
      dfs2(tree, v, u, A[u]);
    }
  }

  int enter_into_strip(int x, int hz) const {
    if (lowestOneBit(I[x]) == hz) return x;
    int hw = highestOneBit(A[x] & (hz - 1));
    return parent[head[(I[x] & -hw) | hw]];
  }

 public:
  // lca in O(1)
  int operator()(int x, int y) const {
    int hb = I[x] == I[y] ? lowestOneBit(I[x]) : highestOneBit(I[x] ^ I[y]);
    int hz = lowestOneBit(A[x] & A[y] & -hb);
    int ex = enter_into_strip(x, hz);
    int ey = enter_into_strip(y, hz);
    return pre_order[ex] < pre_order[ey] ? ex : ey;
  }

  void init(const vector<vector<int>> &tree, int root) {
    int n = tree.size();
    parent.resize(n);
    pre_order.resize(n);
    I.resize(n);
    head.resize(n);
    A.resize(n);

    time = 0;
    dfs1(tree, root, -1);
    dfs2(tree, root, -1, 0);
  }
};
}  // namespace lca

#endif