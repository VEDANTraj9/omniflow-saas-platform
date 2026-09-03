import express from "express";
import http from "http";
import { Server } from "socket.io";
import Redis from "ioredis";
import cors from "cors";
import dotenv from "dotenv";

dotenv.config();

const app = express();
const server = http.createServer(app);

// CORS configuration for REST & WebSockets
app.use(cors({ origin: process.env.CLIENT_ORIGIN || "*" }));
app.use(express.json());

// Initialize Socket.IO
const io = new Server(server, {
  cors: {
    origin: process.env.CLIENT_ORIGIN || "*",
    methods: ["GET", "POST"],
  },
});

// Setup Redis Subscriber Client
const redisHost = process.env.REDIS_HOST || "127.0.0.1";
const redisPort = Number(process.env.REDIS_PORT) || 6379;

const redisSubscriber = new Redis({
  host: redisHost,
  port: redisPort,
  retryStrategy(times) {
    const delay = Math.min(times * 1000, 3000);
    return delay;
  },
});

redisSubscriber.on("connect", () => {
  console.log("✅ Connected to Redis successfully");
});

redisSubscriber.on("error", (err) => {
  console.warn(
    "⚠️ Redis connection issue (events listening paused):",
    err.message,
  );
});

// Subscribe to Spring Boot event channel
redisSubscriber.subscribe("invoice-events", (err, count) => {
  if (err) {
    console.error("Failed to subscribe to invoice-events:", err);
  } else {
    console.log(
      `📡 Subscribed to invoice-events channel. Active subscriptions: ${count}`,
    );
  }
});

// Broadcast Redis messages to connected Socket.IO clients
redisSubscriber.on("message", (channel, message) => {
  if (channel === "invoice-events") {
    console.log("⚡ Redis Event Received:", message);
    io.emit("invoice_created", {
      message: message,
      timestamp: new Date().toISOString(),
    });
  }
});

// WebSocket Client Connection Lifecycle
io.on("connection", (socket) => {
  console.log(`🔌 Client connected: ${socket.id}`);

  socket.on("disconnect", () => {
    console.log(`❌ Client disconnected: ${socket.id}`);
  });
});

// Health check endpoint
app.get("/health", (req, res) => {
  res.status(200).json({ status: "UP", service: "Realtime-Socket-Gateway" });
});

const PORT = process.env.PORT || 5000;
server.listen(PORT, () => {
  console.log(`🚀 Real-time service running on http://localhost:${PORT}`);
});

