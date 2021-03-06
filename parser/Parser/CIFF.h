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
	std::string magic;
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
		uint64_t newline = tmp_tags.find('\n');
		if (newline != std::string::npos) {
			// newline found in tags
			throw ParsingException();
		}
		uint64_t offset = 0;
		while (offset < tmp_tags.size()) {
			uint64_t end = tmp_tags.find('\0', offset);
			if (end == std::string::npos) {
				// probably missing NUL char after the last tag
				throw ParsingException();
			}
			std::string tmp_tag;
			tmp_tag.assign(tmp_tags, offset, end-offset);
			tags.push_back(tmp_tag);
			offset = end+1;
		}
		// everything in order
	}

public:

	void read(Reader& r) {
		magic = r.readStringOfLength(4);
		if (magic != "CIFF") {
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

		// -1 because "\n" is dropped from caption (readUntilChar). That one character is still part of the file, however.
		uint64_t header_size_without_tags = 4 + sizeof(header_size) + sizeof(content_size) + sizeof(width) + sizeof(height) + caption.size() + 1;
		if (header_size < header_size_without_tags) {
			throw ParsingException();
		}
		uint64_t tags_size = header_size - header_size_without_tags;
		readTags(r, tags_size);

		pixels.resize(width*height);
		Pixel* firstElem = &(pixels[0]);
		r.readArray(firstElem, width*height);
	}
};
