const db = require('./db_config');

async function run() {
    try {
        console.log("Triggering INSERT action...");
        const [insertRes] = await db.execute("INSERT INTO employees_audit_test (name, salary) VALUES ('John Doe', 50000.00)");
        const insertedId = insertRes.insertId;

        console.log("Triggering UPDATE action...");
        await db.execute("UPDATE employees_audit_test SET salary = 55000.00 WHERE id = ?", [insertedId]);

        console.log("\n--- Checking Audit Logs ---");
        const [logs] = await db.execute("SELECT * FROM audit_logs ORDER BY action_timestamp DESC LIMIT 2");
        console.log(logs);

        console.log("\n--- Checking Daily Activity Report View ---");
        const [report] = await db.execute("SELECT * FROM daily_activity_report");
        console.table(report);

    } catch (err) {
        console.error(err);
    } finally {
        process.exit();
    }
}

run();
