package main.java.org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day9 {

    public static void day9_part1() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src\\main\\resources\\input_day9.txt"));
        List<Integer> nextCharacters = new ArrayList<>();

        for (String line : lines) {
            int nextCharacter = getNextCharacterPart1(line);
            nextCharacters.add(nextCharacter);
        }

        var sum = nextCharacters.stream().mapToInt(i -> i).sum();
        System.out.println(sum);
    }

    public static void day9_part2() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src\\main\\resources\\input_day9.txt"));
        List<Integer> nextCharacters = new ArrayList<>();

        for (String line : lines) {
            int nextCharacter = getNextCharacterPart2(line);
            nextCharacters.add(nextCharacter);
        }

        var sum = nextCharacters.stream().mapToInt(i -> i).sum();
        System.out.println(sum);
    }

    private static int getNextCharacterPart1(String line) {
        List<List<Integer>> differenceLists = new ArrayList<>();
        List<Integer> numbers = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
        List<Integer> lineDifferences = new ArrayList<>();

        return getNextValue(differenceLists, numbers, lineDifferences);
    }

    private static int getNextCharacterPart2(String line) {
        List<List<Integer>> differenceLists = new ArrayList<>();
        List<Integer> numbers = Arrays.stream(line.split(" ")).map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> lineDifferences = new ArrayList<>();
        Collections.reverse(numbers);

        return getNextValue(differenceLists, numbers, lineDifferences);
    }

    private static int getNextValue(List<List<Integer>> differenceLists, List<Integer> numbers, List<Integer> lineDifferences) {
        for (int i = 0; i < numbers.size() - 1; i++) {
            int firstNumber = numbers.get(i);
            int secondNumber = numbers.get(i + 1);
            int diff = secondNumber - firstNumber;
            lineDifferences.add(diff);
        }

        List<Integer> uniqueDiffs = getUniqueNumbers(lineDifferences);
        if (uniqueDiffs.size() == 1 && uniqueDiffs.get(0) == 0) {
            return numbers.get(numbers.size() - 1);
        } else {
            differenceLists.add(lineDifferences);
        }
        var counter = 0;

        while (true) {
            List<Integer> differenceList;
            if (counter == 0) {
                differenceList = lineDifferences;
            } else {
                differenceList = differenceLists.get(counter);
            }

            lineDifferences = new ArrayList<>();

            for (int i = 0; i < differenceList.size() - 1; i++) {
                int firstNumber = differenceList.get(i);
                int secondNumber = differenceList.get(i + 1);
                int diff = secondNumber - firstNumber;
                lineDifferences.add(diff);
            }

            uniqueDiffs = getUniqueNumbers(lineDifferences);
            if (uniqueDiffs.size() == 1 && uniqueDiffs.get(0) == 0) {
                var dlLength = differenceLists.size();
                Collections.reverse(differenceLists);
                for (List<Integer> DL : differenceLists) {
                    var lastEntry = differenceLists.get(dlLength - 1);
                    if (DL == lastEntry) {
                        DL.add(DL.get(DL.size() - 1));
                        var diffToAdd = DL.get(DL.size() - 1);
                        return numbers.get(numbers.size() - 1) + diffToAdd;
                    } else {
                        var currentIndex = differenceLists.indexOf(DL);

                        var previousList = differenceLists.get(currentIndex + 1);
                        var diffToAdd = DL.get(DL.size() - 1);
                        var numberToAdd = previousList.get(previousList.size() - 1) + diffToAdd;
                        previousList.add(numberToAdd);
                    }
                }
            } else {
                differenceLists.add(lineDifferences);
            }
            counter++;
        }
    }

    public static List<Integer> getUniqueNumbers(List<Integer> numbers) {
        Set<Integer> uniqueSet = new HashSet<>(numbers);
        return new ArrayList<>(uniqueSet);
    }
}
