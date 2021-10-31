#pragma once
class CIFF {
	char magic[4];
	unsigned int header_size;
	unsigned int content_size;
	unsigned int width;
	unsigned int height;
	char* caption;
	char* tags;
	char* content;
};
