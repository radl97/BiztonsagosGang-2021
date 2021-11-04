
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
	std::vector<CAFF_HEADER> headers;
    std::vector<CAFF_CREDITS> credits;
    std::vector<CAFF_ANIM> animations;

	uint64_t blockNum;

    void read(Reader& r) { 
        CAFF_HEADER h;
        h.read(r);
        headers.push_back(h);

        blockNum = h.animationNumber + 2;
        // TODO Credits are not specified too well: 1) is it optional 2) when can it be placed? (end of file? middle of animation?)
        for (int i = 1; i < blockNum; i++) {
            Block containingBlock;
            containingBlock.readBlockHeader(r);
            if (containingBlock.ID == 0x2) {
                CAFF_CREDITS cred;
                cred.readContent(r, containingBlock); 
                credits.push_back(cred);
            }
            else {
                CAFF_ANIM anim;
                anim.readContent(r, containingBlock);
                animations.push_back(anim);
            }
        }
    }
};

