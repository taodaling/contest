#ifndef MEMORY_POOL_H
#define MEMORY_POOL_H

#include "common.h"
char MEM_HEAD[200 * (1 << 20)];
char *MEM_TAIL = MEM_HEAD;
void releaseAllResource()
{
    memset(MEM_HEAD, 0, MEM_TAIL - MEM_HEAD);
    MEM_TAIL = MEM_HEAD;
}
void *operator new(size_t size)
{
    char *ret = MEM_TAIL;
    MEM_TAIL += size;
    return ret;
}
void *operator new[](size_t size)
{
    char *ret = MEM_TAIL;
    MEM_TAIL += size;
    return ret;
}
void operator delete(void *p) {}
void operator delete[](void *p) {}

#endif