-- Employees table
CREATE TABLE IF NOT EXISTS employees_audit_test (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    salary DECIMAL(10,2) NOT NULL
);

-- Audit logs table
CREATE TABLE IF NOT EXISTS audit_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    table_name VARCHAR(50) NOT NULL,
    action VARCHAR(20) NOT NULL,
    record_id INT NOT NULL,
    old_data TEXT,
    new_data TEXT,
    action_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Trigger for INSERT
DELIMITER //
CREATE TRIGGER trg_employee_insert
AFTER INSERT ON employees_audit_test
FOR EACH ROW
BEGIN
    INSERT INTO audit_logs (table_name, action, record_id, new_data)
    VALUES ('employees_audit_test', 'INSERT', NEW.id, CONCAT('name: ', NEW.name, ', salary: ', NEW.salary));
END;
//
DELIMITER ;

-- Trigger for UPDATE
DELIMITER //
CREATE TRIGGER trg_employee_update
AFTER UPDATE ON employees_audit_test
FOR EACH ROW
BEGIN
    INSERT INTO audit_logs (table_name, action, record_id, old_data, new_data)
    VALUES ('employees_audit_test', 'UPDATE', OLD.id, CONCAT('name: ', OLD.name, ', salary: ', OLD.salary), CONCAT('name: ', NEW.name, ', salary: ', NEW.salary));
END;
//
DELIMITER ;

-- View for daily activity report
CREATE OR REPLACE VIEW daily_activity_report AS
SELECT 
    DATE(action_timestamp) as activity_date,
    action,
    COUNT(*) as total_actions
FROM audit_logs
GROUP BY DATE(action_timestamp), action;
