#pragma once
#include <cstdio>
#include <string>

//#define READER_DEBUG

#ifdef READER_DEBUG
#include <iostream>
#endif

/// Handling exceptions is forbidden by Google's C/C++ Style Guide
/// Because this is a small project, and it will remain a small project
/// We believe that this rule can be overriden.
class ParsingException : public std::exception {

};

/// C++ wrapper for reading files
/// This is only working well with little-endian machines!
//  Although they did not say that it is little-endian...
class Reader {
    FILE* f;
    uint64_t bytes_read_counter=0;
public:
    //The Rule-Of-Five is followed
    /// takes ownership of FILE* resource(!)
    explicit Reader(FILE* f) : f(f) {}
    //Unused constructors are deleted so they are not misused at a later point
    Reader(Reader& other) = delete;
    Reader& operator=(const Reader& other) = delete;
    Reader(Reader&& fp) noexcept = delete;
    Reader const& operator=(Reader&& fp) = delete;
    //The handling of the file is ensured here
    ~Reader() {
        fclose(f);
    }

    uint64_t getBytesRead() const {
        return bytes_read_counter;
    }

    std::string readStringOfLength(uint64_t length) {
        //used as array for readArray function
        static char buffer[4096];

        // read in chunks so that a too big a read does not waste memory
        std::string result;
        for (uint64_t chunk_start = 0; chunk_start < length; chunk_start += 4096) {
            uint64_t to_read = length-chunk_start;
            if (to_read > 4096) {
                to_read = 4096;
            }
            // No need for null-termination
            // Based on https://stackoverflow.com/a/8438709
            readArray(buffer, to_read);
            result.append(buffer, to_read);
        }
#ifdef READER_DEBUG
        std::cerr << "String (of length " << length << ") read, result [" << result << "]" << std::endl;
#endif
        return result;
    }

    std::string readUntilChar(char end) {
        bool found = false;
        std::string result;
        do
        {
            char tmp;
            if (fread(&tmp, 1, 1, f) != 1) {
                // not enough data
                throw ParsingException();
            }
            bytes_read_counter++;
            if (tmp != end) {
                result += tmp;
            }
            else {
                found = true;
            }
        } while (!found);
#ifdef READER_DEBUG
        std::cerr << "String (terminator " << end << ") read, result [" << result << "]" << std::endl;
#endif
        return result;
    }

    template<typename T>
    void readPrimitive(T& data) {
        if (fread(&data, sizeof(T), 1, f) != 1) {
            // not enough data
            throw ParsingException();
        }

        bytes_read_counter += sizeof(T);
#ifdef READER_DEBUG
        std::cerr << "Primitive (size " << sizeof(T) << ") read: " << (uint64_t)data << std::endl;
#endif
    }

    // The array needs to be allocated beforehand!
    template<typename T>
    void readArray(T* array, uint64_t len) {
        if (fread(array, sizeof(T), len, f) != len) {
            throw ParsingException();
        }
        bytes_read_counter += sizeof(T)*len;
#ifdef READER_DEBUG
        std::cerr << "Primitive (size " << sizeof(T) << ", length " << len << ") read";
        if (len != 0) {
            std::cerr << "; first data is: " << array[0];
        }
        std::cerr << std::endl;
#endif
    }
};