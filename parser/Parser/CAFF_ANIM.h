#pragma once
#include "Block.h"
#include "ciff.h"

class CAFF_ANIM :
    public Block
{
public:

    uint64_t duration;
    CIFF image;

    void read(Reader& r) {
        r.readPrimitive(ID);
        r.readPrimitive(lengthOfBlock);
        r.readPrimitive(duration);
        image = CIFF();
        image.read(r); 
    }
};

