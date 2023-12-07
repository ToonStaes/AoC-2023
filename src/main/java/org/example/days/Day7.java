package main.java.org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

        var rankings = List.of("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J");

        List<Hand> hands = new ArrayList<>();

        for (String line : lines) {
            var handStr = line.split(" ");
            Hand hand = new Hand();
            hand.cards = handStr[0];
            hand.bid = Integer.parseInt(handStr[1]);
            hands.add(hand);
        }

        List<Hand> fiveOfKind = new ArrayList<>();
        List<Hand> fourOfKind  = new ArrayList<>();
        List<Hand> fullHouse  = new ArrayList<>();
        List<Hand> threeOfKind  = new ArrayList<>();
        List<Hand> twoPair  = new ArrayList<>();
        List<Hand> onePair  = new ArrayList<>();
        List<Hand> highCard  = new ArrayList<>();

        for (Hand hand : hands) {
            Map<Character, Integer> cardFrequencies = new HashMap<>();
            List<String> repeats = new ArrayList<>();

            // Remove 'J' from the string
            String noJString = hand.cards.replaceAll("J", "");
            int addAmount = 5 - noJString.length();

            // Count frequencies of each character
            for (int x = 0; x < hand.cards.length(); x++) {
                char character = hand.cards.charAt(x);
                cardFrequencies.put(character, cardFrequencies.getOrDefault(character, 0) + 1);
            }

            // Identify repeated characters
            for (var entry : cardFrequencies.entrySet()) {
                char key = entry.getKey();
                int value = entry.getValue();

                if (value > 1) {
                    repeats.add(new String(new char[value]).replace('\0', key));
                }
            }


            repeats.sort(Comparator.comparingInt(String::length).reversed());

            var character = "";
            repeats = repeats.stream().filter(repeat -> !repeat.contains("J")).collect(Collectors.toList());

            if (repeats.size() > 0 ) {
                character = String.valueOf(repeats.get(0).charAt(0));
                repeats.set(0, repeats.get(0) + character.repeat(addAmount));
            }
            else if (addAmount > 0) {
                if (addAmount == 5){
                    repeats.add("xxxxx");
                } else {
                    repeats.add(String.join("", Collections.nCopies(addAmount + 1, "x")));
                }
            }


            if (repeats.size() == 0) {
                highCard.add(hand);
            }
            else if (repeats.get(0).length() == 5) {
                fiveOfKind.add(hand);
            } else if (repeats.get(0).length() == 4) {
                fourOfKind.add(hand);
            } else if (repeats.get(0).length() == 3) {
                if (repeats.size() > 1) {
                    fullHouse.add(hand);
                } else {
                    threeOfKind.add(hand);
                }
            } else if (repeats.get(0).length() == 2) {
                if (repeats.size() > 1) {
                    twoPair.add(hand);
                } else {
                    onePair.add(hand);
                }
            }
        }

        mapOrder(fiveOfKind, rankings);
        mapOrder(fourOfKind, rankings);
        mapOrder(fullHouse, rankings);
        mapOrder(threeOfKind, rankings);
        mapOrder(twoPair, rankings);
        mapOrder(onePair, rankings);
        mapOrder(highCard, rankings);

        List<Hand> orderedHands = new ArrayList<>();
        orderedHands.addAll(highCard);
        orderedHands.addAll(onePair);
        orderedHands.addAll(twoPair);
        orderedHands.addAll(threeOfKind);
        orderedHands.addAll(fullHouse);
        orderedHands.addAll(fourOfKind);
        orderedHands.addAll(fiveOfKind);

        var winnings = 0;

        for (Hand hand : orderedHands) {
            var index = orderedHands.indexOf(hand);
            winnings += hand.bid * (index + 1);
        }

        System.out.println(winnings);

    }

    public static void mapOrder(List<Hand> array, List<String> order) {
        array.sort((a, b) -> {
            String A = a.cards;
            String B = b.cards;

            for (int index = 0; index < 5; index++) {
                if (order.indexOf(String.valueOf(A.charAt(index))) < order.indexOf(String.valueOf(B.charAt(index)))) {
                    return 1;
                }
                if (order.indexOf(String.valueOf(A.charAt(index))) > order.indexOf(String.valueOf(B.charAt(index)))) {
                    return -1;
                }
            }
            return 0;
        });
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

class Hand {
    String cards;
    int bid;
}
