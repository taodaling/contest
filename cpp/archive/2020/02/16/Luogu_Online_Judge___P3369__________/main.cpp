#define MEM_LIMIT 100

#include "../../libs/common.h"
#include "../../libs/memory_controll.h"
#include "../../libs/treap.h"

using namespace treap;

void solve(int testId, istream &in, ostream &out)
{
    Treap *tree = Treap::NIL;
    int n;
    in >> n;
    for (int i = 0; i < n; i++)
    {
        int opt, x;
        in >> opt >> x;
        if (opt == 1)
        {
            Treap *node = new Treap();
            node->_key = x;
            pair<Treap *, Treap *> p1 = Treap::SplitByKey(tree, x);
            tree = Treap::Merge(p1.first, node);
            tree = Treap::Merge(tree, p1.second);
        }
        else if (opt == 2)
        {
            pair<Treap *, Treap *> p1 = Treap::SplitByKey(tree, x);
            if (p1.first->_size > 0 && Treap::GetKeyByRank(p1.first, p1.first->_size))
            {
                p1.first = Treap::SplitByRank(p1.first, p1.first->_size - 1).first;
            }
            tree = Treap::Merge(p1.first, p1.second);
        }
        else if (opt == 3)
        {
            pair<Treap *, Treap *> p1 = Treap::SplitByKey(tree, x - 1);
            out << p1.first->_size + 1 << endl;
            tree = Treap::Merge(p1.first, p1.second);
        }
        else if (opt == 4)
        {
            pair<Treap *, Treap *> p1 = Treap::SplitByRank(tree, x);
            out << Treap::GetKeyByRank(p1.first, p1.first->_size) << endl;
            tree = Treap::Merge(p1.first, p1.second);
        }
        else if (opt == 5)
        {
            pair<Treap *, Treap *> p1 = Treap::SplitByKey(tree, x - 1);
            out << Treap::GetKeyByRank(p1.first, p1.first->_size) << endl;
            tree = Treap::Merge(p1.first, p1.second);
        }
        else if (opt == 6)
        {
            pair<Treap *, Treap *> p1 = Treap::SplitByKey(tree, x);
            out << Treap::GetKeyByRank(p1.second, 1) << endl;
            tree = Treap::Merge(p1.first, p1.second);
        }
    }
}

RUN_ONCE