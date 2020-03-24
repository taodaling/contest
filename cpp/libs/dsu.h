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

template <int N>
class XorDeltaDSU {
 private:
  int _p[N];
  int _rank[N];
  int _delta[N];

 public:
  XorDeltaDSU() { reset(); }

  void reset() {
    for (int i = 0; i < N; i++) {
      _p[i] = i;
      _rank[i] = 0;
      _delta[i] = 0;
    }
  }

  int find(int a) {
    if (_p[a] == _p[_p[a]]) {
      return _p[a];
    }
    find(_p[a]);
    _delta[a] ^= _delta[_p[a]];
    return _p[a] = find(_p[a]);
  }

  /**
   * return a - b, you should ensure a and b belong to same set
   */
  int delta(int a, int b) {
    find(a);
    find(b);
    return _delta[a] ^ _delta[b];
  }

  /**
   * a - b = delta
   */
  void merge(int a, int b, int d) {
    find(a);
    find(b);
    d = d ^ _delta[a] ^ _delta[b];
    a = find(a);
    b = find(b);
    if (a == b) {
      return;
    }
    if (_rank[a] == _rank[b]) {
      _rank[a]++;
    }
    if (_rank[a] > _rank[b]) {
      _p[b] = a;
      _delta[b] = d;
    } else {
      _p[a] = b;
      _delta[a] = d;
    }
  }
};
}  // namespace dsu

#endif