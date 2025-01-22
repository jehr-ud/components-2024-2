const express = require("express");
const router = express.Router();
const Level = require("../models/Level");

// get levels
router.get("/:player_uuid", async (req, res) => {
    try {
        const { player_uuid } = req.params;
        let levels = await Level.find({player_uuid});

        if (!levels.length){
            console.log("no hay niveles")
            const newLevel = new Level({ player_uuid });
            await newLevel.save();

            levels = [
                newLevel
            ]
        }

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
        
        await newLevel.save()
        .then(user => {
            console.log('Level creado:', user);
        })
        .catch(err => {
            console.error('Error al crear nivel:', err);
            res.status(422).json(err);
        });

        res.status(201).json(newLevel);
    } catch (err) {
        res.status(500).send("Error al guardar el nivel");
    }
});

module.exports = router;
