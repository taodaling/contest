#ifndef EXCRT_H
#define EXCRT_H

#include "gcd.h"
#include "modular.h"

namespace number_theory {
using namespace modular;
template <class T>
class ExtCRT {
 private:
  T _r;
  T _m;
  bool _valid;

 public:
  ExtCRT() {
    _r = 0;
    _m = 1;
    _valid = true;
  }

  T operator()() { return _r; }

  bool isValid() { return _valid; }

  /**
   * Add a new condition: x % m = r
   */
  bool add(T r, T m) {
    T m1 = _m;
    T x1 = _r;
    T m2 = m;
    T x2 = ((r % m) + m) % m;
    T a, b;
    T g = gcd::Extgcd(m1, m2, a, b);
    if ((x2 - x1) % g != 0) {
      return _valid = false;
    }
    _m = m1 / g * m2;
    _r =
        Mod(Modmul(Modmul(Mod(a, _m), Mod((x2 - x1) / g, _m), _m), m1, _m) + x1,
            _m);
    return true;
  }
};
}  // namespace number_theory

#endif