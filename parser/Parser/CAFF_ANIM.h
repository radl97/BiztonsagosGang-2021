#pragma once
#include "Block.h"
#include "CIFF.h"

const uint8_t BLOCK_ANIM_ID = 0x3;

class CAFF_ANIM :
    public Block
{
public:

    uint64_t duration;
    CIFF image;

    explicit CAFF_ANIM(const Block& block) : Block(block) {
        if(block.ID != BLOCK_ANIM_ID) {
            throw ParsingException();
        }
        duration = 0;
    }

    void read(Reader& r) {
        uint64_t characters_read_counter = r.getBytesRead();

        r.readPrimitive(duration);

        image.read(r);

        uint64_t characters_read_counter_after = r.getBytesRead();

        if (lengthOfBlock != characters_read_counter_after - characters_read_counter) {
            throw ParsingException();
        }
    }
};

