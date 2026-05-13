## UseRef

`useRef` is a React Hook that lets you reference a value that's not needed for rendering.

```javascript
const ref = useRef(initialValue)
```

## Reference

`useRef(initialValue)`  
Call `useRef` at the top level of your component to declare of ref.

### Parameters
    `initValue` : The value you want the ref object's `current` property to be initially. It can be a value of any type. This argument is ignored after the initial render.     


### Returns
`useRef` returns an object with a single property:
- `current`: Initially, it's set to the `initialValue` you have passed. You can later set it to something else. If you pass the ref object to React as a `ref` attribute to a JSX node, React will set its `current` property.
On the next renders, `useRef` will return the same object.

### Caveats
- You  can mutate the `ref.current` property. Unlike state, it is mutable. However, if it holds an object that is used for rendering(for example, a piece of your state), then you shouldn't mutate that object.
- When you change the `ref.current` property, React does not re-render your component. React is not aware of when you change it because a ref is plain JavaScript object.
- Do not write or read `ref.current` during rendering, except for initialization. This makes your component's behavior unpredictable.
- In Strict Mode, React will **call your component function twice** in order to help you find accidental impurities. This is development-only behavior and does not affect production.
- Each ref object will be created twice, but one of the versions will be discarded.
- If your component function is pure(as it should be), this should not affect the behavior.

## Usage
### Referencing a value with a ref

Call `useRef` at the top level of your component to declare one or more refs.  
`useRef` returns a ref object with a single current property initially set to the initial value you provided.  

```javascript
import { useRef } from 'react';

function Stopwatch() {
    const intervalRef = useRef(0);
}
```

On the next renders, `useRef` will return the same object. You can change its `current` property to store information and read it later.  
This might remind you of state, but there is an important difference.  

**Changing a ref does not trigger a re-render.** This means refs are perfect for storing information that doesn't affect the visual output of your component.  
For example, if you need to store an interval ID and retrieve it later, you can put it in a ref.  
To update the value inside the ref, you need to manually change its current property

```javascript
function handleStartClick() {
    
    const intervalId = setInterval(() => {
        // ...
    }, 1000);
    
    intervalRef.current = intervalId;
}
```
Later, you can read that interval ID from the ref so that you can call clear that interval:
```javascript
function handleStopClick() {
    const intervalId = intervalRef.current;
    clearInterval(intervalId);
}
```

By using a ref, you ensure that:
- You can store information between re-renders(unlike regular variables, which reset on every render)
- Changing it does not trigger a re-render(unlike state variables, which trigger a re-render)
- The information is local to each copy of your component(unlike the variables outside, which are shared.)

Changing a ref does not trigger a re-render, so refs are not appropriate for storing information you want to display on the screen.  
Use state for that instead. 

