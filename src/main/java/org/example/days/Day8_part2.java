package main.java.org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8_part2 {
    public static void day8_part2() throws IOException {
        List<String> input = Files.readAllLines( Path.of( "src\\main\\resources\\input_day8.txt" ) );
        List<Integer> stepsUntilEndNode = getLocations(getNodeMap(input), getInstructions(input)).stream()
                .mapToInt( Location::getResult ).boxed().toList();
        long total = stepsUntilEndNode.get(0);
        for (int i = 1; i < stepsUntilEndNode.size(); i++) {
            total = kleinstGemeenschappelijkVeelvoud(total, stepsUntilEndNode.get(i));
        }
        System.out.println(total);
    }

    private static List<Integer> getInstructions( final List<String> input ) {
        List<Integer> instructions = new ArrayList<>();
        String instructionLine = input.get(0);
        for (char c : instructionLine.toCharArray()) {
            instructions.add(c == 'L' ? 0 : 1); // 0 om later eerste entry uit nodeMap te nemen en 1 om de 2e entry te nemen
        }
        return instructions;
    }

    private static Map<String, String[]> getNodeMap( final List<String> input ) {
        Map<String, String[]> nodeMap = new TreeMap<>();
        for (int i = 2; i < input.size(); i++) {
            String line = input.get(i);
            Matcher matcher = Pattern.compile("([A-Z]{3})\\s=\\s\\(([A-Z]{3}),\\s([A-Z]{3})\\)").matcher(line);
            matcher.find();
            String[] nextElements = { matcher.group(2), matcher.group(3) };
            nodeMap.put(matcher.group(1), nextElements);
        }
        return nodeMap;
    }

    private static List<Location> getLocations( Map<String, String[]> nodeMap, List<Integer> instructions ) {
        List<String> startingNodes = nodeMap.keySet().stream().filter(k -> k.endsWith("A")).toList();
        return startingNodes.stream().map(s -> new Location(s, instructions, nodeMap)).toList();
    }

    private static long kleinstGemeenschappelijkVeelvoud( long x, long y ) {
        long max = Math.max(x, y);
        long min = Math.min(x, y);
        long kgv = max;
        while (kgv % min != 0) {
            kgv += max;
        }
        return kgv;
    }
}

class Location {
    private String currentNode;
    private List<Integer> instructions;
    private Map<String, String[]> nodeMap;
    private int index = 0;

    public Location(String currentNode, List<Integer> instructions, Map<String, String[]> nodeMap) {
        this.currentNode = currentNode;
        this.instructions = instructions;
        this.nodeMap = nodeMap;
    }

    private void getNextNode() {
        int instruction = instructions.get(index);
        currentNode = nodeMap.get(currentNode)[instruction];
        index = (index + 1) % instructions.size();
    }

    public int getResult() {
        int counter = 0;
        while (!reachedEndNode()) {
            getNextNode();
            counter++;
        }
        return counter;
    }

    private boolean reachedEndNode() {
        return currentNode.endsWith("Z");
    }
}
