CREATE TABLE IF NOT EXISTS Configurations (
    key VARCHAR(255) NOT NULL,
    value VARCHAR(4096),
    profile VARCHAR(30) NOT NULL,
    PRIMARY KEY (key, profile)
    );

INSERT INTO Configurations VALUES
('payment.provider.accountId', '1234567', 'dev'),
('payment.provider.merchantId', '098765', 'dev');
