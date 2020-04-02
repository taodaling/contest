#ifndef SIMPSON_INTEGRAL_H
#define SIMPSON_INTEGRAL_H

#include "common.h"

namespace simpson_integeral {
template <class T>
class SimpsonIntegral {
  T _eps;
  function<T(T)> _function;
  T simpson(T l, T r) {
    return (r - l) / 6 *
           (_function(l) + 4 * _function((l + r) / 2) + _function(r));
  }

  T integral(T l, T r, T totalArea) {
    T m = (l + r) / 2;
    T lArea = simpson(l, m);
    T rArea = simpson(m, r);
    if (abs(lArea + rArea - totalArea) <= 15 * _eps) {
      return lArea + rArea + (lArea + rArea - totalArea) / 15;
    }
    return integral(l, m, lArea) + integral(m, r, rArea);
  }

 public:
  SimpsonIntegral(T eps, function<T(T)> function)
      : _eps(eps), _function(function) {}

  T integral(T l, T r) { return integral(l, r, simpson(l, r)); }
};
}  // namespace simpson_integeral

#endif