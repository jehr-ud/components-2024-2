const express = require("express");
const router = express.Router();
const Score = require("../models/Score");

// get scores
router.get("/average/:player_uuid", async (req, res) => {
    const { player_uuid } = req.params;

    try {
        const result = await Score.aggregate([
            { $match: { player_uuid: player_uuid } }, // Filtra por el nombre del jugador
            {
                $group: {
                    _id: "$player_uuid",
                    averageScore: { $avg: "$score" }, // Calcula el promedio
                },
            },
        ]);

        if (result.length === 0) {
            return res.status(404).json({ message: "Jugador no encontrado" });
        }

        res.json({ player_uuid, averageScore: result[0].averageScore });
    } catch (err) {
        console.error(err.message);
        res.status(500).send("Error al calcular el promedio");
    }
});

// Store new score
router.post("/", async (req, res) => {
    const { player_uuid, game_uuid, score } = req.body;
    try {
        const newScore = new Score({ player_uuid, game_uuid, score });
        await newScore.save();
        res.status(201).json(newScore);
    } catch (err) {
        console.log(err);
        res.status(500).send("Error al guardar el puntaje");
    }
});

module.exports = router;
