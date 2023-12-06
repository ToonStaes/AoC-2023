package main.java.org.example.days;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day5 {

    public static void day5_part1() throws IOException {
        File input = Paths.get("src\\main\\resources\\input_day5.txt").toFile();
        List<String> lines = Files.readAllLines(input.toPath());

        List<String> seedsStrings = List.of(lines.get(0).split(": ")[1].trim().split(" "));
        List<Long> seeds = seedsStrings.stream().map(Long::parseLong).toList();

        int seedToSoilLineIndex = lines.stream().filter(line -> line.contains("seed-to-soil")).map(lines::indexOf).toList().get(0);
        Map<Long, Long> seedToSoilMap = createMap(lines, seedToSoilLineIndex, seeds);

        int soilToFertilizerLineIndex = lines.stream().filter(line -> line.contains("soil-to-fertilizer")).map(lines::indexOf).toList().get(0);
        Map<Long, Long> soilToFertilizerMap = createMap(lines, soilToFertilizerLineIndex, seedToSoilMap.values());

        int fertilizerToWaterIndex = lines.stream().filter(line -> line.contains("fertilizer-to-water")).map(lines::indexOf).toList().get(0);
        Map<Long, Long> fertilizerToWaterMap = createMap(lines, fertilizerToWaterIndex, soilToFertilizerMap.values());

        int waterToLightLineIndex = lines.stream().filter(line -> line.contains("water-to-light")).map(lines::indexOf).toList().get(0);
        Map<Long, Long> waterToLightMap = createMap(lines, waterToLightLineIndex, fertilizerToWaterMap.values());

        int lightToTemperatureLineIndex = lines.stream().filter(line -> line.contains("light-to-temperature")).map(lines::indexOf).toList().get(0);
        Map<Long, Long> lightToTemperatureMap = createMap(lines, lightToTemperatureLineIndex, waterToLightMap.values());

        int temperatureToHumidityLineIndex = lines.stream().filter(line -> line.contains("temperature-to-humidity")).map(lines::indexOf).toList().get(0);
        Map<Long, Long> temperatureToHumidityMap = createMap(lines, temperatureToHumidityLineIndex, lightToTemperatureMap.values());

        int humidityToLocationLineIndex = lines.stream().filter(line -> line.contains("humidity-to-location")).map(lines::indexOf).toList().get(0);
        Map<Long, Long> humidityToLocationMap = createMap(lines, humidityToLocationLineIndex, temperatureToHumidityMap.values());

        List<Long> locations = new ArrayList<>();
        for (Long seed : seeds) {
            locations.add(getLocationForSeed(seed, seedToSoilMap, soilToFertilizerMap, fertilizerToWaterMap, waterToLightMap, lightToTemperatureMap, temperatureToHumidityMap, humidityToLocationMap));
        }

        var result = Collections.min(locations);
        System.out.println(result);
    }

    public static void day5_part2() throws IOException {
        System.out.println(new Date() + " start");
        String text = readFileAsString("src\\main\\resources\\input_day5.txt");
        List<String> lines = new ArrayList<>(Arrays.asList(text.split("\\n\\s*\\n")));

        var seedsText = lines.get(0);
        seedsText = seedsText.substring(0, seedsText.length() -3);


        List<Mapping> mappings = new ArrayList<>();
        lines.remove(0);

        for (String line : lines){
            var splitLines = line.split("\\r|\\n");

            List<String> maps = Arrays.stream(splitLines)
                    .filter(l -> !l.isEmpty())
                    .collect(Collectors.toList());
            Mapping mapping = new Mapping();

            for (int index = 0; index < maps.size(); index++) {
                String map = maps.get(index);

                if (index == 0) {
                    mapping.name = map;
                } else {
                    String[] sdis = map.split(" ");
                    long destination = Long.parseLong(sdis[0]);
                    long source = Long.parseLong(sdis[1]);
                    long increment = Long.parseLong(sdis[2]);

                    mapping.sdis.add(new SDI(destination, source, increment));
                }
            }

            mappings.add(mapping);
        }

        System.out.println(new Date() + " Got mappings");

        var location = Long.MAX_VALUE;

        var seeds = new ArrayList<>(Arrays.asList(seedsText.split(" ")));

        seeds.remove(0);

        List<Long> newSeeds = new ArrayList<>();
        var counter = 0;

        var iterator = seeds.size() / 2;

        for (int index = 0; counter < iterator; index += 2) {
            long startSeed = Long.parseLong(seeds.get(index));
            long range = Long.parseLong(seeds.get(index+1));

            for (long i = 0L; i < range; i++) {
                final long[] value = {startSeed + i};
                mappings.forEach(mapping -> {
                    for (SDI sdi : mapping.sdis) {
                        if (sdi.source <= value[0] && value[0] <= sdi.source + sdi.increment -1){
                            var diff = value[0] - sdi.source;
                            value[0] = sdi.destination + diff;
                            break;
                        }
                    }
                });
                if (value[0] < location) {
                    location = value[0];
                }
            }
            counter++;
            System.out.println(new Date() + " Seedpair done");
        }
        System.out.println(new Date() + " Location: " + location);
    }

    private static Long getDestination(List<String> lines, int startIndex, Long addValue) {
        var counter = 1;
        String line = lines.get(startIndex + counter);
        while (!"".equals(line)) {
            var destinationStart = Long.parseLong(line.split(" ")[0].trim());
            var sourceStart = Long.parseLong(line.split(" ")[1].trim());
            var range = Long.parseLong(line.split(" ")[2].trim());

            if (sourceStart <= addValue && addValue <= sourceStart + range - 1) {
                var diff = destinationStart - sourceStart;
                return addValue + diff;
            }
            counter++;
            if ((startIndex + counter) >= lines.size()) {
                line = "";
            } else {
                line = lines.get(startIndex + counter);
            }
        }

        return addValue;
    }


    private static Map<Long, Long> createMap(List<String> lines, int lineIndex, Collection<Long> valuesToAdd) {
        var counter = 1;
        String line = lines.get(lineIndex + counter);
        Map<Long, Long> map = new HashMap<>();
        while (!"".equals(line)) {
            var destinationStart = Long.parseLong(line.split(" ")[0].trim());
            var sourceStart = Long.parseLong(line.split(" ")[1].trim());
            var range = Long.parseLong(line.split(" ")[2].trim());

            for (long key : valuesToAdd) {
                if (sourceStart < key && key < sourceStart + range - 1) {
                    var diff = key - sourceStart;
                    var value = destinationStart + diff;
                    map.put(key, value);
                }
            }

            counter++;
            if (lineIndex + counter >= lines.size()) {
                line = "";
            } else {
                line = lines.get(lineIndex + counter);
            }
        }

        return map;
    }

    private static Long getLocationForSeed(Long seed,
                                           Map<Long, Long> seedToSoilMap,
                                           Map<Long, Long> soilToFertilizerMap,
                                           Map<Long, Long> fertilizerToWaterMap,
                                           Map<Long, Long> waterToLightMap,
                                           Map<Long, Long> lightToTemperatureMap,
                                           Map<Long, Long> temperatureToHumidityMap,
                                           Map<Long, Long> humidityToLocationMap) {

        Long soil = seedToSoilMap.getOrDefault(seed, seed);
        Long fertilizer = soilToFertilizerMap.getOrDefault(soil, soil);
        Long water = fertilizerToWaterMap.getOrDefault(fertilizer, fertilizer);
        Long light = waterToLightMap.getOrDefault(water, water);
        Long temperature = lightToTemperatureMap.getOrDefault(light, light);
        Long humidity = temperatureToHumidityMap.getOrDefault(temperature, temperature);
        return humidityToLocationMap.getOrDefault(humidity, humidity);
    }

    private static List<Long> getSeeds(List<String> seedsMap) {
        System.out.println(new Date() + " start getting seeds");
        List<Long> result = new ArrayList<>();
        var listSize = seedsMap.size();
        for (int counter = 0; counter < listSize; counter += 2) {
            var firstSeed = seedsMap.get(counter);
            var range = seedsMap.get(counter + 1);
            for (int adder = 0; adder < Integer.parseInt(range); adder++) {
//                System.out.println(range + "adder");
                result.add(Long.parseLong(firstSeed) + (long) adder);
            }
        }

        System.out.println(new Date() + " got seeds");

        return result;
    }

    private static Map<Long, Long> getSeedPairs(List<String> seedsMap) {
        var counter = 0;
        Map<Long, Long> result = new HashMap<>();
        while (counter < seedsMap.size()) {
            result.put(Long.parseLong(seedsMap.get(counter)), Long.parseLong(seedsMap.get(counter + 1)));
            counter += 2;
        }
        return result;
    }

    public static String readFileAsString(String filePath) throws IOException {
        byte[] encodedBytes = Files.readAllBytes(Paths.get(filePath));
        return new String(encodedBytes);
    }
}

class SDI {
    long destination;
    long source;
    long increment;

    public SDI(long destination, long source, long increment) {
        this.destination = destination;
        this.source = source;
        this.increment = increment;
    }
}

class Mapping {
    String name;
    List<SDI> sdis;

    public Mapping() {
        sdis = new ArrayList<>();
    }
}
