## 문제 상황
실시간 분봉 차트를 구현하는 과정에서 candles 데이터가 refetch될 때마다 차트 viewport가 의도치 않게 변경되는 문제가 발생했습니다.
```javascript
chartRef.current.timeScale().fitContent();
```

하지만 fitContent()는 전체 데이터를 기준으로 visible range를 다시 계산하는 메서드이기 때문에, 사용자가 과거 데이터를 탐색 중이거나 특정 구간을 확대한 상태에도 차트 viewport가 강제로 초기화될 수 있었습니다.  

또한 과거 데이터를 조회할 때 setData()를 사용하여 기존 데이터 앞에 prepend하는 방식으로 구현했는데, 이 과정에서도 viewport 위치가 의도치 않게 이동하는 현상이 발생했습니다.

## 원인 분석
### 1. fitContent()의 동작 방식
fitContent()는 현재 사용자의 viewport 상태를 유지하는 메서드가 아니라, 전체 데이터가 화면에 모두 보이도록 visible range를 재계산하는 메서드입니다.  
따라서 refetch시 반복 호출될 경우 사용자가 보고 있던 위치가 초기화될 수 있습니다.  

### 2. setData() 호출 시 logical index 변경
과거 데이터를 조회할 때는 기존 데이터 앞에 새로운 데이터를 추가하는 구조였습니다.
```javascript
candleDataRef.current = [
  ...olderChartData,
  ...candleDataRef.current,
];
```
이후 setData()를 호출하여 차트 데이터를 갱신했습니다.
하지만 맨앞에 추가하는 방식으로 데이터가 추가되면 기존 캔들의 logical index가 뒤로 밀리게 됩니다.  

이 상태에서 기존 visible range를 그대로 유지하면 사용자가 보던 위치가 달라지는 문제가 발생했습니다.  

## 개선 내용
### 1. 최초 로딩 시에만 fitContent() 호출
useRef를 사용하여 차트 최초 로딩 여부를 관리했습니다.
```typescript
const isFirstLoadingChartRef = useRef(true);
```

최초 로딩 시에만 fitContent()를 실행하고, 이후에는 호출하지 않도록 수정했습니다.  
```typescript
if (isFirstLoadingChartRef.current) {
  chartRef.current.timeScale().fitContent();
  isFirstLoadingChartRef.current = false;
}
```
이를 통해 사용자가 탐색 중인 viewport 상태가 refetch로 인해 초기화되지 않도록 개선했습니다.

### 2. setData() 호출 전후 visible range 유지
과거 데이터 추가시에는 현재 visible range를 저장한 뒤, setData() 호출 이후 다시 복원하도록 수정했습니다.
```typescript
const visibleRange = chart.timeScale().getVisibleLogicalRange();

candleSeries.setData(candleDataRef.current);

chart.timeScale().setVisibleLogicalRange({
    from: visibleRange.from + olderChartData.length,
    to: visibleRange.to + olderChartData.length,
});
```
오래된 데이터의 앞부분 추가로 인해 logical index가 뒤로 밀리므로, 추가된 데이터 개수만큼 visible range를 보정하도록 처리했습니다.  
이 방법을 통해 과거 데이터를 불러오더라도 사용자가 보고 있던 위치가 자연스럽게 유지되도록 개선했습니다.  

## 결과

