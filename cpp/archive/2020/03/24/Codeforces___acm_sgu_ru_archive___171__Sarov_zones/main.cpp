#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/util.h"

struct Student {
  int level;
  int w;
  int z;
};

struct Zone {
  int level;
  int cap;
};

void solve(int testId, istream &in, ostream &out) {
  int k;
  in >> k;
  vector<Zone> zones(k);
  int n = 0;
  for (int i = 0; i < k; i++) {
    in >> zones[i].cap;
    n += zones[i].cap;
  }

  for (int i = 0; i < k; i++) {
    in >> zones[i].level;
  }

  vector<Student> students(n);
  for (int i = 0; i < n; i++) {
    students[i].z = -1;
  }
  for (int i = 0; i < n; i++) {
    in >> students[i].level;
  }
  for (int i = 0; i < n; i++) {
    in >> students[i].w;
  }

  vector<int> sortedStudent = util::Range(0, n - 1);
  vector<int> sortedZone = util::Range(0, k - 1);
  sort(sortedStudent.begin(), sortedStudent.end(),
       [&](auto &a, auto &b) { return students[a].level < students[b].level; });

  sort(sortedZone.begin(), sortedZone.end(),
       [&](auto &a, auto &b) { return zones[a].level < zones[b].level; });

  priority_queue<int, vector<int>, function<bool(int, int)>> pq(
      [&](auto a, auto b) { return students[a].w > students[b].w; });

  dbg(sortedStudent);
  dbg(sortedZone);
  int l = 0;
  while (l < n && students[sortedStudent[l]].level <= zones[sortedZone[0]].level) {
    l++;
  }

  deque<int> spare;
  for (int i = 0; i < k; i++) {
    for (int j = 0; j < zones[sortedZone[i]].cap; j++) {
      spare.push_back(sortedZone[i]);
    }
    while (l < n && (i == k - 1 || students[sortedStudent[l]].level <=
                                       zones[sortedZone[i + 1]].level)) {
      int s = sortedStudent[l++];
      if (!spare.empty()) {
        students[s].z = spare.front();
        spare.pop_front();
        pq.push(s);
      } else if (!pq.empty() && students[pq.top()].w < students[s].w) {
        students[s].z = students[pq.top()].z;
        students[pq.top()].z = -1;
        pq.pop();
        pq.push(s);
      }
    }
  }

  for (int i = 0; i < n; i++) {
    if (students[i].z == -1) {
      students[i].z = spare.front();
      spare.pop_front();
    }
    out << students[i].z + 1 << ' ';
  }
  assert(spare.empty());
}

RUN_ONCE