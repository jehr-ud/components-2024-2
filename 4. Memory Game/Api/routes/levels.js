const express = require("express");
const router = express.Router();
const Level = require("../models/Level");

// get levels
router.get("/", async (req, res) => {
    try {
        const levels = await Level.find();
        res.json(levels);
    } catch (err) {
        res.status(500).send("Error al obtener los niveles");
    }
});

// store level
router.post("/", async (req, res) => {
    const { player_uuid, level } = req.body;
    try {
        const newLevel = new Level({ player_uuid, level });
        await newLevel.save();
        res.status(201).json(newLevel);
    } catch (err) {
        res.status(500).send("Error al guardar el nivel");
    }
});

module.exports = router;
