#pragma once
#include <cstdio>
#include <string>

class ParsingException : public std::exception {

};

/// C++ wrapper for reading files
/// TODO This is only working well with little-endian machines!
//    Although they did not say that it is little-endian...
class Reader {
    FILE* f;
public:
    /// takes ownership of FILE* resource(!)
    Reader(FILE* f) : f(f) {}
    ~Reader() {
        fclose(f);
    }

    std::string readStringOfLength(uint64_t length) {
        static char buffer[4096];

        // read in chunks so that a too big a read does not waste memory
        std::string result;
        for (uint64_t chunk_start = 0; chunk_start < length; chunk_start += 4096) {
            uint64_t to_read = length-chunk_start;
            if (to_read > 4096) {
                to_read = 4096;
            }
            readArray(buffer, length);
            result.append(buffer, length);
        }
        // No need for null-termination
        // Based on https://stackoverflow.com/a/8438709
        return result;
    }

    std::string readUntilChar(char end) {
        bool found = false;
        std::string result;
        do
        {
            char tmp;
            fread(&tmp, 1, 1, f);
            if (tmp != end) {
                result += tmp;
            }
            else {
                found = true;
            }
        } while (!found);
        return result;
    }

    template<typename T>
    void readPrimitive(T& data) {
        fread(&data, sizeof(T), 1, f);
    }

    // The array needs to be allocated beforehand!
    template<typename T>
    void readArray(T* array, int len) {
        fread(array, sizeof(T), len, f);
    }
};