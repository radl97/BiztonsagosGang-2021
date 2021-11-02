#pragma once

#include "Reader.h"
#include <iostream>

class CIFF {

public:
	char magic[4];
	uint64_t header_size;
	uint64_t content_size;
	uint64_t width;
	uint64_t height;
	std::string caption;
	std::string tags;
	std::string content;

	void read(Reader& r) {
		r.readArray(magic, 4);
		r.readPrimitive(header_size);
		r.readPrimitive(content_size);
		r.readPrimitive(width);
		r.readPrimitive(height);
		caption = r.readUntilChar('\n');
		int tags_size = header_size - sizeof(magic) - sizeof(header_size) - sizeof(content_size) - sizeof(width) - sizeof(height) - sizeof(caption);
		char* tmp_tags = new char[header_size];
		r.readArray(tmp_tags, header_size);
		tags.assign(tmp_tags);
		char* tmp_content = new char[content_size];
		r.readArray(tmp_content, content_size);
		content.assign(tmp_content);

	}
};
