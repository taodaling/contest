#include "../../libs/common.h"
#include "../../libs/debug.h"

#define PREC 1e-12
#define double ld

inline bool IsZero(double x) { return -PREC <= x && x <= PREC; }

struct Point {
  double x;
  double y;
  double z;
} ORIGIN;

struct Sphere : public Point {
  double r;
};

ostream &operator<<(ostream &os, const Point &pt) {
  os << "(" << pt.x << "," << pt.y << "," << pt.z << ")";
  return os;
}

double Pow2(double x) { return x * x; }

double Length2(Point &a, Point &b) {
  return Pow2(a.x - b.x) + Pow2(a.y - b.y) + Pow2(a.z - b.z);
}

double Length(Point &a, Point &b) { return sqrtl(Length2(a, b)); }

double DotMul(Point &a, Point &b) { return a.x * b.x + a.y * b.y + a.z * b.z; }

void Normalize(Point &pt) {
  double len = Length(ORIGIN, pt);
  pt.x /= len;
  pt.y /= len;
  pt.z /= len;
}

void Reflect(Point &vec, Point &alpha) {
  double dm = DotMul(vec, alpha);
  vec.x -= 2 * dm * alpha.x;
  vec.y -= 2 * dm * alpha.y;
  vec.z -= 2 * dm * alpha.z;
}

vector<double> Pow2(const vector<double> &x) {
  vector<double> ans(x.size() * 2 - 1);
  for (int i = 0; i < x.size(); i++) {
    for (int j = 0; j < x.size(); j++) {
      ans[i + j] += x[i] * x[j];
    }
  }
  return ans;
}

void Plus(vector<double> &a, const vector<double> &b) {
  for (int i = 0; i < 3; i++) {
    a[i] += b[i];
  }
}

void Move(Point &pt, const Point &ray, double time) {
  pt.x += ray.x * time;
  pt.y += ray.y * time;
  pt.z += ray.z * time;
}

double Intersect(Sphere &s, Point &pt, Point &ray) {
  vector<double> poly(3);
  Plus(poly, Pow2(vector<double>{pt.x - s.x, ray.x}));
  Plus(poly, Pow2(vector<double>{pt.y - s.y, ray.y}));
  Plus(poly, Pow2(vector<double>{pt.z - s.z, ray.z}));

  poly[0] -= s.r * s.r;

  if (IsZero(poly[2])) {
    if (IsZero(poly[1])) {
      return -1;
    } else {
      return -poly[0] / poly[1];
    }
  }

  double a = poly[2];
  double b = poly[1];
  double c = poly[0];
  if (Pow2(b) < 4 * a * c) {
    return -1;
  }
  double x1 = (-b - sqrtl(Pow2(b) - 4 * a * c)) / (2 * a);
  double x2 = (-b + sqrtl(Pow2(b) - 4 * a * c)) / (2 * a);
  if (IsZero(x1 - x2)) {
    return -1;
  }
  if (x1 < -PREC) {
    return x2;
  }
  return x1;
}

Sphere sphere[50];

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  for (int i = 0; i < n; i++) {
    in >> sphere[i].x >> sphere[i].y >> sphere[i].z >> sphere[i].r;
  }

  vector<int> ans;
  Point pt, ray;
  in >> pt.x >> pt.y >> pt.z;
  in >> ray.x >> ray.y >> ray.z;

  if (Length2(ORIGIN, ray) == 0) {
    return;
  }

  int last = -1;
  while (ans.size() < 11) {
    Normalize(ray);
    dbg(pt, ray);
    double best = 1e18;
    int index = -1;
    for (int i = 0; i < n; i++) {
      if (i == last) {
        continue;
      }
      double time = Intersect(sphere[i], pt, ray);
      if (time < 0 || IsZero(time) || time >= best) {
        continue;
      }
      best = time;
      index = i;
    }

    dbg(best, index);
    if (index < 0) {
      break;
    }
    Move(pt, ray, best);
    Point diff{pt.x - sphere[index].x, pt.y - sphere[index].y,
               pt.z - sphere[index].z};
    dbg(abs(Length(ORIGIN, diff) - sphere[index].r));

    // if(!IsZero(DotMul(diff, ray))){
    Reflect(ray, diff);
    //}
    ans.push_back(index);
    last = index;
  }

  for (int i = 0; i < ans.size() && i < 10; i++) {
    out << ans[i] + 1 << ' ';
  }
  if (ans.size() > 10) {
    out << "etc.";
  }
}

RUN_ONCE