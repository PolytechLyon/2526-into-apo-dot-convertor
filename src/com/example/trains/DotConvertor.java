package com.example.trains;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

/**
 * Directed graph format convertor.
 * This class converts graph from text format to dot format.
 */
public class DotConvertor {

    /**
     * Maximum accepted number of vertices
     */
    private static final int MAX_VERTICES_NUMBER = 64;

    /**
     * Convert a file with the name {@code sourceFilename} to dot format.
     *
     * @param sourceFilename    source filename
     */
    public void convert(String sourceFilename) {
        // Output filename: same as input with extension replaced with "dot".
        String targetFilename = sourceFilename.split("\\.")[0].concat(".dot");
        // Open target file for writing
        try (PrintStream output = new PrintStream(targetFilename)) {
            // Read all source file lines
            List<String> lines = readLines(sourceFilename);
            // Process the read lines
            processLines(lines, output);
        } catch (IOException e) {
            // catch and wrap checked exceptions
            throw new RuntimeException(e);
        }
    }

    private void processLines(List<String> lines, PrintStream output) {
        // Vertices array, initially empty (all nulls)
        String[] vertices = new String[MAX_VERTICES_NUMBER];
        // Current index
        int index = 0;
        // Which part of the file is being read, start with vertices
        FilePart part = FilePart.VERTICES;
        // Print out dot file header
        output.printf("digraph {%n");
        // For each line in the source file
        for (String line : lines) {
            if (line.isBlank()) {
                // a blank line delimits the vertices part
                part = FilePart.EDGES;
            } else if (part == FilePart.VERTICES) {
                // While still reading vertices, add a vertex to the array
                vertices[index++] = line;
            } else {
                // if all vertices are read, analyse edge line
                EdgeData edgeData = readEdgeData(line);
                String source = vertices[edgeData.sourceIndex()];
                String target = vertices[edgeData.targetIndex()];
                // print out the directed edge line
                output.printf(
                        "\"%s\" -> \"%s\" [label=%.2f arrowhead=normal]%n",
                        source,
                        target,
                        edgeData.weight()
                );
            }
        }
        // print out dot file footer
        output.printf("}%n");
    }

    /**
     * Read all text lines of a file.
     *
     * @param filename the filename.
     * @return  a list of all text line within the file.
     */
    private List<String> readLines(String filename) throws IOException {
        // Path to source file
        Path path = Path.of(filename);
        // Read all file lines at once
        return Files.readAllLines(path);
    }

    /**
     * Read edge data out of comma-seperated line.
     *
     * @param line comma-seperated line
     * @return  edge data object.
     */
    private EdgeData readEdgeData(String line) {
        // Analyse the line to extract 3 comma-separated elements
        String[] params = line.split(",");
        if (params.length < 3) {
            // if the line doesn't contain 3 elements, throw an exception
            throw new RuntimeException("Illegible line %s".formatted(line));
        }
        // first element is source index
        int sourceIndex = parseInt(params[0].trim());
        // second element is target index
        int targetIndex = parseInt(params[1].trim());
        // third element is weight
        double weight = parseDouble(params[2].trim());
        return new EdgeData(sourceIndex, targetIndex, weight);
    }
}
