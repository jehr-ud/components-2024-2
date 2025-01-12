const mongoose = require("mongoose");

const LevelSchema = new mongoose.Schema({
    player_uuid: { type: String, required: true },
    level: { type: Number, required: true },
    date: { type: Date, default: Date.now },
});

module.exports = mongoose.model("Level", LevelSchema);
