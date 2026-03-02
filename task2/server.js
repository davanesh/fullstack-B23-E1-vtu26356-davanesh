const express = require('express');
const path = require('path');
const db = require('./db_config');

const app = express();
const PORT = 3001; // Using a different port for Task 2

app.use(express.static(__dirname));
app.use(express.json());

// Get stats: count per department
app.get('/api/stats', async (req, res) => {
    try {
        const query = 'SELECT department, COUNT(*) as count FROM students GROUP BY department';
        const [rows] = await db.query(query);
        res.json(rows);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Failed to fetch stats' });
    }
});

// Get students with sorting and filtering
app.get('/api/students', async (req, res) => {
    const { sort, dept } = req.query; // sort: 'name' or 'dob', dept: department name

    let query = 'SELECT * FROM students';
    const params = [];

    if (dept) {
        query += ' WHERE department = ?';
        params.push(dept);
    }

    if (sort === 'name') {
        query += ' ORDER BY name ASC';
    } else if (sort === 'date') {
        query += ' ORDER BY created_at DESC';
    } else if (sort === 'dob') {
        query += ' ORDER BY dob DESC';
    }

    try {
        const [rows] = await db.query(query, params);
        res.json(rows);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Failed to fetch students' });
    }
});

app.listen(PORT, () => {
    console.log(`Task 2 Server running at http://localhost:${PORT}`);
});
