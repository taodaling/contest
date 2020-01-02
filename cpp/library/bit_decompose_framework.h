//
// Created by daltao on 2019/12/18.
//

#ifndef BIT_DECOMPOSE_FRAMEWORK_H
#define BIT_DECOMPOSE_FRAMEWORK_H

#include "common.h"

namespace dalt {
    template<class E, class M>
    class BitDecomposeFramework {
    private:
        vector<E *> _data;
        M _merger;

        void add(E *e, int index) {
            if (_data.size() == index) {
                _data.push_back(e);
                return;
            }
            if(_data[index] == NULL){
                _data[index] = e;
                return;
            }
            add(_merger(_data[index], e), index + 1);
            _data[index] = NULL;
        }

    public:

        void add(E *e) {
            add(e, 0);
        }

        template<class C>
        void consume(C &c){
            for(E *e : _data){
                if(e){
                    c(e);
                }
            }
        }
    };
}

#endif //BIT_DECOMPOSE_FRAMEWORK_H
