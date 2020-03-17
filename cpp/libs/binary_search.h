#ifndef BINARY_SEARCH_H
#define BINARY_SEARCH_H

#include "common.h"

namespace binary_search {
template <class T>
T BinarySearch(T l, T r, const function<bool(T)> &func) {
  assert(l <= r);
  while (l < r) {
    T mid = (l + r) >> 1;
    if (func(mid)) {
      r = mid;
    } else {
      l = mid + 1;
    }
  }
  return l;
}
}  // namespace binary_search

#endif