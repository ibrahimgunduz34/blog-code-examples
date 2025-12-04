CREATE TABLE IF NOT EXISTS Configurations (
    key VARCHAR(255) NOT NULL,
    value VARCHAR(4096),
    profile VARCHAR(30) NOT NULL,
    PRIMARY KEY (key, profile)
    );

INSERT INTO Configurations VALUES
('notification.service.accountId', '1234567', 'default');