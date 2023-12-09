package main.java.org.example.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day8_part1 {
    public static void day8_part1() throws IOException {
        List<String> lines = Files.readAllLines( Path.of( "src\\main\\resources\\input_day8.txt" ) );
        String instructions = lines.get( 0 ).trim();
        int instructionLength = ( int ) instructions.chars().count();
        lines.remove( 0 );
        lines.remove( 0 );
        List<Row> rows = new ArrayList<>();

        for (String line : lines) {
            var split = line.split( " = " );
            Row row = new Row();
            row.setNode( split[0].trim() );
            var leftRight = split[1].trim().split( ", " );
            row.setLeft( leftRight[0].trim().replaceAll( "\\(", "" ) );
            row.setRight( leftRight[1].trim().replaceAll( "\\)", "" ) );
            rows.add( row );
        }

        int counter = getCounterPart1( rows, instructionLength, instructions );

        System.out.println(counter);
    }

    private static int getCounterPart1( List<Row> rows, int instructionLength, String instructions ) {
        int counter = 0;
        int index = 0;
        boolean found = false;
        Row row = new Row();

        for (Row r : rows ) {
            if ( Objects.equals( r.getNode(), "AAA" ) ){
                row = r;
                break;
            }
        }

        while (!found) {
            if (index >= instructionLength ) {
                index -= instructionLength;
            }
            var instruction = instructions.charAt( index );
            var nextNode = "";
            if (instruction == 'R') {
                nextNode = row.getRight();
            } else {
                nextNode = row.getLeft();
            }

            if ( Objects.equals( nextNode, "ZZZ" ) ) {
                found = true;
            }

            for (Row r : rows ) {
                if ( Objects.equals( r.getNode(), nextNode ) ){
                    row = r;
                    break;
                }
            }
            counter++;
            index++;
        }
        return counter;
    }
}

class Row {
    private String node, left, right;

    public String getNode() {
        return node;
    }

    public void setNode( String node ) {
        this.node = node;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft( String left ) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight( String right ) {
        this.right = right;
    }

    public boolean isNode(String input) {
        return Objects.equals( this.getNode(), input );
    }

    public boolean isStartRow() {
        return this.getNode().charAt( 2 ) == 'A';
    }

    public boolean isEndRow() {
        return this.getNode().charAt( 2 ) == 'Z';
    }

    public boolean allSameValue() {
        return Objects.equals( this.getNode(), this.getLeft() ) && Objects.equals( this.getLeft(), this.getRight() );
    }
}
