#ifndef BINARY_SEARCH_H
#define BINARY_SEARCH_H

#include "common.h"
#include "decimal.h"

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

template <class T>
T BinarySearch(T l, T r, const function<bool(T)> &func, T absolute,
               T relative) {
  assert(l <= r);
  while (r - l > absolute) {
    if ((r < 0 && (r - l) < -r * relative) ||
        (l > 0 && (r - l) < l * relative)) {
      break;
    }

    T mid = (l + r) / 2.0;
    if (func(mid)) {
      r = mid;
    } else {
      l = mid;
    }
  }
  return (l + r) / 2.0;
}

/**
 * Used to find the maximum value of a lower convex.
 * Assume f(-inf)<...<f(ans)=f(ans+1)=...=f(ans+k)>...>f(inf)
 */
template <class T>
T TernarySearch(T l, T r, const function<T(T)> &func, T absolute, T relative) {
  while (r - l > absolute) {
    if (r < 0 && (r - l) / -r <= relative || l > 0 && (r - l) / l <= relative) {
      break;
    }
    T dist = (r - l) / 3;
    T ml = l + dist;
    T mr = r - dist;
    if (func(ml) < func(mr)) {
      l = ml;
    } else {
      r = mr;
    }
  }
  return (l + r) / 2;
}

/**
 * Used to find the maximum value of a Upper convex.
 * Assume f(-inf)<...<f(ans)=f(ans+1)=...=f(ans+k)>...>f(inf)
 */
template <class T>
T TernarySearch(T l, T r, const function<T(T)> &func) {
  while (r - l > 2) {
    T ml = l + decimal::FloorDiv(r - l, 3);
    T mr = r - decimal::CeilDiv(r - l, 3);
    if (func(ml) < func(mr)) {
      l = ml;
    } else {
      r = mr;
    }
  }
  while (l < r) {
    if (func(l) >= func(r)) {
      r--;
    } else {
      l++;
    }
  }
  return l;
}
}  // namespace binary_search

#endif