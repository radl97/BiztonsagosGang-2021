#pragma once
#include <cstdint>
#include <ostream>
#include "Reader.h"


#pragma pack(push, 1)
struct Pixel
{
	uint8_t R;
	uint8_t G;
	uint8_t B;
};
#pragma pack(pop)