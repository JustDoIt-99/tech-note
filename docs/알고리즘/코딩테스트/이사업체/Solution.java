import java.util.*;

class Solution {

    private final Long INF = Long.MAX_VALUE / 4;

    private HashMap<String, Integer> cityIdx = new HashMap<>();
    private HashMap<String, List<Car>> carMap = new HashMap<>();
    private long[][] dist;

    public static class Car {
        int capacity;
        int rate;

        public Car(int capacity, int rate) {
            this.capacity = capacity;
            this.rate = rate;
        }
    }

    public String[] solution(String[] cities, String[] roads, String[] cars, String[] customers) {

        int n = cities.length;

        dist = new long[n][n];

        for(int i = 0; i < n; i++) {
            cityIdx.put(cities[i], i);
        }

        for(int i = 0; i < n; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;
        }

        for(String road: roads) {
            String[] split = road.split(" ");

            String a = split[0];
            String b = split[1];
            int distance = Integer.parseInt(split[2]);

            int from = cityIdx.get(a);
            int to = cityIdx.get(b);

            dist[from][to] = distance;
            dist[to][from] = distance;
        }

        for(int k = 0; k < n; k++) { // O(n^3) = 2700
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    if(dist[i][k] == INF || dist[k][j] == INF) continue;
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }

        for(String carInfo: cars) {
            String[] split = carInfo.split(" ");

            String city = split[0];
            int capacity = Integer.parseInt(split[1]);
            int rate = Integer.parseInt(split[2]);

            carMap.computeIfAbsent(city, key -> new ArrayList<>()).add(new Car(capacity, rate));
        }

        String[] sortedCities = cities.clone();
        Arrays.sort(sortedCities);

        for(String city: carMap.keySet()) { // 적재 무게 기준 정렬
            carMap.get(city).sort(Comparator.comparingInt(c -> c.capacity));
        }

        String[] answer = new String[customers.length];

        for(int i = 0; i < customers.length; i++) {
            String[] split = customers[i].split(" ");

            String startCity = split[0];
            String endCity = split[1];
            int weight = Integer.parseInt(split[2]);

            int start = cityIdx.get(startCity);
            int end = cityIdx.get(endCity);

            long bestPrice = INF;
            String bestCity = "";

            for(String companyCity: sortedCities) {
                List<Car> carList = carMap.get(companyCity);
                if(carList == null || carList.size() == 0) continue;

                int rate = findRate(carList, weight);

                if(rate == -1) continue;

                int company = cityIdx.get(companyCity);

                if(dist[company][start] == INF || dist[start][end] == INF) continue; // 연결되지 않은 경우

                long distance = dist[company][start] + dist[start][end];
                long price = distance * rate;

                if(price < bestPrice) {
                    bestPrice = price;
                    bestCity = companyCity;
                }
            }

            answer[i] = bestCity;
        }

        return answer;
    }

    private int findRate(List<Car> cars, int weight) {
        int left = 0;
        int right = cars.size() - 1;
        int result = -1;

        while(left <= right) {
            int mid = (left + right) / 2;

            if(cars.get(mid).capacity >= weight) {
                result = cars.get(mid).rate;
                right = mid - 1;
            }else {
                left = mid + 1;
            }
        }

        return result;
    }
}