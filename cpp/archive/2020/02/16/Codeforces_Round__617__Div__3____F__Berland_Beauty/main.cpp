#include "../../libs/common.h"

#ifndef LCT_H
#define LCT_H

namespace lct
{
class LCTNode
{
public:
    static LCTNode *NIL;
    LCTNode *_left;
    LCTNode *_right;
    LCTNode *_father;
    LCTNode *_treeFather;
    bool _reverse;
    int _id;
    int _min;
    int _setMin;
    int _v;

    LCTNode(bool x)
    {
        LCTNode();
        _left = _right = _father = _treeFather = this;
    }
    LCTNode()
    {
        _left = NIL;
        _right = NIL;
        _father = NIL;
        _treeFather = NIL;
        _reverse = false;
        _min = 1;
        _setMin = 1;
        _v = 1;
    }

    void reverse()
    {
        _reverse = !_reverse;
    }

    static void Access(LCTNode *x)
    {
        LCTNode *last = NIL;
        while (x != NIL)
        {
            Splay(x);
            x->_right->_father = NIL;
            x->_right->_treeFather = x;
            x->setRight(last);
            x->pushUp();

            last = x;
            x = x->_treeFather;
        }
    }

    static void MakeRoot(LCTNode *x)
    {
        Access(x);
        Splay(x);
        x->reverse();
    }

    static void Cut(LCTNode *y, LCTNode *x)
    {
        MakeRoot(y);
        Access(x);
        Splay(y);
        y->_right->_treeFather = NIL;
        y->_right->_father = NIL;
        y->setRight(NIL);
        y->pushUp();
    }

    static void Join(LCTNode *y, LCTNode *x)
    {
        MakeRoot(x);
        x->_treeFather = y;
    }

    static void FindRoute(LCTNode *x, LCTNode *y)
    {
        MakeRoot(y);
        Access(x);
    }

    static void Splay(LCTNode *x)
    {
        if (x == NIL)
        {
            return;
        }
        LCTNode *y;
        LCTNode *z;
        while ((y = x->_father) != NIL)
        {
            if ((z = y->_father) == NIL)
            {
                y->pushDown();
                x->pushDown();
                if (x == y->_left)
                {
                    Zig(x);
                }
                else
                {
                    Zag(x);
                }
            }
            else
            {
                z->pushDown();
                y->pushDown();
                x->pushDown();
                if (x == y->_left)
                {
                    if (y == z->_left)
                    {
                        Zig(y);
                        Zig(x);
                    }
                    else
                    {
                        Zig(x);
                        Zag(x);
                    }
                }
                else
                {
                    if (y == z->_left)
                    {
                        Zag(x);
                        Zig(x);
                    }
                    else
                    {
                        Zag(y);
                        Zag(x);
                    }
                }
            }
        }

        x->pushDown();
        x->pushUp();
    }

    static void Zig(LCTNode *x)
    {
        LCTNode *y = x->_father;
        LCTNode *z = y->_father;
        LCTNode *b = x->_right;

        y->setLeft(b);
        x->setRight(y);
        z->changeChild(y, x);

        y->pushUp();
    }

    static void Zag(LCTNode *x)
    {
        LCTNode *y = x->_father;
        LCTNode *z = y->_father;
        LCTNode *b = x->_left;

        y->setRight(b);
        x->setLeft(y);
        z->changeChild(y, x);

        y->pushUp();
    }

    static LCTNode FindRoot(LCTNode *x)
    {
        Splay(x);
        x->pushDown();
        while (x->_left != NIL)
        {
            x = x->_left;
            x->pushDown();
        }
        Splay(x);
        return x;
    }

    void setMin(int x)
    {
        _min = max(x, _min);
        _v = max(_v, x);
        _setMin = max(_setMin, x);
    }

    void pushDown()
    {
        if (this == NIL)
        {
            return;
        }
        if (_reverse)
        {
            _reverse = false;

            LCTNode *tmpNode = _left;
            _left = _right;
            _right = tmpNode;

            _left->reverse();
            _right->reverse();
        }

        _left->_treeFather = _treeFather;
        _right->_treeFather = _treeFather;

        _left->setMin(_setMin);
        _right->setMin(_setMin);
        _setMin = 1;
    }

    void setLeft(LCTNode *x)
    {
        _left = x;
        x->_father = this;
    }

    void setRight(LCTNode *x)
    {
        _right = x;
        x->_father = this;
    }

    void changeChild(LCTNode *y, LCTNode *x)
    {
        if (_left == y)
        {
            setLeft(x);
        }
        else
        {
            setRight(x);
        }
    }

    void pushUp()
    {
        _min = min(_left->_min, _right->_min);
        _min = min(_min, _v);
    }
};

LCTNode *LCTNode::NIL = new LCTNode(false);

} // namespace lct

#endif

using namespace lct;
LCTNode nodes[5001], edges[5001];

struct Query{
    int a;
    int b;
    int k;
};

void solve(int testId, istream &in, ostream &out)
{
    int n;
    in >> n;
    for (int i = 0; i < n - 1; i++)
    {
        int a, b;
        in >> a >> b;
        LCTNode::Join(&nodes[a], &edges[i]);
        LCTNode::Join(&nodes[b], &edges[i]);
    }
    int m;
    in >> m;
    vector<Query> reqs(m);
    for (int i = 0; i < m; i++){
        in >> reqs[i].a >> reqs[i].b >> reqs[i].k;
        LCTNode::FindRoute(nodes + reqs[i].a, nodes + reqs[i].b);
        LCTNode::Splay(nodes + reqs[i].a);
        nodes[reqs[i].a].setMin(reqs[i].k);
    }

    for(auto &r : reqs){
        LCTNode::FindRoute(nodes + r.a, nodes + r.b);
        LCTNode::Splay(nodes + r.a);
        if(nodes[r.a]._min != r.k){
            out << -1;
            return;
        }
    }

    for(int i = 0; i < n - 1; i++){
        LCTNode::Access(edges + i);
        LCTNode::Splay(edges + i);
        out << edges[i]._v << endl;
    }
    
}

RUN_ONCE