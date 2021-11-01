#pragma once
#include "Block.h"
#include <cstdint>

class CAFF_HEADER : public Block
{
public:

    char m;
    char a;
    char g;
    char ic;
    uint64_t headerSize;
    uint64_t animationNumber;
};

