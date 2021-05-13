#include "header.hpp"


std::string generate() {
	std::string filename = "output/" + std::to_string(time(0)) + ".test";
	std::ofstream output(filename);
	srand(time(0));
	for (int i = 0; i < FILESIZE; i++) {
		output << (unsigned char) (rand() % 256);
	}

	output.close();

	return filename;
}


int compair(std::string first, std::string second) {
	std::ifstream i1(first), i2(second);
	if (!i1 || !i2) { std::cerr << "File not found"; return -1; }
	std::string s1((std::istreambuf_iterator<char>(i1)), std::istreambuf_iterator<char>());
	std::string s2((std::istreambuf_iterator<char>(i2)), std::istreambuf_iterator<char>());
	i1.close();
	i2.close();
	if (s1 == s2) return 0;
	else return 1;
}
