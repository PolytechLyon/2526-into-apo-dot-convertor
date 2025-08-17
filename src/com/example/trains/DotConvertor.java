package com.example.trains;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

/**
 * Directed graph format convertor.
 * This class converts graph from text format to dot format.
 */
public class DotConvertor {
    private String[] lines;
    private String[] vertices;

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
            readLines(sourceFilename);
            // Process the read lines
            processLines(output);
        } catch (IOException e) {
            // catch and wrap checked exceptions
            throw new RuntimeException(e);
        }
    }

    /**
     * Process line from the input file and print it out in the output file.
     *
     * @param output    output stream
     */
    private void processLines(PrintStream output) {
        // Calculate number of vertices
        int n = 0;
        while (n < lines.length && !lines[n].isBlank()) {
            n++;
        }
        // Vertices array, initially empty (all nulls)
        this.vertices = new String[n];
        // Read vertices
        for (int i = 0; i < n; i++) {
            // Add a vertex to the array
            this.vertices[i] = lines[i].trim();
        }
        // Print out dot file header
        output.printf("digraph {%n");
        // Read and print out edges
        for (int i = n + 1; i < lines.length; i++) {
            this.processEdge(output, i);
        }
        // Print out dot file footer
        output.printf("}%n");
    }

    /**
     * Read all text lines of a file.
     *
     * @param filename the filename.
     */
    private void readLines(String filename) throws IOException {
        // Path to source file
        Path path = Path.of(filename);
        // Read all file lines at once
        String content = Files.readString(path);
        this.lines = content.split("\n");
    }

    /**
     * Read edge data out of comma-seperated line.
     *
     * @param output    output stream
     * @param index     line index
     */
    private void processEdge(PrintStream output, int index) {
        String line = this.lines[index];
        if (line.isBlank()) {
            return;
        }
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
        String source = this.vertices[sourceIndex];
        String target = this.vertices[targetIndex];
        // print out the directed edge line
        output.printf(
                "\"%s\" -> \"%s\" [label=%.2f]%n",
                source,
                target,
                weight
        );
    }
}
