#pragma once
#include "Block.h"
#include "CIFF.h"

class CAFF_ANIM :
    public Block
{
public:

    uint64_t duration;
    CIFF image;

    void read(Reader& r) {
        Block::read(r);
        r.readPrimitive(duration);
        std::cout << "\nanimstart";
        image.read(r); 
    }
};

