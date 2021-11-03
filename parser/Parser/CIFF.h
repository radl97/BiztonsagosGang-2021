#pragma once

#include "Reader.h"
#include <iostream>
#include <vector>
#include "Pixel.h"

class CIFF {

	// TODO (fuzz-)test this
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
	std::vector<std::string> tags;
	std::vector<Pixel> pixels;

private:
	void readTags(Reader& r, uint64_t supposed_tags_size) {
		std::string tmp_tags = r.readStringOfLength(supposed_tags_size);
		uint64_t offset = 0;
		while (offset < 0) {
			uint64_t end = tmp_tags.find('\0', offset);
			if (end == std::string::npos) {
				// missing ending (empty) tag
				throw ParsingException();
			}
			if (end == offset) {
				// empty string -> end of tags
				// must be end of the whole data
				if (end != tmp_tags.size()-1) {
					throw ParsingException();
				}
				return;
			} else {
				std::string tmp_tag;
				tmp_tag.assign(tmp_tags, offset, end-offset);
				tags.push_back(tmp_tag);
				offset = end+1;
			}
		}
	}

public:

	void read(Reader& r) {
		r.readArray(magic, 4);
		if (magic[0] != 'C' || magic[1] != 'I' || magic[2] != 'F' || magic[3] != 'F') {
			throw ParsingException();
		}
		r.readPrimitive(header_size);
		r.readPrimitive(content_size);
		r.readPrimitive(width);
		r.readPrimitive(height);

		// cross-check content size with width*height*3
		uint64_t supposedContentSize = width;
		supposedContentSize = checkedMultiplication(supposedContentSize, height);
		supposedContentSize = checkedMultiplication(supposedContentSize, 3);
		if (supposedContentSize != content_size) {
			throw ParsingException();
		}

		caption = r.readUntilChar('\n');

		// TODO this might be unexpected from a parser. Check that it cannot be misused (due to e.g. overflow)
		// -1 because "\n" is dropped from caption (readUntilChar). That one character is still part of the file, however.
		uint64_t tags_size = header_size - sizeof(magic) - sizeof(header_size) - sizeof(content_size) - sizeof(width) - sizeof(height) - caption.size() - 1;
		readTags(r, tags_size);
		// TODO split tags, check whether it contains newline character.
		for (uint64_t i = 0; i < height; i++) {
			for (uint64_t j = 0; j < width; j++) {
				Pixel tmp_pixel;
				tmp_pixel.read(r);
				pixels.push_back(tmp_pixel);
			}
		}
	}
};
