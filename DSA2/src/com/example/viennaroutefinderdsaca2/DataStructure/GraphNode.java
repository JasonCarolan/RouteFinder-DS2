package com.example.viennaroutefinderdsaca2.DataStructure;

import java.util.ArrayList;
import java.util.List;

public class GraphNode<T> {
    public T name;
    public int nodeValue = Integer.MAX_VALUE;
    public List<GraphLink<T>> adjList = new ArrayList<>();
    private final boolean isLandmark;
    private final int culturalSignificance;
    private int graphX;
    private int graphY;

    public GraphNode(T name, boolean isLandmark, int culturalSignificance, int graphX, int graphY) {
        this.name = name;
        this.isLandmark = isLandmark;
        this.culturalSignificance = culturalSignificance;
        this.graphX = graphX;
        this.graphY = graphY;
    }

    public boolean isLandmark() {
        return isLandmark;
    }

    public T getName() {
        return name;
    }

    public int getGraphX() {
        return graphX;
    }

    public int getGraphY() {
        return graphY;
    }

    public int getCulturalSignificance() {
        return culturalSignificance;
    }

    public List<GraphLink<T>> getAdjList() {
        return adjList;
    }

    public int connectToNodeUndirected(GraphNode<T> destNode, int cost, String lineName) {
        adjList.add(new GraphLink<>(destNode, cost, lineName));
        destNode.adjList.add(new GraphLink<>(this, cost, lineName));
        return cost;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
