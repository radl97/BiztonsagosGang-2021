
#pragma once
#include "Block.h"
#include "CAFF_HEADER.h"
#include "CAFF_CREDITS.h"
#include "CAFF_ANIM.h"
#include <cstdint>
#include <vector>
class CAFF
{
	
public:
	std::vector<Block> blocks;
	uint64_t blockNum;

    void read(Reader& r) { 
        CAFF_HEADER h;
        h.read(r);
        blockNum = h.animationNumber + 2;
        CAFF_CREDITS cred;
        cred.read(r);
        blocks.push_back(h);
        blocks.push_back(cred);
        std::cout << blockNum;
        for (int i = 2; i < blockNum; i++) {
            std::cout << i;
            CAFF_ANIM anim;
            anim.read(r);
            blocks.push_back(anim);
        }
    }
};

