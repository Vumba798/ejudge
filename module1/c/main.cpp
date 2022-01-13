#include <cstdint>
#include <iostream>
#include <fstream>
#include <string>

int main(int argc, char** argv) {
    int size = 0;
    int currentSize = 0;
    bool stringIsOkay = true;
    std::string inputLine;

    std::ifstream input;
    std::ofstream output;
    input.open(argv[1]);
    output.open(argv[2]);

    while (std::getline(input, inputLine)) {
        
        if (inputLine.size() > 9 && inputLine.substr(0, 9) == "set_size ") {
            for (std::uint32_t i = 9; i < inputLine.size(); ++i) {
                if (!isdigit(inputLine[i])) {
                    stringIsOkay = false;
                    output << "error\n";
                    break;
                }
            }
            if (stringIsOkay) {
                size = std::stoi(inputLine.substr(9, inputLine.size()));
                break;
            }
        } else if (!inputLine.empty()) {
            output << "error\n";
        }
    }

    std::string* data = new std::string[size];
    std::uint32_t headIndex = 0;
    std::uint32_t tailIndex = 0;

    while (std::getline(input, inputLine)) {
        if (inputLine == "pop") {
            if (currentSize == 0) {
                output << "underflow\n";
            } else {
                output << data[headIndex] << "\n";
                headIndex = (headIndex +  1) % size;
                currentSize--;
            }
        } else if (inputLine == "print") {
            if (currentSize == 0) {
                output << "empty\n";
            } else {
                std::uint32_t tmpHead = headIndex;
                output << data[tmpHead];
                tmpHead = (tmpHead + 1) % size;
                while (tmpHead != tailIndex) {
                    output << " " << data[tmpHead];
                    tmpHead = (tmpHead + 1) % size;
                }
                output << "\n";
            }
        } else if (inputLine.size() > 5 && inputLine.substr(0, 5) == "push ") {
            stringIsOkay = true;
            for (std::uint32_t i = 5; i < inputLine.size(); ++i) {
                if (std::isspace(inputLine[i])) {
                    stringIsOkay = false;
                    output << "error\n";
                    break;
                }
            }
            if (currentSize >= size && stringIsOkay) {
                output << "overflow\n";
                continue;
            } else if (stringIsOkay) {
                data[tailIndex] = inputLine.substr(5, inputLine.size());
                tailIndex = (tailIndex +1) % size;
                currentSize++;
            }
        } else if (!inputLine.empty()) {
            output << "error\n";
        }
    }

    input.close();
    output.close();
    delete[] data;
  
    return 0;
}
