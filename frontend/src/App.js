import React, { useState, useEffect } from "react";
import "./style/App.css";
import ChatHistory from "./components/ChatHistory";
import ChatWindow from "./components/ChatWindow";
import axios from "axios";

function App() {
  const [messages, setMessages] = useState([]); // Chat messages
  const [currentMessage, setCurrentMessage] = useState("");
  const [conversations, setConversations] = useState([]); // Past conversations
  const [currentConversationId, setCurrentConversationId] = useState(null); // Track current conversation ID
  const [isCollapsed, setIsCollapsed] = useState(false); // Collapse state for chat history
  const [isTyping, setIsTyping] = useState(false); // State to manage typing indicator
  const [isDarkMode, setIsDarkMode] = useState(false);

  const toggleTheme = () => {
    setIsDarkMode(!isDarkMode);
  };

  useEffect(() => {
    const fetchConversations = async () => {
      try {
        const response = await fetch("http://localhost:8080/conversations");
        const data = await response.json();
        setConversations(data);
      } catch (err) {
        console.error("Error fetching conversations:", err);
      }
    };

    fetchConversations();
  }, []);

  const startNewConversation = () => {
    setMessages([]); // Clear messages
    setCurrentConversationId(null); // Reset conversation ID
  };

  const loadConversation = async (conversationId) => {
    try {
      const response = await fetch(
        `http://localhost:8080/history/${conversationId}`
      );
      if (!response.ok)
        throw new Error(`Failed to fetch history: ${response.statusText}`);
      const data = await response.json();

      const formattedMessages = data
        .map((history) => [
          { sender: "user", text: history.prompt },
          { sender: "bot", text: history.response },
        ])
        .flat();
      setMessages(formattedMessages);
      setCurrentConversationId(conversationId);
    } catch (err) {
      console.error("Error fetching history:", err);
    }
  };
  const deleteConversation = async (conversationId) => {
    try {
      const response = await axios.delete(
        `http://localhost:8080/history/${conversationId}`
      );

      if (response.status !== 200) {
        throw new Error(`Failed to delete history: ${response.statusText}`);
      }

      setConversations((prevConversations) =>
        prevConversations.filter((conv) => conv.id !== conversationId)
      );

      setMessages([]);
      setCurrentConversationId(null);
    } catch (err) {
      console.error("Error deleting conversation:", err);
    }
  };

  const sendMessage = async () => {
    if (!currentMessage.trim()) return;

    const payload = currentConversationId
      ? { message: currentMessage, conversation_id: currentConversationId }
      : { message: currentMessage };

    const newMessages = [...messages, { sender: "user", text: currentMessage }];
    setMessages(newMessages);
    setCurrentMessage("");
    setIsTyping(true);

    try {
      const response = await fetch("http://localhost:8080/chat", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      const data = await response.json();
      setIsTyping(false);

      setMessages([...newMessages, { sender: "bot", text: data.result }]);
      if (!currentConversationId) {
        const newConversation = {
          id: data.conversation.id,
          title: `${data.conversation.title}`,
        };

        setConversations((prevConversations) => [
          newConversation,
          ...prevConversations,
        ]);

        setCurrentConversationId(newConversation.id);
      }

      if (!currentConversationId) {
        setCurrentConversationId(data.conversation.id);
      }
    } catch (err) {
      console.error("Error sending message:", err);
      setIsTyping(false);
    }
  };

  return (
    <div className={`app-container ${isDarkMode ? "dark" : "light"}`}>
      <div className="theme-toggle">
        <button className="theme-toggle-btn" onClick={toggleTheme}>
          Switch to {isDarkMode ? "Light" : "Dark"} Mode
        </button>
      </div>
      <ChatHistory
        conversations={conversations}
        onConversationSelect={(conversationId) =>
          loadConversation(conversationId)
        }
        onConversationDelete={(conversationId) =>
          deleteConversation(conversationId)
        }
        onNewConversation={startNewConversation}
        isCollapsed={isCollapsed}
        toggleCollapse={() => setIsCollapsed(!isCollapsed)}
        isDarkMode={isDarkMode}
      />

      <ChatWindow
        messages={messages}
        currentMessage={currentMessage}
        setCurrentMessage={setCurrentMessage}
        sendMessage={sendMessage}
        isTyping={isTyping}
        isCollapsed={isCollapsed}
        isDarkMode={isDarkMode}
      />
    </div>
  );
}

export default App;
