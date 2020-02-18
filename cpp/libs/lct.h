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
        if (this == NIL)
        {
            return;
        }
    }
};

LCTNode *LCTNode::NIL = new LCTNode(false);

} // namespace lct

#endif