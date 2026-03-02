const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const path = require('path');
const db = require('./db_config');

const app = express();
const PORT = 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Serve static files from the current directory
app.use(express.static(__dirname));

// Routes
// 1. Submit Student Registration
app.post('/register', async (req, res) => {
    const { name, email, dob, department, phone } = req.body;

    if (!name || !email || !dob || !department || !phone) {
        return res.status(400).json({ error: 'All fields are required.' });
    }

    try {
        const query = 'INSERT INTO students (name, email, dob, department, phone) VALUES (?, ?, ?, ?, ?)';
        await db.execute(query, [name, email, dob, department, phone]);
        res.status(201).json({ message: 'Student registered successfully!' });
    } catch (err) {
        console.error('Registration Error:', err);
        if (err.code === 'ER_DUP_ENTRY') {
            res.status(409).json({ error: 'Email already exists.' });
        } else {
            res.status(500).json({ error: 'Database error occurred.', details: err.message });
        }
    }
});

// 2. Retrieve All Students
app.get('/students', async (req, res) => {
    try {
        const [rows] = await db.query('SELECT name, email, dob, department, phone FROM students ORDER BY created_at DESC');
        res.json(rows);
    } catch (err) {
        console.error('Retrieval Error:', err);
        res.status(500).json({ error: 'Failed to retrieve records.', details: err.message });
    }
});

// Start Server
app.listen(PORT, () => {
    console.log(`Server is running at http://localhost:${PORT}`);
    console.log(`Open http://localhost:${PORT}/index.html in your browser.`);
});
