#ifndef GEO2_H
#define GEO2_H

#include "common.h"

namespace geo2 {
template <class T>
struct Point {
  T x, y;
  Point<T> operator+(const Point &p) const { return {x + p.x, y + p.y}; }
  Point<T> operator-(const Point &p) const { return {x - p.x, y - p.y}; }
  Point<T> operator*(T d) { return {x * d, y * d}; }
  Point<T> operator/(T d) { return {x / d, y / d}; }
  T square() { return x * x + y * y; }
  double abs() { return sqrt(square()); }
};
template <class T>
bool operator==(const Point<T> &a, const Point<T> &b) {
  return a.x == b.x && a.y == b.y;
}
template <class T>
bool operator!=(const Point<T> &a, const Point<T> &b) {
  return !(a == b);
}
template <class T>
ostream &operator<<(ostream &os, const Point<T> &pt) {
  os << "(" << pt.x << "," << pt.y ")";
  return os;
}

template <class T>
int Sign(T x) {
  return (T(0) < x) - (x < T(0));
}

template <class T>
Point<T> Translate(const Point<T> &v, const Point<T> &p) {
  return p + v;
}

template <class T>
Point<T> Scale(const Point<T> &c, T factor, const Point<T> &p) {
  return c + (p - c) * factor;
}

template <class T>
Point<T> Rotate(const Point<T> &pt, double a) {
  double c = std::cos(a);
  double s = std::sin(a);
  return {pt.x * c - pt.y * s, pt.x * s + pt.y * c};
}

template <class T>
Point<T> Perp(const Point<T> &pt) {
  return {-pt.y, pt.x};
}

template <class T>
Point<T> LinearTransformTo(const Point<T> &p, const Point<T> &q,
                           const Point<T> &fp, const Point<T> &fq,
                           const Point<T> &req) {
  return fp + (req - p) * (fq - fp) / (q - p);
}

template <class T>
T Dot(const Point<T> &a, const Point<T> &b) {
  return a.x * b.x + a.y * b.y;
}

template <class T>
bool IsPerp(const Point<T> &a, const Point<T> &b) {
  return Dot(a, b) == 0;
}

template <class T>
double Angle(const Point<T> &a, const Point<T> &b) {
  return std::acos(clamp(Dot(a, b) / a.abs() / b.abs()), -1.0, 1.0);
}

template <class T>
T Cross(const Point<T> &a, const Point<T> &b) {
  return a.x * b.y - a.y * b.x;
}

template <class T>
int Orient(const Point<T> &a, const Point<T> &b, const Point<T> &c) {
  return Sign(Cross(b - a, c - a));
}

template <class T>
bool InAngle(const Point<T> &a, const Point<T> &b, const Point<T> &c,
             const Point<T> &p) {
  assert(Orient(a, b, c) != 0);
  if (Orient(a, b, c) < 0) {
    swap(b, c);
  }
  return Orient(a, b, p) >= 0 && Orient(a, c, p) <= 0;
}

template <class T>
double OrientedAngle(const Point<T> &a, const Point<T> &b, const Point<T> &c) {
  if (Orient(a, b, c) >= 0) {
    return Angle(b - a, c - a);
  } else {
    return 2 * PI - Angle(b - a, c - a);
  }
}

template <class T>
bool IsConvex(const vector<Point<T>> &p) {
  bool hasPos = false, hasNeg = false;
  for (int i = 0, n = p.size(); i < n; i++) {
    int o = Orient(p[i], p[(i + 1) % n], p[(i + 2) % n]);
    if (o > 0) hasPos = true;
    if (o < 0) hasNeg = true;
  }
  return !(hasPos & hasNeg);
}
}  // namespace geo2

#endif