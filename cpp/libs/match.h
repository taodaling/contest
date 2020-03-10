#ifndef MATCH_H
#define MATCH_H

#include "common.h"

namespace match {
class BipartiteMatch {
 private:
  vector<int> _partner[2];
  vector<vector<int>> _g[2];
  vector<int> _time[2];
  int _now;

  bool release(int side, int i) {
    if (_time[side][i] == _now) {
      return false;
    }
    _time[side][i] = _now;
    if (_partner[side][i] == -1) {
      return true;
    }
    return bind(1 ^ side, _partner[side][i]);
  }
  bool bind(int side, int i) {
    if (_time[side][i] == _now) {
      return false;
    }
    _time[side][i] = _now;
    for (int node : _g[side][i]) {
      if (release(1 ^ side, node)) {
        _partner[side][i] = node;
        _partner[1 ^ side][node] = i;
        return true;
      }
    }
    return false;
  }

  void dfs(int side, int node) {
    if (_time[side][node] == _now) {
      return;
    }
    _time[side][node] = _now;
    for (int next : _g[side][node]) {
      dfs(1 ^ side, next);
    }
  }

 public:
  BipartiteMatch(int l, int r) {
    int size[]{l, r};
    _now = 0;
    for (int i = 0; i < 2; i++) {
      _partner[i].resize(size[i]);
      _time[i].resize(size[i]);
      _g[i].resize(size[i]);
      fill(_partner[i].begin(), _partner[i].end(), -1);
    }
  }

  void addEdge(int a, int b, bool greedy) {
    _g[0][a].push_back(b);
    _g[1][b].push_back(a);
    if (greedy && _partner[0][a] == -1 && _partner[1][b] == -1) {
      _partner[0][a] = b;
      _partner[1][b] = a;
    }
  }

  int matchLeft(int i) {
    if (_partner[0][i] == -1) {
      _now++;
      bind(0, i);
    }
    return _partner[0][i];
  }
  int matchRight(int i) {
    if (_partner[1][i] == -1) {
      _now++;
      bind(1, i);
    }
    return _partner[1][i];
  }

  void minVertexCover(vector<vector<bool>> &status) {
    _now++;
    for (int i = 0; i < _time[1].size(); i++) {
      if (_partner[1][i] == -1) {
        dfs(1, i);
      }
    }
    for (int i = 0; i < 2; i++) {
      fill(status[i].begin(), status[i].end(), false);
    }
    for (int i = 0; i < _time[0].size(); i++) {
      status[0][i] = _time[0][i] == _now;
    }
    for (int i = 0; i < _time[1].size(); i++) {
      status[1][i] = _time[1][i] != _now;
    }
  }
};
}  // namespace match

#endif