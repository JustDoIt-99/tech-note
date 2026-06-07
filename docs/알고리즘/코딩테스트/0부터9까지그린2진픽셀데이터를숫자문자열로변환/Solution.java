import java.util.*;

class Solution {

    private HashMap<String, Integer> map = new HashMap<>();

    public String solution(String[] pixels) {
        StringBuilder answer = new StringBuilder();

        map.put("111101101101111", 0);
        map.put("110010010010111", 1);
        map.put("111001111100111",2);
        map.put("111001111001111",3);
        map.put("101101111001001",4);
        map.put("111100111001111",5);
        map.put("111100111101111",6);
        map.put("111101001001001",7);
        map.put("111101111101111",8);
        map.put("111101111001111",9);

        int turn = pixels[0].length() / 3;
        StringBuilder sb = new StringBuilder();

        int s = 0;
        while(s < turn) {
            for(int i = 0; i < 5; i++) {
                for(int j = 3 * s; j < 3 * s + 3; j++) {
                    sb.append(pixels[i].charAt(j));
                }
            }

            answer.append(map.get(sb.toString()));
            sb = new StringBuilder();
            s++;
        }

        return answer.toString();
    }
}