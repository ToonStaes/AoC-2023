package main.java.org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day7Rudy {
    public static void day7_part2_rudy() throws IOException {
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
}




class Hand {
    String cards;
    int bid;
}
