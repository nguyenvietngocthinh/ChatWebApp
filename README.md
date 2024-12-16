# ChatWeb

## Overview
ChatWeb is a real-time messaging application designed for seamless communication and enhanced security. It features OTP-based user authentication and secure cloud storage for user images, leveraging AWS services.

## Features
- **Real-Time Messaging**: Enables users to exchange messages instantly.
- **OTP Email Verification**: Secure authentication using Firebase.
- **Secure Image Management**: AWS S3 integration for uploading and storing user images.

## Tech Stack
- **Backend**: Spring Boot, MySQL (hosted on AWS RDS), Firebase.
- **Frontend**: HTML, CSS, JavaScript, Bootstrap 4.
- **Cloud Services**: AWS S3, IAM for secure storage and access management.

## Deployment
The application is deployed on Render for a scalable and cost-effective hosting solution.

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/nguyenvietngocthinh/ChatWebApp.git
   ```
2. Set up the database on AWS RDS with the required schema.
3. Configure Firebase and AWS S3 credentials in the application.
4. Run the backend server:
   ```bash
   ./mvnw spring-boot:run
   ```
5. Open the frontend in a browser.

## How to Use
1. Register a new account using a valid email.
2. Log in with OTP verification.
3. Start real-time chats with other registered users.

## Project Highlights
- **Scalable Architecture**: Leveraged AWS RDS and S3 for scalability and reliability.
- **Enhanced Security**: Utilized Firebase for secure OTP authentication and IAM for role-based access control.
- **Cloud Deployment**: Easy and efficient hosting on Render.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

---

**GitHub Repository**: [ChatWebApp](https://github.com/nguyenvietngocthinh/ChatWebApp.git)
