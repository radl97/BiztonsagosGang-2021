#pragma once
#include <cstdint>
#include "Reader.h"

#pragma pack(push, 1)
struct Pixel
{
	uint8_t R, G, B;
};
#pragma pack(pop)

static std::ostream& operator<<(std::ostream& os, const Pixel& pixel) {
    return os << "R" << pixel.R << " G" << pixel.G << " B" << pixel.B;
}
