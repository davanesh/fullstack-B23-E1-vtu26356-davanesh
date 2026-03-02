const db = require('./db_config');

async function testConnection() {
    try {
        console.log('Testing connection to MySQL...');
        const [rows] = await db.query('SHOW TABLES LIKE "students"');
        if (rows.length > 0) {
            console.log('SUCCESS: Connection established and "students" table exists.');
        } else {
            console.log('PARTIAL SUCCESS: Connection established, but "students" table is MISSING.');
            console.log('Attempting to create "students" table...');
            const fs = require('fs');
            const sql = fs.readFileSync('./create_table.sql', 'utf8');
            // Split by semicolon and filter empty lines
            const commands = sql.split(';').filter(cmd => cmd.trim());
            for (let cmd of commands) {
                await db.query(cmd);
            }
            console.log('SUCCESS: "students" table created.');
        }
        process.exit(0);
    } catch (err) {
        console.error('ERROR: Could not connect to MySQL.');
        console.error('Details:', err.message);
        process.exit(1);
    }
}

testConnection();
