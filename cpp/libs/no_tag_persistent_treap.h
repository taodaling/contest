#ifndef NO_TAG_PERSISTENT_TREAP_H
#define NO_TAG_PERSISTENT_TREAP_H
#include"common.h"

namespace no_tag_persistent_treap
{
class NoTagPersistentTreap
{
private:
    NoTagPersistentTreap(bool x)
    {
        NoTagPersistentTreap();
        _left = this;
        _right = this;
        _size = 0;
    }

public:
    NoTagPersistentTreap()
    {
        _left = NIL;
        _right = NIL;
        _size = 1;
        _key = 0;
    }

    static NoTagPersistentTreap *NIL;
    NoTagPersistentTreap *_left;
    NoTagPersistentTreap *_right;
    int _size;
    int _key;

    void pushUp()
    {
        if (this == NIL)
        {
            return;
        }
        _size = _left->_size + _right->_size + 1;
    }

    /**
     * split by rank and the node whose rank is argument will stored at result[0]
     */
    static pair<NoTagPersistentTreap *, NoTagPersistentTreap *> SplitByRank(NoTagPersistentTreap *root, int rank)
    {
        if (root == NIL)
        {
            return mp(NIL, NIL);
        }
        root = Clone(root);
        pair<NoTagPersistentTreap *, NoTagPersistentTreap *> result;
        if (root->_left->_size >= rank)
        {
            result = SplitByRank(root->_left, rank);
            root->_left = result.second;
            result.second = root;
        }
        else
        {
            result = SplitByRank(root->_right, rank - (root->_size - root->_right->_size));
            root->_right = result.first;
            result.first = root;
        }
        root->pushUp();
        return result;
    }

    static NoTagPersistentTreap *Merge(NoTagPersistentTreap *a, NoTagPersistentTreap *b)
    {
        if (a == NIL)
        {
            return b;
        }
        if (b == NIL)
        {
            return a;
        }
        if (uniform_int_distribution<int>(1, a->_size + b->_size)(rng) <= a->_size)
        {
            a = Clone(a);
            a->_right = Merge(a->_right, b);
            a->pushUp();
            return a;
        }
        else
        {
            b = Clone(b);
            b->_left = Merge(a, b->_left);
            b->pushUp();
            return b;
        }
    }

    static NoTagPersistentTreap *DeepClone(const NoTagPersistentTreap *root)
    {
        if (root == NIL)
        {
            return NIL;
        }
        NoTagPersistentTreap *cl = new NoTagPersistentTreap(*root);
        cl->_left = DeepClone(cl->_left);
        cl->_right = DeepClone(cl->_right);
        return cl;
    }

    static NoTagPersistentTreap *Clone(const NoTagPersistentTreap *root)
    {
        if (root == NIL)
        {
            return NIL;
        }
        return new NoTagPersistentTreap(*root);
    }

    /**
     * nodes with key <= arguments will stored at result[0]
     */
    static pair<NoTagPersistentTreap *, NoTagPersistentTreap *> SplitByKey(NoTagPersistentTreap *root, int key)
    {
        if (root == NIL)
        {
            return mp(NIL, NIL);
        }
        root = Clone(root);
        pair<NoTagPersistentTreap *, NoTagPersistentTreap *> result;
        if (root->_key > key)
        {
            result = SplitByKey(root->_left, key);
            root->_left = result.second;
            result.second = root;
        }
        else
        {
            result = SplitByKey(root->_right, key);
            root->_right = result.first;
            result.first = root;
        }
        root->pushUp();
        return result;
    }

    static int GetKeyByRank(NoTagPersistentTreap *treap, int k)
    {
        while (treap->_size > 1)
        {
            if (treap->_left->_size >= k)
            {
                treap = treap->_left;
            }
            else
            {
                k -= treap->_left->_size;
                if (k == 1)
                {
                    break;
                }
                k--;
                treap = treap->_right;
            }
        }
        return treap->_key;
    }
};

NoTagPersistentTreap *NoTagPersistentTreap::NIL = new NoTagPersistentTreap(false);

void dfs(ostream &out, NoTagPersistentTreap *root)
{
    if (root == NoTagPersistentTreap::NIL)
    {
        return;
    }
    dfs(out, root->_left);
    out << root->_key << ',';
    dfs(out, root->_right);
}
ostream &operator<<(ostream &out, const NoTagPersistentTreap *root)
{
    dfs(out, NoTagPersistentTreap::DeepClone(root));
    return out;
}
} // namespace no_tag_persistent_treap

#endif