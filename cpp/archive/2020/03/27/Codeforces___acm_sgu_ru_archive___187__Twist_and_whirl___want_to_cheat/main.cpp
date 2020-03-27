#include "../../libs/common.h"

namespace treap {
class Treap {
 private:
  Treap(bool x) {
    Treap(0);
    _left = this;
    _right = this;
    _size = 0;
  }

 public:
  Treap(int x) {
    _left = NIL;
    _right = NIL;
    _size = 1;
    _key = 0;
    _rev = false;
    _key = x;
  }

  static Treap *NIL;
  Treap *_left;
  Treap *_right;
  int _size;
  int _key;
  int _rev;

  void reverse() {
    if (this == NIL) {
      return;
    }
    _rev = !_rev;
    swap(_left, _right);
  }

  void pushDown() {
    if (this == NIL) {
      return;
    }
    if (_rev) {
      _left->reverse();
      _right->reverse();
      _rev = false;
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
      return mp(NIL, NIL);
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
      return mp(NIL, NIL);
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

void Dfs(Treap *root, ostream &os) {
  if (root == Treap::NIL) {
    return;
  }
  root->pushDown();
  Dfs(root->_left, os);
  os << root->_key << ' ';
  Dfs(root->_right, os);
}

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  Treap *root = Treap::NIL;
  for (int i = 1; i <= n; i++) {
    Treap *t = new Treap(i);
    root = Treap::Merge(root, t);
  }
  for (int i = 0; i < m; i++) {
    int l, r;
    in >> l >> r;
    pair<Treap *, Treap *> split0 = Treap::SplitByRank(root, r);
    pair<Treap *, Treap *> split1 = Treap::SplitByRank(split0.first, l - 1);
    split1.second->reverse();
    split0.first = Treap::Merge(split1.first, split1.second);
    root = Treap::Merge(split0.first, split0.second);
  }
  Dfs(root, out);
}

RUN_ONCE