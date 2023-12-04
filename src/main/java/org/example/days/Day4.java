package main.java.org.example.days;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Day4 {
    public static void day4_part1() throws IOException {
        File input = Paths.get("src\\main\\resources\\input_day4.txt").toFile();
        try(BufferedReader fileReader = new BufferedReader(new FileReader(input))) {
            Integer sum = fileReader.lines()
                    .map(Day4::getPointsPart1)
                    .mapToInt(Integer::valueOf)
                    .sum();

            System.out.println(sum);
        }
    }

    public static void day4_part2() throws IOException {
        List<String> originalLines = Files.readAllLines(Paths.get("src\\main\\resources\\input_day4.txt"));
        List<String> extraLines = Files.readAllLines(Paths.get("src\\main\\resources\\input_day4.txt"));

        for (int lineIndex = 0; lineIndex < extraLines.size(); lineIndex++) {
            String currentLine = extraLines.get(lineIndex);
            String fullCurrentGameId = currentLine.split(": ")[0].trim();
            String currentGameIdString = fullCurrentGameId.substring(5, 8);
            int linesToAdd = getMatchingNumberCount(currentLine);
            for (int index = 0; index < linesToAdd; index++) {
                int currentGameId = Integer.parseInt(currentGameIdString.trim());
                var indexToAdd = currentGameId + index;
                if (indexToAdd < originalLines.size()){
                    extraLines.add(originalLines.get(indexToAdd));
                }
            }
        }
        System.out.println(extraLines.size());
    }

    public static int getPointsPart1(String line) {
        var counter = 0;
        var points = 0;
        var winningNumbers = Arrays.stream(line.split(": ")[1].trim().split("\\|")[0].trim().split(" ")).filter(number -> !number.equals("")).toList();
        var yourNumbers = Arrays.stream(line.split(": ")[1].trim().split("\\|")[1].trim().split(" ")).filter(number -> !number.equals("")).toList();
        for (String number : yourNumbers){
            if (winningNumbers.contains(number)){
                if (counter == 0){
                    points += 1;
                } else {
                    points *= 2;
                }
                counter++;
            }
        }

        return points;
    }

    public static int getMatchingNumberCount(String line){
        var counter = 0;
        var winningNumbers = Arrays.stream(line.split(": ")[1].trim().split("\\|")[0].trim().split(" ")).filter(number -> !number.equals("")).toList();
        var yourNumbers = Arrays.stream(line.split(": ")[1].trim().split("\\|")[1].trim().split(" ")).filter(number -> !number.equals("")).toList();
        for (String number : yourNumbers){
            if (winningNumbers.contains(number)){
                counter++;
            }
        }
        return counter;
    }
}
