#ifndef CHT_H
#define CHT_H

#include "common.h"

namespace cht {
template <class T>
struct Line {
  T a;
  T b;
  T operator()(T x) { return a * x + b; }
};

template <class T>
class ConvexHullTrick {
 private:
  map<T, Line> indexByA;
  map<T, Line> indexByLeft;

 public:
  void add(const Line<T> &cand){
    while(true){
      auto &floor = indexByA.
    }
  }
};
}  // namespace cht

#endif