#ifndef DSU_H
#define DSU_H

#include "common.h"

namespace dsu {
template <int N>
class DSU {
 private:
  int p[N];
  int rank[N];

 public:
  DSU() { reset(); }
  void reset() {
    for (int i = 0; i < N; i++) {
      p[i] = i;
      rank[i] = 0;
    }
  }

  int find(int a) { return p[a] == p[p[a]] ? p[a] : (p[a] = find(p[a])); }

  void merge(int a, int b) {
    a = find(a);
    b = find(b);
    if (a == b) {
      return;
    }
    if (rank[a] == rank[b]) {
      rank[a]++;
    }
    if (rank[a] > rank[b]) {
      p[b] = a;
    } else {
      p[a] = b;
    }
  }
};
}  // namespace dsu

#endif