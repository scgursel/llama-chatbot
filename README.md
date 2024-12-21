
# Chatbot App with Llama Model

This project is a chatbot application built using **Spring Boot**, **React**, **PostgreSQL**, and the **Llama model**. It is fully Dockerized and supports easy deployment with Docker Compose.

## Features
- Chat functionality powered by the Llama model.
- Frontend built with React.
- Backend built with Spring Boot.
- PostgreSQL for database management.
- Dockerized setup and deployment.

## Project Structure
```plaintext
.
├── backend/            # Spring Boot application
│   ├── src/main/java   # Java source files
│   ├── src/main/resources/static  # React build files
│   └── Dockerfile      # Docker configuration for backend
├── frontend/           # React application
│   ├── src/            # React source files
│   ├── build/          # Production build output
│   └── Dockerfile      # Docker configuration for frontend
├── docker-compose.yml  # Docker Compose configuration
└── README.md           # Project documentation
```

## Prerequisites
Before setting up the project, make sure you have the following installed on your system:

- **Docker** and **Docker Compose** for containerization and orchestration.
- **Java Development Kit (JDK)** for Spring Boot backend development.
- **Node.js** and **npm** for React frontend development.
- **Ollama** for running the Llama2:7B or any llama model locally.


# Setup Instructions
## 1.Running the Llama2:7B Model Locally

### 1. Install Ollama
To run the Llama2:7B model locally, you'll need to use Ollama. You can get started by installing the Ollama CLI.

- **Installation Instructions:**
    - Go to the official Ollama website: [Ollama](https://ollama.com/).
    - Follow the instructions for your operating system.

### 2. Run Llama2:7B Model
Once you have Ollama installed, you can run any llama model with the following command:

```bash
ollama run llama2:7b
```
This will start the Llama2:7B model locally on your machine, and you can interact with it using the specified APIs.

### 3. Example Request
Once the Llama2:7B model is running, you can use your backend API to interact with it. For example, if you're sending a prompt to the model:
```bash
curl -X POST http://localhost:8080/chat -H "Content-Type: application/json" -d '{"message": "Hello, Llama!"}'
```
The backend will forward the request to the Llama2:7B model and return the response.
For more details on the Llama2:7B model and how to use it with Ollama, visit [Ollama Llama2:7B Documentation](https://ollama.com/library/llama2:7b) 


## 2. Clone the Repository
Clone the repository to your local machine:

```bash
git clone https://github.com/scgursel/llama-chatbot.git
cd llama-chatbot
```

## 3. Build and Run the Application
Build and run the application using Docker Compose:
```bash
docker-compose up --build
```

## 4. Access the Application

* Frontend: http://localhost
* Backend: http://localhost:8080
* Database: localhost:5432

## API Endpoints

* /chat: Endpoint for sending and receiving chatbot messages.
* /history/{conversationId}: Retrieve chat history for a conversation.
## Environment Variables

* spring.datasource.username: Database connection URL.
* spring.datasource.password: Database username.
* spring.datasource.url: Database password.
* spring.ai.ollama.chat.model: Llama model 


