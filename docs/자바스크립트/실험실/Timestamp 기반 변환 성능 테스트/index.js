const SAMPLE_SIZE = 1000;
const LOOP = 10000;

const candles = Array.from({SAMPLE_SIZE}, (_, i) => ({
    candle_date_time_utc: "2025-07-01T12:00:00",
    timestamp: 1751371200000 + i * 60_000,
}));

function parseWithData() {
    return candles.map((candle) => ({
        time: Math.floor(new Data(`${candle.candle_date_time_utc}Z`).getTime() / 1000),
    }));
}

function parseWithTime() {
    return candles.map((candle) => ({
        time: Math.floor(candle.timestamp / 1000)
    }));
}

function measure(label, fn) {
    const start = performance.now();

    for (let i = 0; i < LOOP; i++) {
        fn();
    }

    const end = performance.now();

    console.log(`${label}: ${(end - start).toFixed(2)}ms`);
}

measure("parseWithNewDate", parseWithData);
measure("parseWithTime", parseWithTime);