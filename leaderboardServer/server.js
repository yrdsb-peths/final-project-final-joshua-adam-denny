// server.js
const express = require('express');
const app = express();
const port = 8080;
const DATA_FILE = path.join(__dirname, 'leaderboard.json');



app.use(express.json());


let storedEntries = [];
try {
  const raw = fs.readFileSync(DATA_FILE, 'utf8');
  storedEntries = JSON.parse(raw);
} catch (e) {
  console.log('No existing leaderboard file, bwomp.');
  storedEntries = [];
}

function saveEntries() {
  fs.writeFileSync(DATA_FILE,
    JSON.stringify(storedEntries, null, 2),
    'utf8'
  );
}

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
    constructor(entries) {
        this.entries = entries; 
    }

    add(id, score, wave) {
        const entry = { id, score, wave };
        let idx = this.entries.findIndex(e => e.score < score);
        if (idx === -1) idx = this.entries.length;
        this.entries.splice(idx, 0, entry);
        saveEntries();
        return idx + 1;
    }

    top(n = 10) {
        return this.entries.slice(0, n);
    }
}

const leaderboard = new Leaderboard(storedEntries);

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
  const topList = leaderboard.top(10);
  const result = [];
  for (let i = 0; i < 10; i++) {
    if (i < topList.length) {
      const e = topList[i];
      result.push({
        userID: e.id,
        rank:   i + 1,
        score:  e.score,
        wave:   e.wave
      });
    } else {
      result.push({
        userID: "(empty)",
        rank:    0,
        score:  6969,
        wave:     0
      });
    }
  }
  res.json(result);
});

app.get('/connect', (req, res) => {
  res.json({ success: true });
});



app.listen(port, () => {
    console.log(`Leaderboard server listening on port ${port}`);
});
