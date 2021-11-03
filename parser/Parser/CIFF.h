#pragma once

#include "Reader.h"
#include <iostream>
#include <vector>
#include "Pixel.h"

class CIFF {

	static uint64_t checkedMultiplication(uint64_t a, uint64_t b) {
		// Based on: https://stackoverflow.com/a/1815391
		if (b > 0 && a > UINT64_MAX / b) {
			throw ParsingException();
		}
		return a*b;
	}

public:
	char magic[4];
	uint64_t header_size;
	uint64_t content_size;
	uint64_t width;
	uint64_t height;
	std::string caption;
	std::string tags;
	std::vector<Pixel> pixels;

	void read(Reader& r) {
		r.readArray(magic, 4);
		if (magic[0] != 'C' || magic[1] != 'I' || magic[2] != 'F' || magic[3] != 'F') {
			throw ParsingException();
		}
		r.readPrimitive(header_size);
		r.readPrimitive(content_size);
		r.readPrimitive(width);
		r.readPrimitive(height);
		uint64_t supposedContentSize = width;
		supposedContentSize = checkedMultiplication(supposedContentSize, height);
		supposedContentSize = checkedMultiplication(supposedContentSize, 3);
		if (supposedContentSize != content_size) {
			throw ParsingException();
		}
		caption = r.readUntilChar('\n');
		uint64_t tags_size = header_size - sizeof(magic) - sizeof(header_size) - sizeof(content_size) - sizeof(width) - sizeof(height) - sizeof(caption);
		char* tmp_tags = new char[header_size+1];
		r.readArray(tmp_tags, header_size);
		tmp_tags[header_size] = '\0';
		tags.assign(tmp_tags);
		delete[] tmp_tags;
		for (uint64_t i = 0; i < height; i++) {
			for (uint64_t j = 0; j < width; j++) {
				Pixel tmp_pixel;
				tmp_pixel.read(r);
				pixels.push_back(tmp_pixel);
			}
		}
	}
};
