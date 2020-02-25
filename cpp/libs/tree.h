#ifndef TREE_H
#define TREE_H

#include "common.h"
#include "lca.h"

namespace tree {
using path = pair<int, int>;
inline bool Under(int child, int parent, const Lca &lca) {
  return lca(child, parent) == parent;
}
inline bool Intersect(const path &a, const path &b, const Lca &lca) {
  int l1 = lca(a.first, a.second);
  int l2 = lca(b.first, b.second);
  return (Under(b.first, l1, lca) || Under(b.second, l1, lca)) &&
         (Under(a.first, l2, lca) || Under(a.second, l2, lca));
}
inline bool Include(const path &p, int v, const Lca &lca) {
  int lca1 = lca(p.first, p.second);
  int lca2 = lca(p.first, v);
  int lca3 = lca(p.second, v);
  return lca1 == v || ((lca2 == v) != (lca3 == v));
}
};  // namespace tree

#endif