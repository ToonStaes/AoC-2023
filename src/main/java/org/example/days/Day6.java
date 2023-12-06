package main.java.org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day6 {
    public static void day6_part1() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src\\main\\resources\\input_day6.txt"));
        var timesString = lines.get(0);
        var distanceString = lines.get(1);

        var times = Arrays.stream(timesString.split(":")[1].trim().split(" ")).filter(time -> !time.equals("")).toList();
        var records = Arrays.stream(distanceString.split(":")[1].trim().split(" ")).filter(distance -> !distance.equals("")).toList();

        List<Integer> winList = new ArrayList<>();

        for (String timeStr : times) {
            int wins = 0;
            int index = times.indexOf(timeStr);
            int buttonPressTime = 0;
            int speed = 0;
            int time = Integer.parseInt(timeStr);
            int record = Integer.parseInt(records.get(index));

            while (buttonPressTime <= time) {
                int travelTime = time - buttonPressTime;
                int distanceTraveled = travelTime * speed;
                if (distanceTraveled > record) {
                    wins++;
                }
                buttonPressTime++;
                speed++;
            }

            winList.add(wins);
        }

        var result = winList.stream()
                .mapToInt(i -> i)
                .reduce(1, (a, b) -> a * b);
        System.out.println(result);
    }

    public static void day6_part2() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src\\main\\resources\\input_day6.txt"));
        var timesString = lines.get(0).replaceAll("\\s", "");
        var distanceString = lines.get(1).replaceAll("\\s", "");

        long wins = 0;
        long buttonPressTime = 0;
        long speed = 0;
        long time = Long.parseLong(timesString.split(":")[1]);
        long record = Long.parseLong(distanceString.split(":")[1]);

        while (buttonPressTime <= time) {
            long travelTime = time - buttonPressTime;
            long distanceTraveled = travelTime * speed;
            if (distanceTraveled > record) {
                wins++;
            }
            buttonPressTime++;
            speed++;
        }

        System.out.println(wins);
    }
}
