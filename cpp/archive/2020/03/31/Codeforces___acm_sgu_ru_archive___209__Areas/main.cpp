#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/geo2.h"

using namespace geo2;
using pt = Point<double>;

struct Segment {
  pt a, b;
  vector<int> ptOnSeg;
  function<bool(int a, int b)> comp;
};

ostream &operator<<(ostream &os, const Segment &seg) {
  os << seg.a << "->" << seg.b << ":" << seg.ptOnSeg;
  return os;
}

vector<SortByPolarAngleWithOrigin<double>> sorter;
vector<function<bool(int, int)>> comps;
vector<vector<int>> next;
vector<pt> pts;
map<pt, int> vertices;

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<Segment> segs(n);
  for (int i = 0; i < n; i++) {
    in >> segs[i].a >> segs[i].b;
  }

  for (int i = 0; i < n; i++) {
    for (int j = 0; j < i; j++) {
      pt intersect;
      if (Intersect(segs[i].a, segs[i].b, segs[j].a, segs[j].b, intersect)) {
        if (vertices.find(intersect) == vertices.end()) {
          vertices[intersect] = vertices.size();
          pts.push_back(intersect);
        }
        segs[i].ptOnSeg.push_back(vertices[intersect]);
        segs[j].ptOnSeg.push_back(vertices[intersect]);
      }
    }
  }

  int m = vertices.size();
  comps.resize(m);
  sorter.resize(m);
  dbg(pts);
  next.resize(m);

  for (auto &seg : segs) {
    seg.comp = [&](auto a, auto b) {
      return (seg.a - pts[a]).square() < (seg.a - pts[b]).square();
    };
    sort(seg.ptOnSeg.begin(), seg.ptOnSeg.end(), seg.comp);
    dbg(seg);
    seg.ptOnSeg.erase(unique(seg.ptOnSeg.begin(), seg.ptOnSeg.end()),
                      seg.ptOnSeg.end());
  }

  dbg(segs);

  for (int i = 0; i < m; i++) {
    for (int j = 0; j < n; j++) {
      if (!OnSegment(segs[j].a, segs[j].b, pts[i])) {
        continue;
      }
      auto index = std::lower_bound(segs[j].ptOnSeg.begin(),
                                    segs[j].ptOnSeg.end(), i, segs[j].comp);
      if (index != segs[j].ptOnSeg.begin()) {
        next[i].push_back(*(index - 1));
      }
      if (index + 1 != segs[j].ptOnSeg.end()) {
        next[i].push_back(*(index + 1));
      }
    }
  }

  for (int i = 0; i < m; i++) {
    sorter[i].setOrigin(pts[i]);
    comps[i] = [&, i](int a, int b) { return sorter[i](pts[a], pts[b]); };
    sort(next[i].begin(), next[i].end(), comps[i]);
  }

  dbg(next);

  vector<double> areas;
  vector<pt> trace;
  trace.reserve(m);
  for (int i = 0; i < m; i++) {
    trace.clear();
    if (next[i].empty()) {
      continue;
    }
    trace.push_back(pts[i]);
    int node = next[i].back();
    next[i].pop_back();
    int p = i;
    while (node != i) {
      trace.push_back(pts[node]);
      int index = 0;
      for (int j = next[node].size() - 1; j >= 0; j--) {
        if (comps[node](p, next[node][j])) {
          continue;
        }
        index = (j + 1) % next[node].size();
        break;
      }
      p = node;
      node = next[node][index];
      next[p].erase(next[p].begin() + index);
    }
    dbg(trace);
    double area = Area(trace);
    if (area > 1e-8) {
      areas.push_back(area);
    }
  }
  sort(areas.begin(), areas.end());
  out << areas.size() << endl;
  for (double a : areas) {
    out << a << endl;
  }
}

RUN_ONCE