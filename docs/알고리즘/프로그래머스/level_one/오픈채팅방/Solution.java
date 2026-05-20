import java.util.*;

// 1. 들어오는 경우 "[닉네임]님이 들어왔습니다."
// 2. 나가는 경우 "[닉네임]님이 나갔습니다."
// 3. 닉네임 변경
// 3-1. 채팅방을 나간 후, 새로운 닉네임
// 3-2. 채팅방에서 닉네임 변경
// 기존 채팅방에 출력되어 있던 메시지의 닉네임도 전부 변경
// 중복 닉네임이 허용된다.
// 유저 아이디로 구분 - 식별자
// Enter, Leave, Change

class Solution {

    private final String ENTER_TEXT = "님이 들어왔습니다.";
    private final String OUT_TEXT = "님이 나갔습니다.";
    private final String ENTER = "Enter";
    private final String CHANGE = "Change";
    private final String LEAVE = "Leave";

    private Map<String, String> names = new HashMap<>();

    public String[] solution(String[] record) {

        for(int i = 0; i < record.length; i++) { // O(n)
            String[] value = record[i].split(" ");
            String order = value[0];
            String uid = value[1];
            String name = "";

            if(!order.equals(LEAVE)){
                name = value[2];

                if(order.equals(ENTER)) {
                    names.put(uid, name);
                }else {
                    names.put(uid, name);
                }
            }
        }

        List<String> answer = new ArrayList<>();

        for(int i = 0; i < record.length; i++) { // O(n)
            String[] value = record[i].split(" ");
            String order = value[0];
            String uid = value[1];

            if(order.equals("Enter")) {
                answer.add(names.get(uid) + ENTER_TEXT);
            }else if(order.equals("Leave")){
                answer.add(names.get(uid) + OUT_TEXT);
            }
        }

        return answer.toArray(new String[0]);
    }
}