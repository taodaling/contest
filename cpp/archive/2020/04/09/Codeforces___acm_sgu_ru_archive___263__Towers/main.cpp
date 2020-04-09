#include "../../libs/common.h"
#include "../../libs/debug.h"

namespace treap {
class Treap {
 private:
  Treap(bool x) {
    Treap();
    _left = this;
    _right = this;
    _size = 0;
  }

 public:
  Treap() {
    _left = NIL;
    _right = NIL;
    _size = 1;
    _key = 0;
  }

  static Treap *NIL;
  Treap *_left;
  Treap *_right;
  int _size;
  int _key;

  void pushDown() {
    if (this == NIL) {
      return;
    }
  }

  void pushUp() {
    if (this == NIL) {
      return;
    }
    _size = _left->_size + _right->_size + 1;
  }

  /**
   * split by rank and the node whose rank is argument will stored at result[0]
   */
  static pair<Treap *, Treap *> SplitByRank(Treap *root, int rank) {
    if (root == NIL) {
      return make_pair(NIL, NIL);
    }
    root->pushDown();
    pair<Treap *, Treap *> result;
    if (root->_left->_size >= rank) {
      result = SplitByRank(root->_left, rank);
      root->_left = result.second;
      result.second = root;
    } else {
      result =
          SplitByRank(root->_right, rank - (root->_size - root->_right->_size));
      root->_right = result.first;
      result.first = root;
    }
    root->pushUp();
    return result;
  }

  static Treap *Merge(Treap *a, Treap *b) {
    if (a == NIL) {
      return b;
    }
    if (b == NIL) {
      return a;
    }
    if (uniform_int_distribution<int>(1, a->_size + b->_size)(rng) <=
        a->_size) {
      a->pushDown();
      a->_right = Merge(a->_right, b);
      a->pushUp();
      return a;
    } else {
      b->pushDown();
      b->_left = Merge(a, b->_left);
      b->pushUp();
      return b;
    }
  }

  static Treap *DeepClone(const Treap *root) {
    if (root == NIL) {
      return NIL;
    }
    Treap *cl = new Treap(*root);
    cl->_left = DeepClone(cl->_left);
    cl->_right = DeepClone(cl->_right);
    return cl;
  }

  /**
   * nodes with key <= arguments will stored at result[0]
   */
  static pair<Treap *, Treap *> SplitByKey(Treap *root, int key) {
    if (root == NIL) {
      return make_pair(NIL, NIL);
    }
    root->pushDown();
    pair<Treap *, Treap *> result;
    if (root->_key > key) {
      result = SplitByKey(root->_left, key);
      root->_left = result.second;
      result.second = root;
    } else {
      result = SplitByKey(root->_right, key);
      root->_right = result.first;
      result.first = root;
    }
    root->pushUp();
    return result;
  }

  static int GetKeyByRank(Treap *treap, int k) {
    while (treap->_size > 1) {
      treap->pushDown();
      if (treap->_left->_size >= k) {
        treap = treap->_left;
      } else {
        k -= treap->_left->_size;
        if (k == 1) {
          break;
        }
        k--;
        treap = treap->_right;
      }
    }
    return treap->_key;
  }

  static int GetRankByKey(Treap *treap, int key) {
    int rank = 0;
    while (treap != NIL) {
      if (treap->_key == key) {
        rank += treap->_left->_size + 1;
        return rank;
      } else if (treap->_key < key) {
        rank += treap->_left->_size + 1;
        treap = treap->_right;
      } else {
        treap = treap->_left;
      }
    }
    return rank;
  }
};

Treap *Treap::NIL = new Treap(false);

void dfs(ostream &out, Treap *root) {
  if (root == Treap::NIL) {
    return;
  }
  root->pushDown();
  dfs(out, root->_left);
  out << root->_key << ',';
  dfs(out, root->_right);
}
ostream &operator<<(ostream &out, const Treap *root) {
  dfs(out, Treap::DeepClone(root));
  return out;
}
}  // namespace treap

using namespace treap;
using pt = pair<Treap *, Treap *>;

vector<int> cubes(1000000 + 100);
vector<int> length(1000000 + 100);
vector<ll> total(1000000 + 100);
Treap *root = Treap::NIL;

int getByRank(int rank) { return Treap::GetKeyByRank(root, rank); }

int nearKey(int key) {
  int prev = -1;
  pt split = Treap::SplitByKey(root, key);
  if (X(split)->_size > 0) {
    prev = Treap::GetKeyByRank(X(split), X(split)->_size);
  }
  root = Treap::Merge(X(split), Y(split));
  return prev;
}

void removeKey(int key) {
  pt split = Treap::SplitByKey(root, key);
  pt lsplit = Treap::SplitByKey(X(split), key - 1);
  X(split) = X(lsplit);
  root = Treap::Merge(X(split), Y(split));
}

void addKey(int key) {
  Treap *t = new Treap();
  t->_key = key;
  pt split = Treap::SplitByKey(root, key);
  X(split) = Treap::Merge(X(split), t);
  root = Treap::Merge(X(split), Y(split));
}

void MergeKey(int a, int b) {
  if (a > b) {
    swap(a, b);
  }
  if (a + length[a] < b) {
    return;
  }
  removeKey(b);
  length[a] += length[b];
  total[a] += total[b];
}

void solve(int testId, istream &in, ostream &out) {
  stringstream ss;

  int n;
  in >> n;
  for (int i = 0; i < n; i++) {
    string s;
    in >> s;
    if (s == "put") {
      int x, c;
      in >> x >> c;
      cubes[x] += c;
      if (cubes[x] == c) {
        length[x] = 1;
        total[x] += c;
        addKey(x);
        if (cubes[x + 1]) {
          MergeKey(x, x + 1);
        }
        if (cubes[x - 1]) {
          MergeKey(nearKey(x - 1), x);
        }
      }else{
        int offset = nearKey(x);
        total[offset] += c;
      }
    } else if (s == "tput") {
      int t, x, c;
      in >> t >> x >> c;
      int offset = getByRank(t);
      cubes[offset + x - 1] += c;
      total[offset] += c;
    } else if (s == "towers") {
      int ans = root->_size;
      ss << ans << " towers" << endl;
    } else if (s == "length") {
      int t;
      in >> t;
      int offset = getByRank(t);
      ss << "length of " << t << "th tower is " << length[offset] << endl;
    } else if (s == "cubes") {
      int t;
      in >> t;
      int offset = getByRank(t);
      ss << total[offset] << " cubes in " << t << "th tower" << endl;
    } else if (s == "tcubes") {
      int t, x;
      in >> t >> x;
      int offset = getByRank(t);
      ss << cubes[offset + x - 1] << " cubes in " << x << "th column of " << t
         << "th tower" << endl;
    } else {
      exit(1);
    }

    dbg(root);
  }

  out << ss.str();
}

RUN_ONCE