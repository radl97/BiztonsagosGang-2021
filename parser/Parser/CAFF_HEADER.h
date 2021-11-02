#pragma once
#include "Block.h"
#include <cstdint>

class CAFF_HEADER : public Block
{
public:
    char magic[4];
    uint64_t headerSize;
    uint64_t animationNumber;

    void read(Reader& r) {
        r.readPrimitive(ID);
        r.readPrimitive(lengthOfBlock);
        r.readArray(magic, 4);
        r.readPrimitive(headerSize);
        r.readPrimitive(animationNumber);
    }
};

