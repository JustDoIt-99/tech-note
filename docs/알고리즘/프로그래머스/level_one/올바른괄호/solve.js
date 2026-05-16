const LEFT = "(";
const RIGHT = ")";

function solution(s){

    const stack = [];

    for(let i = 0; i < s.length; i++) {
        if(stack.length === 0) {
            if(s[i] === RIGHT) {
                return false;
            }
        }

        if(s[i] === LEFT) {
            stack.push(LEFT);
        }else { // ")"
            if(stack[stack.length - 1] === LEFT) { // "("
                stack.pop();
            }else { // ")"
                return false;
            }
        }
    }

    return stack.length === 0 ? true : false;
}