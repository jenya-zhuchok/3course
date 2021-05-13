#include "deflate.hpp"


void encoder(std::string filename) {
	
	std::ifstream in;
	std::ofstream out;

	in.open(filename, std::ios::binary);
	out.open(filename + ".def", std::ios::binary);


	std::string input((std::istreambuf_iterator<char>(in)), std::istreambuf_iterator<char>());
	

	for (int i = 0; i < FILESIZE; i += BLOCKSIZE) {
		std::string buffer = lz77encode(input.substr(i, BLOCKSIZE));
		std::string output = hafencode(buffer, (i == (FILESIZE - BLOCKSIZE)) ? 1 : 0);
		out << output;
	}

	in.close();
	out.close();
}