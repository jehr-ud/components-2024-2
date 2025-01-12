const express = require("express");
const bodyParser = require("body-parser");
const dotenv = require("dotenv");
const connectDB = require("./config/db");

// Load config
dotenv.config();

connectDB();

const app = express();

// Middleware
app.use(bodyParser.json());

// Routes
app.use("/api/v1/scores", require("./routes/scores"));
app.use("/api/v1/levels", require("./routes/levels"));

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
