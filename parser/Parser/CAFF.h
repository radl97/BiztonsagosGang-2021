
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
        bool isCreditPresent = false;
        CAFF_HEADER h;
        h.read(r);
        headers.push_back(h);

        blockNum = h.animationNumber;
        for (int i = 0; i < blockNum; i++) {
            Block containingBlock;
            containingBlock.readBlockHeader(r);
            if (containingBlock.ID == 0x2) {
                if (isCreditPresent) {
                    throw ParsingException();
                }
                isCreditPresent = true;
                CAFF_CREDITS cred(containingBlock);
                cred.read(r); 
                credits.push_back(cred);
                // blockNum is incremented, to allow animationnumber + 1(credits) blocks to be read
                // since credits block is optional
                blockNum++;
            }
            else if (containingBlock.ID == 0x3){
                CAFF_ANIM anim(containingBlock);
                anim.read(r);
                animations.push_back(anim);
            }
            else {
                throw ParsingException();
            }
        }
        //No preview can be produced if there are no animations in the Caff file
        if (animations.empty()) {
            throw ParsingException();
        }
    }
};

