package com.example.trains;

/**
 * Dot convertor application.
 */
public class DotConvertorApplication {

    /**
     * Application entry point.
     *
     * @param args  program arguments, being source file name
     */
    public static void main(String[] args) {
        String filename = args.length != 0 ? args[0] : "graph.txt";
        new DotConvertor().convert(filename);
        System.out.println("Conversion done with success");
    }
}
