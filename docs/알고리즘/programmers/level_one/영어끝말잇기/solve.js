function solution(n, words) {
    let wordSet = new Set();

    wordSet.add(words[0]);
    let order = 0;

    for(let i = 1; i < words.length; i++) { // O(n)

        let bf = words[i - 1].charAt(words[i - 1].length - 1);
        let af = words[i].charAt(0);

        if(wordSet.has(words[i]) || bf != af || words[i].length == 1) {
            order = i + 1;
            return order != 0 ? [order % n == 0 ? n : order % n, Math.ceil(order / n)] : [0,0];
        }

        wordSet.add(words[i]);
    }

    return [0,0];
}