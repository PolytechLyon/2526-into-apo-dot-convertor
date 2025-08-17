package com.example.trains;

/**
 * Graph edge data in a text file
 *
 * @param sourceIndex   source vertex index
 * @param targetIndex   target vertex index
 * @param weight        edge weight
 */
public record EdgeData(
        int sourceIndex,
        int targetIndex,
        double weight
) {}
