#pragma once
#include "Block.h"
class CAFF_HEADER :
    public Block
{
    char magic[4];
    unsigned int headerSize;
    unsigned int animationNumber;
};

