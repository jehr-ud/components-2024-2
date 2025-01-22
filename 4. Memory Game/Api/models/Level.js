const mongoose = require("mongoose");


const levelEnum = ['low', 'medium', 'high'];


const LevelSchema = new mongoose.Schema({
    player_uuid: { type: String, required: true },
    level: { type: String, enum: levelEnum, default: 'low' },
    date: { type: Date, default: Date.now },
});

module.exports = mongoose.model("Level", LevelSchema);
