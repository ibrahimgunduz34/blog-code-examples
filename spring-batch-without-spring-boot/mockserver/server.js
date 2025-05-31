const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const FILE_NAME = 'acme-supplier-products.csv';

const server = http.createServer((req, res) => {
    if (req.method === 'GET' && req.url === '/catalogs/download') {
        const filePath = path.join(__dirname, 'samples', FILE_NAME);

        fs.access(filePath, fs.constants.F_OK, (err) => {
            if (err) {
                res.writeHead(404, { 'Content-Type': 'text/plain' });
                res.end('CSV file not found.\n');
                return;
            }

            // Set headers to trigger file download in browser
            res.writeHead(200, {
                'Content-Type': 'text/csv',
                'Content-Disposition': `attachment; filename="${FILE_NAME}"`
            });

            // Create a stream and pipe the file content to the response
            const fileStream = fs.createReadStream(filePath);
            fileStream.pipe(res);
        });
    } else {
        res.writeHead(404, { 'Content-Type': 'text/plain' });
        res.end('Not found.\n');
    }
});

server.listen(PORT, () => {
    console.log(`Server is running at http://localhost:${PORT}`);
});

const shutdown = () => {
    console.log('\nShutting down gracefully...');
    server.close(() => {
        console.log('Server closed.');
        process.exit(0);
    });
};

process.on('SIGTERM', shutdown);
process.on('SIGINT', shutdown);