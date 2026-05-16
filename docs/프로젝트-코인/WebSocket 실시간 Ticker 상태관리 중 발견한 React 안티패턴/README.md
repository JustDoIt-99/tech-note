## 들어가며

![img.png](../이미지/img.png)

Upbit WebSocket API를 활용하여 현재가, 전일 대비, 거래대금 데이터를 실시간으로 업데이트하는 기능을 구현하고 PR을 날린 후gemini-code-assist에게 코드 리뷰로 받은 멘트입니다.  


리뷰를 받은 코드는 아래와 같습니다.  

```javascript
const handleTickerMessage = useCallback((data: any) => {
        setTickerMap((prev) => {
            const prevPrice = prevPriceRef.current[data.code];
            const nextPrice = data.trade_price;

            const next = {
                ...prev,
                [data.code]: {
                    market: data.code,
                    trade_price: nextPrice,
                    signed_change_rate: data.signed_change_rate,
                    acc_trade_price_24h: data.acc_trade_price_24h,
                },
            };

            if (prevPrice !== undefined && prevPrice !== nextPrice) {
                setFlashMap((prevFlash) => ({
                    ...prevFlash,
                    [data.code]: Date.now(),
                }));
            }

            prevPriceRef.current[data.code] = nextPrice;
            return next;
        });
    }, []);
```

## 왜 안티 패턴일까?

React 공식 문서에 작성된 내용은 아래와 같습니다.

![img_1.png](../이미지/img_1.png)

이벤트 핸들러가 모두 실행된 후, React는 다시 렌더링을 수행합니다. 다시 렌더링하는 동안 React는 state 업데이트 queue를 처리합니다. updater function은 렌더링 과정 중 실행되기 때문에 반드시 순수 함수여야 하며 결과만 반환해야 합니다. updater function 내부에서 다른 state를 변경하거나 다른 부수 효과를 실행하려고 하지 마세요.


## 왜 updater function은 순수 함수여야 할까?

예시를 위해 React 실험용 프로젝트에서 아래와 같은 코드를 작성했습니다.

```javascript
function UpdaterFunctionPure() {

    const [count, setCount] = useState(0);
    const [logCount, setLogCount] = useState(0);

    const handleBadClick = () => {
        setCount((prev) => {
           setLogCount((log) => log + 1);
            console.log("setLogCount 호출");
           return prev + 1;
        });
    }

    const handleGoodClick = () => {;
        setCount((prev) => prev + 1);
        setLogCount((log) => log + 1);
    }

    return (
        <div>
            <h1>Updater Function Pure Test</h1>
            <p>count: {count}</p>
            <p>logCount: {logCount}</p>

            <button onClick={handleBadClick}>
                Bad: updater 안에서 setState
            </button>
            <button onClick={handleGoodClick}>
                Good: updater 밖에서 setState
            </button>
        </div>
    );
}
```
handleBadClick은 setCount의 updater function 내부에서 setLogCount를 호출하고 있습니다. 즉, updater function 내부에서 다른 state값을 변경하는 side effect를 포함하고 있습니다.

handleGoodClick은 updater function 내부에서는 count 계산만 수행하고, setLogCount는 updater function 외부에서 호출합니다.

### handleBadClick

https://github.com/user-attachments/assets/87bff9f9-b77f-4865-809e-a34032031638

count는 1씩 증가하는 반면, logCount는 2씩 증가합니다.

### hanldeGoodClick

https://github.com/user-attachments/assets/76874b97-b384-4801-834c-1f4088a97644

count와 logCount 둘다 1씩 증가합니다.

현재 React 개발 환경은 StrictMode가 활성화 되어 있어 updater function이 한번 더 실행이 됩니다. 이는 공식문서에서 개발 모드에서 컴포넌트나 updater function이 순수하게 작성되어 있는지 확인하기 위해 일부 로직을 의도적으로 한번 더 실행합니다. 이때 updater function 내부에 side effect가 존재할 경우 해당 side effect 역시 반복 실행 됩니다.

반면 handleGoodClick은 updater function 내부에서 다른 state를 변경하지 않습니다. setCount의 updater function은 count의 다음 상태만 계산하고, setLogCount는 updater function 외부에서 별도로 호출됩니다. 따라서 logCount는 의도한 대로 1씩 증가합니다.

해당 예시를 통해 updater function은 순수 함수를 유지해야 함을 확인했습니다.


## 동작 원리

updater function의 동작 원리에 대해서 조금 더 살펴보겠습니다.  
React에서 `setState`를 호출하면 state가 즉시 변경되는 것이 아니라, 다음 렌더링을 위해 state update가 queue에 추가됩니다.

예를 들어 아래와 같은 코드가 있다고 가정해보겠습니다.
```javascript
setCount((prev) => prev + 1);
setCount((prev) => prev + 1);
setCount((prev) => prev + 1);
```
React는 이 updater function들을 즉시 각각 렌더링하지 않고 queue에 저장합니다.  
이후 핸들러의 실행이 모두 끝난 뒤 React는 다음 렌더링 과정에서 queue에 쌓인 updater function들을 순서대로 처리합니다.

StrictMode를 비활성화하면 해당 코드는 일반적으로 정상 동작하는 것처럼 보입니다. 실제 production 환경에서도 updater function이 반복 실행되는 상황을 직접 마주하지 않을 수도 있습니다.

하지만 React는 updater function이 반드시 한 번만 실행된다고 보장하지 않습니다. React의 렌더링 최적화나 Concurrent Rendering과 같은 내부 동작 과정에서는 updater function이 다시 실행될 수 있으며, 이때 updater 내부의 side effect 역시 반복 실행될 수 있습니다.

## 개선 코드

기존 코드의 문제는 `setTickerMap`의 updater function 내부에서 `setFlashMap`을 호출하고 있다는 점이었습니다.  
updater function 내부에서는 다음 상태를 계산하는 역할만 수행해야 하기 때문에, 가격 비교 로직과 setFlashMap 호출을 updater function 외부로 분리했습니다.

```javascript
const handleTickerMessage = useCallback((data: any) => {
    const prevPrice = prevPriceRef.current[data.code];
    const nextPrice = data.trade_price;

    setTickerMap((prev) => ({
        ...prev,
        [data.code]: {
            market: data.code,
            trade_price: nextPrice,
            signed_change_rate: data.signed_change_rate,
            acc_trade_price_24h: data.acc_trade_price_24h,
        },        
    }));

    if (prevPrice !== undefined && prevPrice !== nextPrice) {
        setFlashMap((prevFlash) => ({
            ...prevFlash,
            [data.code]: Date.now(),
        }));
    }
    prevPriceRef.current[data.code] = nextPrice;
}, []);
```

## 정리
updater function 내부에서 다른 state를 변경하는 side effect를 실행하면 예기치 못한 동작이 발생할 수 있습니다.  
React 공식 문서에서 권장하는 것처럼 updater function은 이전 상태를 기반으로 다음 상태만 계산하는 순수 함수로 유지하는 것이 중요합니다.
