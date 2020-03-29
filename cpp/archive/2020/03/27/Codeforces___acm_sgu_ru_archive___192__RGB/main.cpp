#include "../../libs/common.h"
#include "../../libs/geo2.h"
#include "../../libs/util.h"

using namespace geo2;
using pt = geo2::Point<double>;
const double prec = 1e-8;
const double inf = 1e6;

struct Segment {
  pt a, b;
  char c;

  double left() { return min(a.x, b.x); }

  double right() { return max(a.x, b.x); }
};

double Y(Segment &seg, double x) {
  pt top(x, inf);
  pt bot(x, -inf);
  pt mid;
  geo2::Intersect(seg.a, seg.b, top, bot, mid);
  return mid.y;
}

void insertSort(vector<Segment> &segs, double x) {
  for (int i = segs.size() - 1; i >= 0; i--) {
    if (segs[i].right() < x) {
      segs.erase(segs.begin() + i);
    }
  }

  int n = segs.size();
  for (int i = 1; i < n; i++) {
    int j = i;
    while (0 < j && Y(segs[j - 1], x) > Y(segs[j], x)) {
      swap(segs[j], segs[j - 1]);
      j--;
    }
  }
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<Segment> segs;
  segs.reserve(n + n);

  pt xL(-1e6, 0);
  pt xR(1e6, 0);
  for (int i = 0; i < n; i++) {
    pt a, b;
    char c;
    in >> a >> b >> c;

    if (abs(a.x - b.x) < prec) {
      continue;
    }

    pt mid;
    if (ProperIntersect(a, b, xL, xR, mid)) {
      a.y = abs(a.y);
      b.y = abs(b.y);
      segs.push_back({a : a, b : mid, c : c});
      segs.push_back({a : mid, b : b, c : c});
    } else {
      a.y = abs(a.y);
      b.y = abs(b.y);
      segs.push_back({a : a, b : b, c : c});
    }
  }
  n = segs.size();
  vector<bool> added(n);

  vector<double> xs;
  xs.reserve(n * n + n * 2);
  for (int i = 0; i < n; i++) {
    xs.push_back(segs[i].a.x);
    xs.push_back(segs[i].b.x);
    for (int j = 0; j < n; j++) {
      if (i == j) {
        continue;
      }
      pt mid;
      if (ProperIntersect(segs[i].a, segs[i].b, segs[j].a, segs[j].b, mid)) {
        xs.push_back(mid.x);
      }
    }
  }

  vector<Segment> sorted;
  vector<double> area(128);
  sort(xs.begin(), xs.end());
  for (int i = 1; i < xs.size(); i++) {
    double l = xs[i - 1];
    double r = xs[i];
    if (abs(r - l) < prec) {
      continue;
    }
    for (int j = 0; j < n; j++) {
      if (!added[j] && segs[j].left() <= l) {
        sorted.push_back(segs[j]);
        added[j] = true;
      }
    }
    insertSort(sorted, (r + l) / 2);
    if (sorted.empty()) {
      continue;
    }
    area[sorted.front().c] += r - l;
  }

  for (char c : vector<char>{'R', 'G', 'B'}) {
    out << c << ' ' << area[c] << endl;
  }
}

RUN_ONCE