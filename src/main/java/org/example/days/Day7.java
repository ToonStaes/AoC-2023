package main.java.org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day7 {
    private static final Map<String, String> letterToNumberMapPart1 = new HashMap<>();

    static {
        letterToNumberMapPart1.put("A", "14");
        letterToNumberMapPart1.put("K", "13");
        letterToNumberMapPart1.put("Q", "12");
        letterToNumberMapPart1.put("J", "11");
        letterToNumberMapPart1.put("T", "10");
        for (int i = 2; i <= 9; i++) {
            letterToNumberMapPart1.put(String.valueOf(i), "0" + i);
        }
    }

    private static final Map<String, String> letterToNumberMapPart2 = new HashMap<>();

    static {
        letterToNumberMapPart2.put("A", "13");
        letterToNumberMapPart2.put("K", "12");
        letterToNumberMapPart2.put("Q", "11");
        letterToNumberMapPart2.put("J", "01");
        letterToNumberMapPart2.put("T", "10");
        for (int i = 2; i <= 9; i++) {
            letterToNumberMapPart2.put(String.valueOf(i), "0" + i);
        }
    }

    public static void day7_part1() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src\\main\\resources\\input_day7.txt"));
        List<String> rankLines = new ArrayList<>();

        List<String> cardSets = new ArrayList<>();
        Map<String, Long> cardSetPointsUnordered = new HashMap<>();

        for (String line : lines) {
            String cards = line.split(" ")[0];
            cardSets.add(cards);
        }

        System.out.println(cardSets);

        // Get Score for card
        for (String cardSet : cardSets) {

            StringBuilder scoreBuilder = new StringBuilder();
            String uniqueCharacters = getUniqueCharactersPart1(cardSet);
            getScoreForCardSetPart1(cardSet, uniqueCharacters, scoreBuilder);

            var modifiedString = replaceOriginalValuesPart1(cardSet);
            scoreBuilder.append(modifiedString);
            var score = scoreBuilder.toString();
            cardSetPointsUnordered.put(cardSet, Long.parseLong(score));
        }

        Map<String, Long> cardSetPointsOrdered = sortByValues(cardSetPointsUnordered);

        setRank(lines, rankLines, cardSetPointsOrdered);
    }

    public static void day7_part2() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src\\main\\resources\\input_day7.txt"));
        List<String> rankLines = new ArrayList<>();

        List<String> cardSets = new ArrayList<>();
        Map<String, Long> cardSetPointsUnordered = new HashMap<>();

        for (String line : lines) {
            String cards = line.split(" ")[0];
            cardSets.add(cards);
        }

        System.out.println(cardSets);

        // Get Score for card
        for (String cardSet : cardSets) {

            StringBuilder scoreBuilder = new StringBuilder();
            String uniqueCharacters = getUniqueCharactersPart2(cardSet);
            boolean containsJ = uniqueCharacters.contains("J");
            getScoreForCardSetPart2(cardSet, uniqueCharacters, containsJ, scoreBuilder);

            var modifiedString = replaceOriginalValuesPart2(cardSet);
            scoreBuilder.append(modifiedString);
            var score = scoreBuilder.toString();
            if (cardSetPointsUnordered.containsValue(Long.parseLong(score))){
                System.out.println("DUPLICATE ENTRY");
            }
            cardSetPointsUnordered.put(cardSet, Long.parseLong(score));
        }

        Map<String, Long> cardSetPointsOrdered = sortByValues(cardSetPointsUnordered);

        setRank(lines, rankLines, cardSetPointsOrdered);

    }

    private static void setRank(List<String> lines, List<String> rankLines, Map<String, Long> cardSetPointsOrdered) {
        Map<String, Integer> cardSetRank = new HashMap<>();
        var rankCounter = 1;
        for (String cardSet : cardSetPointsOrdered.keySet()) {
            cardSetRank.put(cardSet, rankCounter);
            rankCounter++;
        }

        for (String line : lines) {
            var cardSet = line.split(" ")[0];
            var rankLine = line.replace(cardSet, cardSetRank.get(cardSet).toString());
            rankLines.add(rankLine);
        }

        int result = 0;

        for (String rankLine : rankLines) {
            int first = Integer.parseInt(rankLine.split(" ")[0]);
            int second = Integer.parseInt(rankLine.split(" ")[1]);
            int product = first * second;
            result += product;
        }

        System.out.println(result);
    }

    private static String getUniqueCharactersPart1(String input) {
        Set<Character> uniqueCharsSet = new HashSet<>();
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (uniqueCharsSet.add(c)) {
                result.append(c);
            }
        }

        return result.toString();
    }

    private static String getUniqueCharactersPart2(String input) {
        Set<Character> uniqueCharsSet = new HashSet<>();
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (uniqueCharsSet.add(c)) {
                result.append(c);
            }
        }

        return result.toString();
    }

    private static String replaceOriginalValuesPart1(String originalString) {
        StringBuilder modifiedString = new StringBuilder();

        for (char c : originalString.toCharArray()) {
            String replacement = letterToNumberMapPart1.getOrDefault(String.valueOf(c), String.valueOf(c));
            modifiedString.append(replacement);
        }

        return modifiedString.toString();
    }

    private static String replaceOriginalValuesPart2(String originalString) {
        StringBuilder modifiedString = new StringBuilder();

        for (char c : originalString.toCharArray()) {
            String replacement = letterToNumberMapPart2.getOrDefault(String.valueOf(c), String.valueOf(c));
            modifiedString.append(replacement);
        }

        return modifiedString.toString();
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValues(Map<K, V> map) {
        List<Map.Entry<K, V>> entryList = new LinkedList<>(map.entrySet());

        // Sort the list based on values using a custom comparator
        entryList.sort(Map.Entry.comparingByValue());

        // Create a LinkedHashMap to preserve the order of elements
        Map<K, V> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static void getScoreForCardSetPart2(String cardSet, String uniqueCharacters, boolean containsJ, StringBuilder scoreBuilder) {
        if (uniqueCharacters.length() == 1) {
            // Five of a kind
            scoreBuilder.append("7");
        } else if (uniqueCharacters.length() == 2) {
            if (containsJ) {
                // Five of a kind
                scoreBuilder.append("7");
            } else {
                // Four of a Kind
                scoreBuilder.append("6");
            }
        } else if (uniqueCharacters.length() == 3) {
            if (containsJ) {
                var jCount = cardSet.chars().filter(character -> character == 'J').count();
                if (jCount == 3 || jCount == 2) {
                    // Four of a kind
                    scoreBuilder.append("6");
                } else {
                    // Four of a kind or full house
                    uniqueCharacters = uniqueCharacters.replace("J", "");
                    String finalUniqueCharacters = uniqueCharacters;
                    var char1Count = cardSet.chars().filter(character -> character == finalUniqueCharacters.charAt(0)).count();
                    var char2Count = cardSet.chars().filter(character -> character == finalUniqueCharacters.charAt(1)).count();
                    if (char1Count == 3 || char2Count == 3) {
                        // Four of a kind
                        scoreBuilder.append("6");
                    } else {
                        // Full house
                        scoreBuilder.append("5");
                    }
                }
            } else {
                uniqueCharacters = uniqueCharacters.replace("J", "");
                String finalUniqueCharacters = uniqueCharacters;
                var char1Count = cardSet.chars().filter(character -> character == finalUniqueCharacters.charAt(0)).count();
                var char2Count = cardSet.chars().filter(character -> character == finalUniqueCharacters.charAt(1)).count();
                var char3Count = cardSet.chars().filter(character -> character == finalUniqueCharacters.charAt(2)).count();

                if (char1Count == 3 || char2Count == 3 || char3Count == 3) {
                    // Three of a kind
                    scoreBuilder.append("4");
                } else {
                    // Two pair
                    scoreBuilder.append("3");
                }
            }
        } else if (uniqueCharacters.length() == 4) {
            // Logic for length 4
            if (containsJ) {
                // Three of a kind
                scoreBuilder.append("4");
            } else {
                // One pair
                scoreBuilder.append("2");
            }
        } else {
            if (containsJ) {
                // One pair
                scoreBuilder.append("2");
            } else {
                // High card
                scoreBuilder.append("1");
            }
        }
    }

    public static void getScoreForCardSetPart1(String cardSet, String uniqueCharacters, StringBuilder scoreBuilder) {
        if (uniqueCharacters.length() == 1) {
            // Five of a kind
            scoreBuilder.append("7");
        } else if (uniqueCharacters.length() == 2) {
            // Four of a Kind or Full house
            var char1Count = cardSet.chars().filter(character -> character == uniqueCharacters.charAt(0)).count();
            var char2Count = cardSet.chars().filter(character -> character == uniqueCharacters.charAt(1)).count();
            if (char1Count == 4 || char2Count == 4) {
                // Four of a Kind
                scoreBuilder.append("6");
            } else {
                // Full house
                scoreBuilder.append("5");
            }
        } else if (uniqueCharacters.length() == 3) {
            // Three of a kind or Two pair
            var char1Count = cardSet.chars().filter(character -> character == uniqueCharacters.charAt(0)).count();
            var char2Count = cardSet.chars().filter(character -> character == uniqueCharacters.charAt(1)).count();
            var char3Count = cardSet.chars().filter(character -> character == uniqueCharacters.charAt(2)).count();
            if (char1Count == 3 || char2Count == 3 || char3Count == 3) {
                // Three of a Kind
                scoreBuilder.append("4");
            } else {
                // Two pair
                scoreBuilder.append("3");
            }
        } else if (uniqueCharacters.length() == 4) {
            scoreBuilder.append("2");
        } else {
            scoreBuilder.append("1");
        }
    }
}
