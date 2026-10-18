#include <iostream>
#include <string>

struct Table {
    std::string name;
    int width;
    int height;
};

int main() {
    // Creating an instance of the struct
    struct Table myTable;
// Assigning values to the members
    myTable.name = "Coffee table";
    myTable.width = 80;
    myTable.height = 50;
    // Accessing the values
    std::cout << "Table Name: " << myTable.name << std::endl;
    std::cout << "Table Width: " << myTable.width << " cm" << std::endl;
    std::cout << "Table Height: " << myTable.height << " cm" << std::endl;
    return 0;
}