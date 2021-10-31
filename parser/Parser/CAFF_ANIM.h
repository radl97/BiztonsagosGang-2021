#pragma once
#include "Block.h"
#include "ciff.h"
class CAFF_ANIM :
    public Block
{
    unsigned int duration;
    CIFF image;
};

