
# Chatbot App with Llama Model

This project is a chatbot application built using **Spring Boot**, **React**, **PostgreSQL**, and the **Llama model**. It is fully Dockerized and supports easy deployment with Docker Compose.

## Features
- Chat functionality powered by the Llama model.
- Have conversation logic, remembers past conversations
- You can view past conversations, continue and delete.
- Frontend built with React. Have light and dark mode 
- Backend built with Spring Boot.
- PostgreSQL for database management.
- Dockerized setup and deployment.

## Demonstration
[Video](https://www.youtube.com/watch?v=NZz0bjvUbBQ)
![Screenshot 2024-12-23 at 02 49 41](https://github.com/user-attachments/assets/c30e290e-a01d-4e01-bf30-bb0c1e1f0e7d)
![Screenshot 2024-12-23 at 02 52 06](https://github.com/user-attachments/assets/f22a170c-c537-4201-abea-f452a7faf654)


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
## 1.Running the Llama Model Locally

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
### Option 1: Using Docker Compose

  Build and run the application using Docker Compose:
```bash
docker-compose up --build
```
### Option 2: Running the .jar File Directly
If you prefer not to use Docker, you can run the application directly by executing the .jar file:

```bash
java -jar llama2-1.jar
```
## 4. Access the Application

* Frontend: http://localhost
* Backend: http://localhost:8080
* Database: localhost:5432

## API Endpoints

* `POST /chat`: Endpoint for sending and receiving chatbot messages.
* `GET /conversations` Retrieves a list of all conversations.
* `GET /history/{conversationId}`:  Retrieve chat history for a conversation.
* `DELETE /history/{conversationId}`: Deletes a conversation and its associated history by conversationId.

## Environment Variables

### Database connection details
* spring.datasource.url=jdbc:postgresql://database:5432/llamadb  # Database connection URL.
* spring.datasource.username=postgres                            # Database username.
* spring.datasource.password=admin                               # Database password.

### AI service configuration
* spring.ai.ollama.chat.model=Llama model                        # The name of the Llama model to be used for chat.
* spring.ai.ollama.base-url=http://host.docker.internal:11434    # Base URL for the Ollama AI service (required only when running in Docker). You can skip this if running locally.

