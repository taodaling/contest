#ifndef KMP_H
#define KMP_H

namespace kmp {
template <class T, int N>
class KMPAutomaton {
 private:
  T _data[N + 2];
  int _fail[N + 2];
  int _buildLast;
  int _matchLast;

 public:
  KMPAutomaton() {
    _buildLast = 0;
    _fail[0] = -1;
  }

  void reset() { _buildLast = 0; }

  /**
   * Get the border of s[1...i]
   */
  int maxBorder(int i) { return _fail[i + 1]; }

  bool isMatch() { return _matchLast == _buildLast; }

  int length() { return _buildLast; }

  void beginMatch() { _matchLast = 0; }

  void match(T c) { _matchLast = visit(c, _matchLast) + 1; }

  int visit(T c, int trace) {
    while (trace >= 0 && _data[trace + 1] != c) {
      trace = _fail[trace];
    }
    return trace;
  }

  void build(T c) {
    _buildLast++;
    _fail[_buildLast] = visit(c, _fail[_buildLast - 1]) + 1;
    _data[_buildLast] = c;
  }
};
}  // namespace kmp

#endif