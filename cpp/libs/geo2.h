#ifndef GEO2_H
#define GEO2_H

#include "common.h"

namespace geo2 {

const double PREC = 1e-10;

template <class T>
bool IsZero(T x) {
  return -PREC <= x && x <= PREC;
}

template <class T>
struct Point {
  T x, y;
  Point(T a, T b) : x(a), y(b) {}
  Point() { Point(0, 0); }
  Point<T> conj() const { return {x, -y}; }
  T square() const { return x * x + y * y; }
  double abs() const { return sqrt(square()); }
  bool half() const { return y > 0 || y == 0 && x < 0; }
};

template <class T>
class SortByOrder {
 public:
  bool operator()(const Point<T> &a, const Point<T> &b) {
    return a.x < b.x || a.x == b.x && a.y < b.y;
  }
};

template <class T>
class SortByPolarAngle {
 public:
  bool operator()(const Point<T> &a, const Point<T> &b) {
    if (a.half() != b.half()) {
      return a.half() - b.half();
    }
    return Orient(b, a);
  }
};

template <class T>
Point<T> operator+(const Point<T> &a, const Point<T> &b) {
  return {a.x + b.x, a.y + b.y};
}

template <class T>
Point<T> operator-(const Point<T> &a, const Point<T> &b) {
  return {a.x - b.x, a.y - b.y};
}

template <class T>
Point<T> operator*(const Point<T> &a, T b) {
  return {a.x * b, a.y * b};
}

template <class T>
Point<T> operator*(T b, const Point<T> &a) {
  return {a.x * b, a.y * b};
}

template <class T>
Point<T> operator*(const Point<T> &a, const Point<T> &b) {
  return {a.x * b.x - a.y * b.y, a.x * b.y + a.y * b.x};
}

template <class T>
Point<T> operator/(const Point<T> &a, T b) {
  return {a.x / b, a.y / b};
}

template <class T>
Point<T> operator/(const Point<T> &a, const Point<T> &b) {
  return a * (b.conj() / b.square());
}

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
  os << "(" << pt.x << "," << pt.y << ")";
  return os;
}
template <class T>
istream &operator>>(istream &is, Point<T> &pt) {
  is >> pt.x >> pt.y;
  return is;
}

template <class T>
int Sign(T x) {
  return IsZero(x) ? 0 : x > 0 ? 1 : -1;
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
Point<T> Rotate(const Point<T> &origin, const Point<T> &pt, double a) {
  return Rotate(pt - origin, a) + origin;
}

template <class T>
Point<T> Perp(const Point<T> &pt) {
  return {-pt.y, pt.x};
}

template <class T>
Point<T> LinearTransformTo(const Point<T> &p, const Point<T> &fp,
                           const Point<T> &q, const Point<T> &fq,
                           const Point<T> &req) {
  return fp + (req - p) * (fq - fp) / (q - p);
}

template <class T>
T Dot(const Point<T> &a, const Point<T> &b) {
  return a.x * b.x + a.y * b.y;
}

template <class T>
T Dot(T x1, T y1, T x2, T y2) {
  return x1 * x2 + y1 * y2;
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

/**
 * 判断c是否落在以a与b为直径两端的圆中（包含边界）
 */
template <class T>
bool InDisk(const Point<T> &a, const Point<T> &b, const Point<T> &c) {
  return Sign(Dot(a - c, b - c)) <= 0;
}

template <class T>
bool OnSegment(const Point<T> &a, const Point<T> &b, const Point<T> &c) {
  return Orient(a, b, c) == 0 && InDisk(a, b, c);
}

template <class T>
int Above(const Point<T> &a, const Point<T> &b) {
  return b.y >= a.y ? 1 : 0;
}

template <class T>
bool CrossRay(const Point<T> &a, const Point<T> &p, const Point<T> &q) {
  return (Above(a, q) - Above(a, p)) * Orient(a, p, q) > 0;
}

/**
 * 判断某个顶点是否落在矩形内，1表示矩形内，2表示矩形边缘，0表示矩形外
 */
template <class T>
int InPolygon(const vector<Point<T>> &polygon, const Point<T> &pt) {
  int cross = 0;
  for (int i = 0, n = polygon.size(); i < n; i++) {
    Point<T> cur = polygon[i];
    Point<T> next = polygon[(i + 1) % n];
    if (OnSegment(cur, next, pt)) {
      return 2;
    }
    if (CrossRay(pt, cur, next)) {
      cross++;
    }
  }
  return cross % 2;
}

template <class T>
int InPolygon(const vector<pair<Point<T>, Point<T>>> &polygonBorder,
              const Point<T> &pt) {
  int cross = 0;
  for (auto &seg : polygonBorder) {
    const Point<T> &cur = seg.first;
    const Point<T> &next = seg.second;
    if (OnSegment(cur, next, pt)) {
      return 2;
    }
    if (CrossRay(pt, cur, next)) {
      cross++;
    }
  }
  return cross % 2;
}

}  // namespace geo2

#endif