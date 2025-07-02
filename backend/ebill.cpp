#include <iostream>
#include <fstream>
#include <string>
#include <iomanip> 
using namespace std;

// Function to calculate bill amount based on units
float calculateBill(int units) {
    if (units <= 100)
        return units * 1.5;
    else if (units <= 300)
        return 100 * 1.5 + (units - 100) * 2.5;
    else if (units <= 500)
        return 100 * 1.5 + 200 * 2.5 + (units - 300) * 4;
    else
        return 100 * 1.5 + 200 * 2.5 + 200 * 4 + (units - 500) * 6;
}

// Function to add a new bill
void addBill() {
    ofstream fout("../ebills.txt", ios::app);
    if (!fout) {
        cout << "Error opening file for writing.\n";
        return;
    }

    string id, name, date;
    int units;
    cout << "Enter Consumer ID: ";
    cin >> id;
    cout << "Enter Name: ";
    cin >> name;
    cout << "Enter Units: ";
    cin >> units;
    cout << "Enter Date (YYYY-MM-DD): ";
    cin >> date;

    float amount = calculateBill(units);

    fout << id << "|" << name << "|" << units << "|" << amount << "|" << date << endl;
    fout.close();
    cout << "Bill added successfully!\n";
}

// Function to view all bills

void viewBills() {
    ifstream fin("ebills.txt");
    if (!fin) {
        cout << "No bills found or file can't be opened.\n";
        return;
    }

    string id, name, date;
    int units;
    float amount;

    cout << "\n" << left
         << setw(12) << "ID"
         << setw(15) << "Name"
         << setw(10) << "Units"
         << setw(10) << "Amount"
         << setw(12) << "Date" << endl;
    cout << "----------------------------------------------------------\n";

    string line;
    while (getline(fin, line)) {
        size_t pos = 0;
        int field = 0;
        string fields[5];

        for (int i = 0; i < 5 && pos != string::npos; i++) {
            size_t next = line.find('|', pos);
            fields[i] = line.substr(pos, next - pos);
            pos = (next == string::npos) ? string::npos : next + 1;
        }

        cout << left
             << setw(12) << fields[0]
             << setw(15) << fields[1]
             << setw(10) << fields[2]
             << setw(10) << fields[3]
             << setw(12) << fields[4] << endl;
    }

    fin.close();
}


// Main function with menu
int main() {
    int choice;
    while (true) {
        cout << "\n1. Add Bill\n2. View Bills\n3. Exit\nChoice: ";
        cin >> choice;

        if (choice == 1)
            addBill();
        else if (choice == 2)
            viewBills();
        else if (choice == 3)
            break;
        else
            cout << "Invalid choice. Try again.\n";
    }
    return 0;
}

