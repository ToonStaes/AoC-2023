package main.java.org.example.days;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day3 {
    // got lots of inspiration from Arne Hus: https://github.com/ArneHus/advent-of-code-2023/blob/master/src/main/java/be/arnehus/day3/Part1.java
    private static final String symbols_part1 = "*-+/$=@#%&";
    private static final String symbols_part2 = "*";
    private static final String numbers = "01234567889";
    private static final List<Day3_Position> surrounding = List.of(
            new Day3_Position(-1, -1),
            new Day3_Position(-1, 0),
            new Day3_Position(-1, 1),
            new Day3_Position(0, -1),
            new Day3_Position(0, 1),
            new Day3_Position(1, -1),
            new Day3_Position(1, 0),
            new Day3_Position(1, 1)
    );

    public static void day3_part1() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src\\main\\resources\\input_day3.txt"));

        List<Integer> allNumbers = new ArrayList<>();
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {

            String line = lines.get(lineIndex);
            for (int charIndex = 0; charIndex < line.length(); charIndex++) {

                String character = String.valueOf(line.charAt(charIndex));
                if (symbols_part1.contains(character)) {
                    Set<Integer> adjacentNumbers = findAdjacentNumbers(lines, lineIndex, charIndex);
                    allNumbers.addAll(adjacentNumbers);
                }
            }
        }

        System.out.println("day3 part1: " + allNumbers.stream().mapToInt(i -> i).sum());
    }

    public static void day3_part2() throws IOException  {
        List<String> lines = Files.readAllLines(Paths.get("src\\main\\resources\\input_day3.txt"));

        List<Integer> allNumbers = new ArrayList<>();
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {

            String line = lines.get(lineIndex);
            for (int charIndex = 0; charIndex < line.length(); charIndex++) {

                String character = String.valueOf(line.charAt(charIndex));
                if (symbols_part2.contains(character)) {
                    Set<Integer> adjacentNumbers = findAdjacentNumbers(lines, lineIndex, charIndex);
                    if (adjacentNumbers.size() == 2){
                        int ratio = adjacentNumbers.stream().reduce(1, (a, b) -> a * b);
                        allNumbers.add(ratio);
                    }
                }
            }
        }

        System.out.println("day3 part2: " + allNumbers.stream().mapToInt(i -> i).sum());
    }

    private static Set<Integer> findAdjacentNumbers(List<String> lines, int currentLine, int currentCharacter) {
        Set<Integer> adjacentNumbers = new HashSet<>();
        for (Day3_Position position: surrounding) {
            int lineToCheck = currentLine + position.lineIndex();
            int charToCheck = currentCharacter + position.charIndex();

            String character = String.valueOf(lines.get(lineToCheck).charAt(charToCheck));
            if (numbers.contains(character)) {
                int fullNumber = findFullNumber(lines.get(lineToCheck), charToCheck);
                adjacentNumbers.add(fullNumber);
            }
        }
        return adjacentNumbers;
    }

    private static int findFullNumber(String line, int charachterIndex) {
        int initialCharacterIndex = charachterIndex;
        String initialCharacter = String.valueOf(line.charAt(charachterIndex++));
        String character = String.valueOf(line.charAt(charachterIndex));

        StringBuilder charactersAfter = new StringBuilder();
        while(numbers.contains(character)) {
            charactersAfter.append(character);
            if (charachterIndex != line.length() - 1) {
                character = String.valueOf(line.charAt(++charachterIndex));
            } else {
                break;
            }
        }

        charachterIndex = initialCharacterIndex - 1;
        character = String.valueOf(line.charAt(charachterIndex));
        StringBuilder charactersBefore = new StringBuilder();
        while(numbers.contains(character)) {
            charactersBefore.insert(0, character);
            if (charachterIndex != 0) {
                character = String.valueOf(line.charAt(--charachterIndex));
            } else {
                break;
            }
        }

        return Integer.parseInt(charactersBefore + initialCharacter + charactersAfter);
    }
}
