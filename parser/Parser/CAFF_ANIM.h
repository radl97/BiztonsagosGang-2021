#pragma once
#include "Block.h"
#include "ciff.h"
class CAFF_ANIM :
    public Block
{
    uint64_t duration;
    CIFF image;
};

