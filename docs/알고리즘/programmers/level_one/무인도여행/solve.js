const dy = [-1,1,0,0];
const dx = [0,0,-1,1];
let n;
let m;
let mapData;

function solution(maps) {
    var answer = [];

    mapData = maps;

    n = maps.length;
    m = maps[0].length;

    const visited = Array.from({length: n}, () => Array(m).fill(false));

    for(let i = 0; i < n; i++) {
        for(let j = 0; j < m; j++) {
            let value = 0;
            if(mapData[i][j] !== "X" && !visited[i][j]) {
                visited[i][j] = true;
                value += parseInt(mapData[i][j]) + dfs(i, j, visited);
                answer.push(value);
            }
        }
    }

    return answer.length == 0 ? [-1] : answer.sort((a,b) => a - b);
}

function dfs(y, x, visited) {
    let result = 0;

    for(let i = 0; i < 4; i ++) {
        let ny = y + dy[i];
        let nx = x + dx[i];

        if(ny < 0 || ny >= n || nx < 0 || nx >= m) continue ;
        if(visited[ny][nx] || mapData[ny][nx] === "X") continue;
        visited[ny][nx] = true;
        result += dfs(ny, nx, visited) + parseInt(mapData[ny][nx]);
    }

    return result;
}