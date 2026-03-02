const express = require('express');
const db = require('./db_config');
const cors = require('cors');

const app = express();
const PORT = 3003;

app.use(cors());
app.use(express.static(__dirname));

// 1. Get Order History using JOIN
app.get('/api/orders', async (req, res) => {
    try {
        const query = `
            SELECT 
                o.id AS order_id,
                c.name AS customer_name,
                p.name AS product_name,
                o.amount,
                o.order_date
            FROM orders o
            JOIN customers c ON o.customer_id = c.id
            JOIN products p ON o.product_id = p.id
            ORDER BY o.order_date DESC
        `;
        const [rows] = await db.query(query);
        res.json(rows);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Failed to fetch order history' });
    }
});

// 2. Get Advanced Reports using Subqueries
app.get('/api/reports', async (req, res) => {
    try {
        // Subquery for Highest Value Order
        const highestOrderQuery = `
            SELECT * FROM orders 
            WHERE amount = (SELECT MAX(amount) FROM orders)
            LIMIT 1
        `;

        // Subquery for Most Active Customer (count of orders)
        const activeCustomerQuery = `
            SELECT c.name, COUNT(o.id) as order_count 
            FROM customers c
            JOIN orders o ON c.id = o.customer_id
            GROUP BY c.id
            HAVING order_count = (
                SELECT MAX(cnt) FROM (
                    SELECT COUNT(*) as cnt FROM orders GROUP BY customer_id
                ) as counts
            )
            LIMIT 1
        `;

        const [highestOrder] = await db.query(highestOrderQuery);
        const [activeCustomer] = await db.query(activeCustomerQuery);

        res.json({
            highestOrder: highestOrder[0],
            activeCustomer: activeCustomer[0]
        });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Failed to fetch reports' });
    }
});

app.listen(PORT, () => {
    console.log(`Task 4 Server running at http://localhost:${PORT}`);
});
