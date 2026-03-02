const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const db = require('./db_config');

const app = express();
const PORT = 3002;

app.use(cors());
app.use(bodyParser.json());
app.use(express.static(__dirname));

// Login Endpoint
app.post('/api/login', async (req, res) => {
    const { username, password } = req.body;

    if (!username || !password) {
        return res.status(400).json({ success: false, message: 'Username and password are required.' });
    }

    try {
        const query = 'SELECT * FROM users WHERE username = ? AND password = ?';
        const [rows] = await db.query(query, [username, password]);

        if (rows.length > 0) {
            res.json({ success: true, message: 'Login successful!', user: rows[0].username });
        } else {
            res.status(401).json({ success: false, message: 'Invalid username or password.' });
        }
    } catch (err) {
        console.error(err);
        res.status(500).json({ success: false, message: 'Server error occurred.' });
    }
});

app.listen(PORT, () => {
    console.log(`Task 3 Server running at http://localhost:${PORT}`);
});
