#pragma once
class CIFF {
	char magic[4];
	uint64_t header_size;
	uint64_t content_size;
	uint64_t width;
	uint64_t height;
	char* caption;
	char* tags;
	char* content;
public:
	void read(Reader& r) {
		r.readArray(magic, 4);
		r.readPrimitive(header_size);
		r.readPrimitive(content_size);
		r.readPrimitive(width);
		r.readPrimitive(height);

	}
};
