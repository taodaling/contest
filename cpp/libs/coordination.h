#ifndef COORDINATON_H
#define COORDINATON_H

#include "common.h"
#include "matrix.h"

namespace coordination {

const double PREC = 1e-12;

template <class T>
inline bool IsZero(T x) {
  return -PREC <= x && x <= PREC;
}

template <class T, int D>
T Length2(array<T, D> &vec) {
  T ans = 0;
  for (T x : vec) {
    ans += x * x;
  }
  return ans;
}

template <int D>
double Length(array<double, D> &vec) {
  return sqrt(Length2(vec));
}

template <int D>
long double Length(array<long double, D> &vec) {
  return sqrtl(Length2(vec));
}

template <class T, int D>
void Normalize(array<T, D> &vec) {
  T len = Length(vec);
  assert(len > 0);
  for (int i = 0; i < D; i++) {
    vec[i] /= len;
  }
}

template <class T, int D>
void AnyOrthogonalVector(array<T, D> &z, array<T, D> &x) {
  fill(x.begin(), x.end(), 0);
  for (int i = 0; i < D; i++) {
    if (IsZero(z[i])) {
      x[i] = 1;
      return;
    }
  }
  x[0] = 1;
  x[1] = z[0] / -z[1];
}

template <class T>
void CrossProduct3D(array<T, 3> &a, array<T, 3> &b, array<T, 3> &ans) {
  // i j k
  // a0 a1 a2
  // b0 b1 b2
  ans[0] = a[1] * b[2] - a[2] * b[1];
  ans[1] = -(a[0] * b[2] - a[2] * b[0]);
  ans[2] = a[0] * b[1] - a[1] * b[0];
}

template <class T>
void AnyMatrix3D(array<array<T, 3>, 3> &mat, array<T, 3> &z) {
  mat[2] = z;
  AnyOrthogonalVector(mat[2], mat[0]);
  Normalize(mat[2]);
  Normalize(mat[0]);
  CrossProduct3D(mat[2], mat[0], mat[1]);
}
}  // namespace coordination

#endif