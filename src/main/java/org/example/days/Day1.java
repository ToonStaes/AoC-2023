package main.java.org.example.days;


import main.java.org.example.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Day1 {
    private static File getFile(String filename ) throws URISyntaxException {
        URL resource = Main.class.getClassLoader().getResource( filename );
        File file = null;
        if ( resource == null ) {
            throw new IllegalArgumentException( "file not found!" );
        } else {
            file = new File( resource.toURI() );
        }
        return file;
    }
    private static boolean isNumber( String input ) {
        var numbers = List.of( "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" );
        return numbers.contains( input );
    }
    private static int stringToNumber( String input ) {
        return switch ( input.toLowerCase() ) {
            case "zero" -> 0;
            case "one" -> 1;
            case "two" -> 2;
            case "three" -> 3;
            case "four" -> 4;
            case "five" -> 5;
            case "six" -> 6;
            case "seven" -> 7;
            case "eight" -> 8;
            case "nine" -> 9;
            default -> throw new IllegalStateException( "Unexpected value: " + input.toLowerCase() );
        };
    }
    private static void getCorrectNumbers( List<Integer> numbersToAdd, List<Character> numbersInString ) {
        if ( numbersInString.size() == 1 ) {
            numbersToAdd.add( Integer.valueOf( String.valueOf( numbersInString.get( 0 ).toString() + numbersInString.get( 0 ).toString() ) ) );
        } else if ( numbersInString.size() > 1 ) {
            var length = numbersInString.size();
            numbersToAdd.add( Integer.valueOf( String.valueOf( numbersInString.get( 0 ).toString() + numbersInString.get( length - 1 ).toString() ) ) );
        }
    }
    public static void day1_part1() throws URISyntaxException, IOException {
        var file = getFile( "input_day1.txt" );
        BufferedReader br = new BufferedReader( new FileReader( file ) );
        String st;
        List<Integer> numbersToAdd = new ArrayList<>();
        while ( (st = br.readLine()) != null ) {
            var stringLength = st.length();
            var counter = 0;
            List<Character> numbersInString = new ArrayList<>();
            while ( counter < stringLength ) {
                var character = st.charAt( counter );
                if ( isNumber( String.valueOf( character ) ) ) {
                    numbersInString.add( character );
                }
                counter++;
            }
            getCorrectNumbers( numbersToAdd, numbersInString );
        }
        var result = 0;
        for( int number : numbersToAdd ) {
            result += number;
        }
        System.out.println( "day 1 part 1: " + result );
    }
    public static void day1_part2() throws URISyntaxException, IOException {
        var file = getFile( "input_day1.txt" );
        BufferedReader br = new BufferedReader( new FileReader( file ) );
        String st;
        List<Integer> numbersToAdd = new ArrayList<>();
        while ( (st = br.readLine()) != null ) {
            var stringLength = st.length();
            List<Character> numbersInString = new ArrayList<>();
            var strNumbers = List.of( "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "zero" );
            var firstLetters = List.of( "o", "t", "f", "s", "e", "n", "z" );
            var counter = 0;
            while ( counter < stringLength ) {
                var character = st.charAt( counter );
                if ( isNumber( String.valueOf( character ) ) ) {
                    numbersInString.add( character );
                }
                else if ( firstLetters.contains( String.valueOf( character ) ) ) {
                    String three = "";
                    if ( (counter + 3) <= stringLength ) {
                        three =st.substring( counter, counter + 3 );
                    }
                    String four = "";
                    if ( (counter + 4) <= stringLength ) {
                        four = st.substring( counter, counter + 4 );
                    }
                    String five = "";
                    if ( (counter + 5) <= stringLength ) {
                        five = st.substring( counter, counter + 5 );
                    }
                    if ( strNumbers.contains( three ) ) {
                        var index = strNumbers.indexOf( three );
                        var numberString = strNumbers.get( index );
                        var number = stringToNumber( numberString );
                        numbersInString.add( ( char ) (number + 48) ); // +48 om van int naar char te gaan char 48 is 0, 49 is 1...
                    }
                    else if ( strNumbers.contains( four ) ) {
                        var index = strNumbers.indexOf( four );
                        var numberString = strNumbers.get( index );
                        var number = stringToNumber( numberString );
                        numbersInString.add( ( char ) (number + 48) ); // +48 om van int naar char te gaan char 48 is 0, 49 is 1...
                    }
                    else if ( strNumbers.contains( five ) ) {
                        var index = strNumbers.indexOf( five );
                        var numberString = strNumbers.get( index );
                        var number = stringToNumber( numberString );
                        numbersInString.add( ( char ) (number + 48) ); // +48 om van int naar char te gaan char 48 is 0, 49 is 1...
                    }
                }
                counter++;
            }
            getCorrectNumbers( numbersToAdd, numbersInString );
        }
        var result = 0;
        for( int number : numbersToAdd ) {
            result += number;
        }
        System.out.println("day 1 part 2: " + result );
    }
}
