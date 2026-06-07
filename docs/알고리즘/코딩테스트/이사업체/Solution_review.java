import java.util.*;

public class Solution_review {

    public static void main(String[] args) {
        Solution_review sol = new Solution_review();

        // Test Case 1
        String[] cities1 = {"a", "b", "c"};
        String[] roads1 = {
                "a b 1",
                "a c 1",
                "b c 1"
        };
        String[] cars1 = {
                "a 100 10",
                "b 300 20",
                "c 50 4"
        };
        String[] customers1 = {
                "a b 100",
                "a b 30",
                "c a 250"
        };
        String[] expected1 = {"a", "c", "b"};

        runTest(sol, 1, cities1, roads1, cars1, customers1, expected1);


        // Test Case 2
        String[] cities2 = {"a", "b", "c", "d", "e", "f", "g"};
        String[] roads2 = {
                "a b 1",
                "a c 1",
                "c d 3",
                "b d 5",
                "b e 6",
                "d e 2",
                "f g 8"
        };
        String[] cars2 = {
                "a 100 10",
                "a 200 15",
                "b 100 5",
                "c 20 2",
                "c 300 30",
                "d 200 20",
                "e 500 100",
                "f 500 50",
                "g 100 40"
        };
        String[] customers2 = {
                "g f 200",
                "c e 50",
                "d a 500",
                "a b 50"
        };
        String[] expected2 = {"f", "b", "e", "a"};

        runTest(sol, 2, cities2, roads2, cars2, customers2, expected2);
    }

    private static void runTest(
            Solution_review sol,
            int testNo,
            String[] cities,
            String[] roads,
            String[] cars,
            String[] customers,
            String[] expected
    ) {

        String[] result = sol.solution(cities, roads, cars, customers);

        boolean isCorrect = Arrays.equals(result, expected);

        System.out.println("===== Test Case " + testNo + " =====");
        System.out.println("Result   : " + Arrays.toString(result));
        System.out.println("Expected : " + Arrays.toString(expected));
        System.out.println("Status   : " + (isCorrect ? "✅ PASS" : "❌ FAIL"));
        System.out.println();
    }

    private final long INF = Long.MAX_VALUE / 4;

    public static class Car {
        int capacity;
        int rate;

        public Car(int capacity, int rate) {
            this.capacity = capacity;
            this.rate = rate;
        }
    }

    public String[] solution(String[] cities, String[] roads, String[] cars, String[] customers) {

        HashMap<String, Integer> cityIndex = new HashMap<>();
        HashMap<String, List<Car>> carMap = new HashMap<>();
        long[][] distance;

        int n = cities.length;

        for(int i = 0; i < n; i++) {
            String city = cities[i];
            cityIndex.put(city, i);
        }

        distance = new long[n][n];

        for(int i = 0; i < n; i++) {
            Arrays.fill(distance[i], INF);
            distance[i][i] = 0;
        }

        for(int i = 0; i < roads.length; i++) {
            String[] road = roads[i].split(" ");
            String from = road[0];
            String to = road[1];

            int fromIdx = cityIndex.get(from);
            int toIdx = cityIndex.get(to);

            int dist = Integer.parseInt(road[2]);

            distance[fromIdx][toIdx] = dist;
            distance[toIdx][fromIdx] = dist;
        }

        for(int i = 0; i < cars.length; i++) {
            String[] car = cars[i].split(" ");
            String carCity = car[0];
            int capacity = Integer.parseInt(car[1]);
            int rate = Integer.parseInt(car[2]);

            carMap.computeIfAbsent(carCity, k -> new ArrayList<>()).add(new Car(capacity, rate));
        }

        for(int k = 0; k < n; k++) {
            for(int i = 0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    if(distance[i][k] == INF || distance[k][j] == INF) continue;
                    distance[i][j] = Math.min(distance[i][j], distance[i][k] + distance[k][j]);
                }
            }
        }

        String[] sortedCities = cities.clone();
        Arrays.sort(sortedCities); // 문자열 정렬

        for(List<Car> carList: carMap.values()) {
            carList.sort(Comparator.comparingInt(car -> car.capacity));
        }

        int m = customers.length;

        String[] answer = new String[m];

        for(int i = 0; i < m; i++) {
            String[] customer = customers[i].split(" ");
            String startCity = customer[0];
            String endCity = customer[1];
            int capacity = Integer.parseInt(customer[2]);

            int startCityIdx = cityIndex.get(startCity);
            int endCityIdx = cityIndex.get(endCity);

            String bestCity = "";
            long bestCost = INF;

            for(int j = 0; j < sortedCities.length; j++) {
                String carCity = sortedCities[j];
                List<Car> carList = carMap.get(carCity);

                if(carList == null || carList.size() == 0) continue;

                int carCityIdx = cityIndex.get(carCity);

                int rate = findRate(carList, capacity);
                if(rate == -1) continue; // 용량을 만족하는 차량이 없다.

                if(distance[carCityIdx][startCityIdx] == INF || distance[startCityIdx][endCityIdx] == INF) continue; // 이동이 불가능하다.
                long dist = distance[carCityIdx][startCityIdx] + distance[startCityIdx][endCityIdx];
                long cost = rate * dist;

                if(bestCost > cost) {
                    bestCost = cost;
                    bestCity = carCity;
                }
            }

            answer[i] = bestCity;
        }

        return answer;
    }

    private int findRate(List<Car> carList, int capacity) {
        int left = 0;
        int right = carList.size() - 1;

        int result = -1;

        while(left <= right) {
            int mid = (left + right) / 2;

            if(carList.get(mid).capacity >= capacity) {
                result = carList.get(mid).rate;
                right = mid - 1;
            }else {
                left = mid + 1;
            }
        }

        return result;
    }
}