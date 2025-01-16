const mongoose = require("mongoose");

const ScoreSchema = new mongoose.Schema({
    player_uuid: { type: String, required: true },
    game_uuid: { type: String, required: true}, //Se agrega el uuid del juego en el esquema
    score: { type: Number, required: true },
    date: { type: Date, default: Date.now },
});

module.exports = mongoose.model("Score", ScoreSchema);
