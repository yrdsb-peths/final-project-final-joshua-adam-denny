// server.js
const express = require('express');
const app = express();
const port = 8080;

app.use(express.json());

function logMiddleware(req, res, next) {
    let userIP = '';
    if (req.headers['cf-connecting-ip']) {
        userIP = req.headers['cf-connecting-ip'];
    } else if (req.headers['x-forwarded-for']) {
        const ips = req.headers['x-forwarded-for'].split(',');
        userIP = ips[0];
    } else {
        userIP = req.ip;
    }

    console.log(`${userIP} || Method: ${req.method} || URL: ${req.url}`);
    next();
}
app.use(logMiddleware)

class Leaderboard {
    constructor() {
        this.entries = [];
    }
    add(id, score, wave) {
        const entry = { id, score, wave };
        let idx = this.entries.findIndex(e => e.score < score);
        if (idx === -1) {
            idx = this.entries.length;
        }
        this.entries.splice(idx, 0, entry);
        return idx + 1;
    }

    top(n = 10) {
        return this.entries.slice(0, n);
    }
}

const leaderboard = new Leaderboard();

app.post('/sendScore', (req, res) => {
    const { id, score, wave } = req.body;
    if (typeof id !== 'string') {
        return res.status(400).json({ error: 'Invalid id' });
    }

    const numScore = typeof score === 'string' ? Number(score) : score;
    const numWave = typeof wave === 'string' ? Number(wave) : wave;

    if (!Number.isFinite(numScore) || !Number.isInteger(numWave)) {
        return res.status(400).json({ error: 'Score must be a number and wave must be an integer' });
    }

    const place = leaderboard.add(id, numScore, numWave);
    res.json({ success: true, place });
});

app.get('/getLeaderboard', (req, res) => {
  const lines = leaderboard
    .top(10)
    .map(e => `${e.id},${e.score},${e.wave}`);
  res
    .type('text/plain')
    .send(lines.join('\n'));
});

app.get('/connect', (req, res) => {
  res.json({ success: true });
});



app.listen(port, () => {
    console.log(`Leaderboard server listening on port ${port}`);
});
