const db = require('./db_config');

async function processPayment(userId, merchantId, amount) {
    const connection = await db.getConnection();
    try {
        // Start transaction
        await connection.beginTransaction();
        console.log('Transaction started.');

        // 1. Check user balance
        const [userRows] = await connection.execute('SELECT balance FROM user_accounts WHERE id = ? FOR UPDATE', [userId]);
        if (userRows.length === 0) throw new Error('User not found');
        const userBalance = userRows[0].balance;

        if (userBalance < amount) {
            throw new Error('Insufficient balance');
        }

        // 2. Deduct from user
        await connection.execute('UPDATE user_accounts SET balance = balance - ? WHERE id = ?', [amount, userId]);
        console.log(`Deducted ${amount} from user account.`);

        // 3. Add to merchant
        await connection.execute('UPDATE merchant_accounts SET balance = balance + ? WHERE id = ?', [amount, merchantId]);
        console.log(`Added ${amount} to merchant account.`);

        // Commit transaction
        await connection.commit();
        console.log('Transaction committed successfully. Payment successful!');
    } catch (error) {
        // Rollback on failure
        await connection.rollback();
        console.error('Transaction failed. Rollback executed.', error.message);
    } finally {
        connection.release();
    }
}

// Example Run
async function run() {
    console.log('Processing payment of 50.00 from User 1 to Merchant 1...');
    await processPayment(1, 1, 50.00);
    process.exit(0);
}

run();
