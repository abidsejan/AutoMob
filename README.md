# AutoMob - Car Inventory Management System

## 🚗 About AutoMob

AutoMob is a comprehensive car inventory management system built with Java Swing. It provides a dual-interface platform for administrators and customers to manage car inventory, process purchases, and generate sales reports. The system features user authentication, inventory management, purchase approval workflows, and detailed sales analytics.

## ✨ Features

### Admin Features
- **Inventory Management**: Add, remove, and update car types and quantities
- **Purchase Approval**: Review and approve/reject customer purchase requests
- **Sales Analytics**: Generate daily and date-range sales reports
- **Transaction Tracking**: Complete audit trail of all system activities
- **User Management**: Create and manage customer and admin accounts

### Customer Features
- **Car Browsing**: View available cars with pricing and stock information
- **Purchase Requests**: Submit purchase requests for admin approval
- **Account Management**: Secure login and account access

### System Features
- **User Authentication**: Secure login system with role-based access
- **Data Persistence**: All data is saved to local files
- **Responsive UI**: Clean, modern interface with rounded components
- **Real-time Updates**: Inventory and status updates in real-time

## 🖼️ Screenshots

### Login Screen
<img width="1012" height="612" alt="Screenshot 2025-09-18 at 11 27 19 AM" src="https://github.com/user-attachments/assets/79b97d1c-3fec-4a70-bfb3-dbbb1c58900b" />
<img width="712" height="612" alt="Screenshot 2025-09-18 at 11 27 27 AM" src="https://github.com/user-attachments/assets/6239341b-b79a-46e9-8cab-5b877e94323e" />

### Admin Dashboard
 - Inventory Management
<img width="1012" height="762" alt="Screenshot 2025-09-18 at 11 26 10 AM" src="https://github.com/user-attachments/assets/20de788b-ca79-4875-81c0-6ddb9a00b24c" />

### Admin Dashboard - Pending Purchases
<img width="1012" height="762" alt="Screenshot 2025-09-18 at 11 26 17 AM" src="https://github.com/user-attachments/assets/a404f7e0-07b0-4d09-b832-cedc080aa0f8" />


### Admin Dashboard - Sales Reports
<img width="1012" height="762" alt="Screenshot 2025-09-18 at 11 26 42 AM" src="https://github.com/user-attachments/assets/3cb438a5-775e-41d8-98e5-6cc03a4134a4" />

### Customer Interface
<img width="1012" height="762" alt="Screenshot 2025-09-18 at 11 25 30 AM" src="https://github.com/user-attachments/assets/f1c82814-7185-46ce-8a62-0509037dbbb2" />

## 🚀 Getting Started

### Prerequisites
- Java JDK 8 or higher
- No external libraries required

### Installation
1. Clone the repository
```bash
git clone https://github.com/abidsejan/AutoMob.git
```

2. Navigate to the project directory
```bash
cd AutoMob
```

3. Compile the Java files
```bash
javac -d . Start.java
```

4. Run the application
```bash
java Start
```

## 📁 Project Structure

```
AutoMob/
├── Components/              # Custom UI components
│   ├── RoundedButton.java   # Rounded button implementation
│   ├── RoundedTextField.java # Rounded text field implementation
│   └── RoundedPasswordField.java # Rounded password field implementation
├── Entities/                # Data model classes
│   ├── Account.java         # User account management
│   └── PendingPurchase.java # Purchase request model
├── Frame/                   # Main application frames
│   ├── Frame.java           # Login frame
│   └── Homepage.java        # Main dashboard
├── Login/                   # Authentication components
│   └── RegisterFrame.java   # User registration
├── Pics/                    # Images and data files
│   ├── car.jpg
|   |__ automob.png              # Application logo
│── car_data.txt             # Car inventory data
├── Data.txt                 # User account data
├── pending_purchases.txt    # Pending purchase data
└── transactions.txt         # Transaction history
└── Start.java               # Application entry point
```

## 📖 Usage

### For Administrators
1. Log in with admin credentials
2. Use the "Inventory Management" tab to:
   - Add new car types with pricing
   - Update existing car quantities
   - Remove car types
3. Use the "Pending Purchases" tab to:
   - Review customer purchase requests
   - Approve or reject purchases
4. Use the "Sales Reports" tab to:
   - Generate daily sales reports
   - Create date-range sales reports

### For Customers
1. Log in with customer credentials
2. Browse available cars in the main dashboard
3. Select a car and quantity to purchase
4. Submit purchase request (requires admin approval)
5. Wait for admin approval confirmation

## 💾 Data Storage

The application uses plain text files for data persistence:

- **car_data.txt**: Stores car inventory information (type, quantity, price)
- **Data.txt**: Stores user account information (username, password, user type)
- **pending_purchases.txt**: Stores pending purchase requests
- **transactions.txt**: Records all system transactions and audit logs

## 🎨 Customization

### Adding New UI Components
1. Create new component classes in the `Components` package
2. Extend existing Swing components
3. Implement custom painting for rounded corners

### Extending Functionality
1. Add new methods to existing classes
2. Create new entity classes in the `Entities` package
3. Update the UI in the `Frame` classes

### Styling
- Modify color schemes in the UI initialization code
- Update font settings in the component creation methods
- Customize the rounded corner radius in the `Components` classes
