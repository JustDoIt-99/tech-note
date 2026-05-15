const arr = [1,20,10,3];

console.log("sort 기본 정렬", [...arr].sort());
console.log("숫자 기준 정렬", [...arr].sort((a,b) => a - b));