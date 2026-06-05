import java.util.*;

class Solution {
    public String solution(int n, int t, int m, String[] timetable) {
        Arrays.sort(timetable);

        int busTime = 9 * 60;
        int crewIdx = 0;
        int lastCrewTime = 0;

        for (int i = 0; i < n; i++) {
            int seat = m;

            while (seat > 0 && crewIdx < timetable.length) {
                int crewTime = toMinute(timetable[crewIdx]);

                if (crewTime <= busTime) {
                    lastCrewTime = crewTime;
                    crewIdx++;
                    seat--;
                } else {
                    break;
                }
            }

            if (i == n - 1) {
                if (seat > 0) {
                    return toTimeString(busTime);
                }

                return toTimeString(lastCrewTime - 1);
            }

            busTime += t;
        }

        return "";
    }

    private int toMinute(String time) {
        String[] split = time.split(":");
        int hour = Integer.parseInt(split[0]);
        int min = Integer.parseInt(split[1]);

        return hour * 60 + min;
    }

    private String toTimeString(int time) {
        return String.format("%02d:%02d", time / 60, time % 60);
    }
}