📌 Feedback Survey System – README
📖 Project Overview

The Feedback Survey System is a Java-based application designed to collect and analyze user feedback efficiently.
It provides structured storage of survey data and supports generating visual and statistical summaries of responses.

🧾 Features
Collects structured survey responses
Ensures data integrity and consistency
Generates summary statistics of collected feedback
Supports graphical representation of results
Uses MongoDB for backend data storage

📂 Repository Structure
├── src/
│   └── FeedbackSurveySystem.java     # Main application file
├── lib/                              # External dependencies (MongoDB driver JARs)
├── README.md                         # Project documentation

🛠️ Technologies Used
Component	Technology
Programming Language	:Java
Database            	:MongoDB
Drivers/Dependencies	:MongoDB Java Driver


🔧 Setup & Installation
1️⃣ Clone the Repository
git clone https://github.com/your-username/FeedbackSurveySystem.git
cd FeedbackSurveySystem


2️⃣ Add Required Libraries

Ensure lib/ folder contains the following JARs:

bson-4.10.2.jar

mongodb-driver-core-4.10.2.jar

mongodb-driver-sync-4.10.2.jar



3️⃣ Compile the Project
javac -cp "lib/*" src/FeedbackSurveySystem.java

4️⃣ Run the Application
java -cp "lib/*:src" FeedbackSurveySystem

