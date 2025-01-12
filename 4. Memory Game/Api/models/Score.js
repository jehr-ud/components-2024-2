const mongoose = require("mongoose");

const ScoreSchema = new mongoose.Schema({
    player_uuid: { type: String, required: true },
    score: { type: Number, required: true },
    date: { type: Date, default: Date.now },
});

module.exports = mongoose.model("Score", ScoreSchema);
