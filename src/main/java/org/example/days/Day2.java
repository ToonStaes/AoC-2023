package org.example.days;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Day2 {
    private static File getFile(String filename) throws URISyntaxException {
        URL resource = Day2.class.getClassLoader().getResource(filename);
        File file = null;
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {
            file = new File(resource.toURI());
        }
        return file;
    }

    public static void day2_part1() throws URISyntaxException {
        var file = getFile("input_day2.txt");
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            var sum = fileReader.lines()
                    .filter(Day2::isPossible)
                    .map(Day2::getId)
                    .mapToInt(i -> i)
                    .sum();


            System.out.println("day 2 PART 1: " + sum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void day2_part2() throws URISyntaxException {
        var file = getFile("input_day2.txt");
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            var product = fileReader.lines()
                    .map(Day2::getProductOfMinimumCubes)
                    .mapToInt(i -> i)
                    .sum();


            System.out.println("day 2 PART 2: " + product);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static boolean isPossible(String input) {
        int allowedRed = 12;
        int allowedGreen = 13;
        int allowedBlue = 14;
        int maxRed = 0;
        int maxBlue = 0;
        int maxGreen = 0;
        int game = getId(input);
        String allRuns = input.split(":")[1].trim();
        var runList = allRuns.split(";");

        for (String run : runList) {
            var colours = run.split(",");
            for (String colour : colours) {
                var amountPerColour = colour.trim().split(" ");
                try {
                    int currentAmount = Integer.parseInt(amountPerColour[0]);
                    String currentColor = amountPerColour[1];

                    if ("red".equals(currentColor) && currentAmount > maxRed) {
                        maxRed = currentAmount;
                    } else if ("blue".equals(currentColor) && currentAmount > maxBlue) {
                        maxBlue = currentAmount;
                    } else if ("green".equals(currentColor) && currentAmount > maxGreen) {
                        maxGreen = currentAmount;
                    }
                } catch (NumberFormatException ignored) {
                    // Ignore if parsing as integer fails
                }
            }
        }

        if (maxRed > allowedRed || maxBlue > allowedBlue || maxGreen > allowedGreen) {
            return false;
        } else {
            return true;
        }
    }

    private static int getId(String input) {
        var gameId = input.split(":")[0].split(" ")[1].trim();
        return Integer.parseInt(gameId);
    }

    private static int getProductOfMinimumCubes(String input) {
        int game = getId(input);
        String allRuns = input.split(":")[1].trim();
        var runList = allRuns.split(";");

        int maxRed = 0;
        int maxBlue = 0;
        int maxGreen = 0;

        for (String run : runList) {
            var colours = run.split(",");
            for (String colour : colours) {
                var amountPerColour = colour.trim().split(" ");
                try {
                    int currentAmount = Integer.parseInt(amountPerColour[0]);
                    String currentColor = amountPerColour[1];

                    if ("red".equals(currentColor) && currentAmount > maxRed) {
                        maxRed = currentAmount;
                    } else if ("blue".equals(currentColor) && currentAmount > maxBlue) {
                        maxBlue = currentAmount;
                    } else if ("green".equals(currentColor) && currentAmount > maxGreen) {
                        maxGreen = currentAmount;
                    }
                } catch (NumberFormatException ignored) {
                    // Ignore if parsing as integer fails
                }
            }
        }

        return maxRed * maxBlue * maxGreen;
    }


}
