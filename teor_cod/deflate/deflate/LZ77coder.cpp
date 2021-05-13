#include "lz77.hpp"

typedef struct tmp
{
	int shift;
	int length;
	char value;

	std::string print() {
		std::string str = "";
		str.push_back((char)shift);
		str.push_back((char)length);
		str.push_back(value);
		return str;
	}

	void in_null() {
		shift = 0;
		length = 0;
		value = NULL;
	}


}tmp; // хз как назвать


tmp* find(std::string str, std::string buffer, tmp *res) {

	res->in_null();
	res->value = str.at(0);

	tmp* temp = (tmp*)malloc(sizeof(tmp));

	for (int j = 0; j < buffer.length(); j++) {
		
		if (str.at(0) == buffer.at(j)) {
			temp->in_null();
			temp->shift = buffer.length() - j;
		}
		else continue;
		
		do {
			temp->length++;
			if ((j + temp->length == buffer.length()) || (temp->length == str.length())) break;
		}
		while ((str.at(temp->length - 1) == buffer.at(j + temp->length - 1)));

		if (temp->length != str.length())
			temp->value = str.at(temp->length);

		if (temp->length > res->length) {
			res->shift = temp->shift;
			res->length = temp->length;
			res->value = temp->value;
		}
		
	}
	
	free(temp);
	return res;
}


std::string lz77encode(std::string input) {
	std::string buffer = ""; // запоминает все прошедшие символы
	std::string ret = "";

	tmp* res = (tmp*)malloc(sizeof(tmp));

	for (int i = 0; i < input.length(); i += res->length + 1) {
		std::string str = input.substr(i);
		find(str, buffer, res);
		buffer.push_back(res->value);
		ret.append(res->print());
	}

	free(res);
	return ret;
}