#ifndef CHT_H
#define CHT_H

#include "common.h"
#include "decimal.h"

namespace cht {
template <class T>
struct Line {
  T a;
  T b;

  T operator()(T x) { return a * x + b; }
  T l, r;
};

template <class T>
ostream &operator<<(ostream &os, const Line<T> &line) {
  os << line.a << "x + " << line.b << "[" << line.l << "," << line.r << "]";
  return os;
}

template <class T>
pair<T, T> Intersect(Line<T> &a, Line<T> &b) {
  T floor = decimal::FloorDiv(b.b - a.b, a.a - b.a);
  T ceil = decimal::CeilDiv(b.b - a.b, a.a - b.a);
  return make_pair(floor, ceil);
}

template <>
pair<double, double> Intersect<double>(Line<double> &a, Line<double> &b) {
  double ans = (b.b - a.b) / (double)(a.a - b.a);
  return make_pair(ans, ans);
}

template <>
pair<long double, long double> Intersect<long double>(Line<long double> &a, Line<long double> &b) {
  long double ans = (b.b - a.b) / (long double)(a.a - b.a);
  return make_pair(ans, ans);
}

template <class T>
struct SortByA {
  bool operator()(const Line<T> *a, const Line<T> *b) { return a->a < b->a; }
};

template <class T>
struct SortByLeft {
  bool operator()(const Line<T> *a, const Line<T> *b) { return a->l < b->l; }
};

template <class T>
struct ConvexHullTrick {
 private:
  void erase(Line<T> *line) {
    indexByA.erase(indexByA.find(line));
    indexByLeft.erase(indexByLeft.find(line));
    delete line;
  }
  Line<T> _qLine;

 public:
  set<Line<T> *, SortByA<T>> indexByA;
  set<Line<T> *, SortByLeft<T>> indexByLeft;

  const Line<T> *add(T a, T b) {
    Line<T> *cand = new Line<T>();
    cand->a = a;
    cand->b = b;
    cand->l = numeric_limits<T>::min();
    cand->r = numeric_limits<T>::max();

    auto exist = indexByA.find(cand);
    if (exist == indexByA.end()) {
    } else if ((*exist)->b < cand->b) {
      erase(*exist);
    }
    else {
      return cand;
    }

    while (true) {
      auto left = indexByA.lower_bound(cand);
      if (left == indexByA.begin()) {
        break;
      }
      left--;
      Line<T> *floor = *left;
      pair<T, T> pt = Intersect(*floor, *cand);
      if (floor->l >= pt.second) {
        erase(floor);
        continue;
      }
      floor->r = pt.second;
      cand->l = pt.second;
      break;
    }
    while (true) {
      auto right = indexByA.upper_bound(cand);
      if (right == indexByA.end()) {
        break;
      }
      Line<T> *ceil = (*right);
      pair<T, T> pt = Intersect(*cand, *ceil);
      if (pt.second >= ceil->r) {
        erase(ceil);
        continue;
      }
      ceil->l = pt.second;
      cand->r = pt.second;
      break;
    }

    if (cand->l >= cand->r) {
      return cand;
    }
    indexByA.insert(cand);
    indexByLeft.insert(cand);
    return cand;
  }

  T operator()(T x) {
    _qLine.l = x;
    auto left = indexByLeft.upper_bound(&_qLine);
    left--;
    return (**left)(x);
  }
};

template <class T>
ostream &operator<<(ostream &os, const ConvexHullTrick<T> &cht) {
  for (auto &p : cht.indexByA) {
    os << *(p) << endl;
  }
  return os;
}
}  // namespace cht

#endif