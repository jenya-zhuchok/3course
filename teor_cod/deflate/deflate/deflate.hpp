
#ifndef _DEFLATE_

#define _DEFLATE_

#include <string>
#include <fstream>

#include "lz77.hpp"
#include "haf.hpp"

constexpr auto FILESIZE = 6144;
constexpr auto BLOCKSIZE = 2048;

void encoder(std::string filename);

#endif // !_DEFLATE_
