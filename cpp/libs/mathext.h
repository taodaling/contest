#ifndef MATHEXT_H
#define MATHEXT_H

namespace mathext {
template <class T, class Exp>
T Pow(const T &x, const Exp &exp) {
  if (exp == 0) {
    return T(1);
  }
  T ans = Pow(x, exp / 2);
  ans = ans * ans;
  if (exp % 2) {
    ans = ans * x;
  }
  return ans;
}
}  // namespace math

#endif