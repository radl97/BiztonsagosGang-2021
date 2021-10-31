#pragma once
#include "Block.h"
class CAFF_CREDITS :
    public Block
{
    unsigned int year;
    unsigned int month;
    unsigned int day;
    unsigned int hour;
    unsigned int minute;
    unsigned int creatorNameLen;
    char* creatorName;
};

