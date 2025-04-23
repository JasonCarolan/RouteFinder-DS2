package com.example.viennaroutefinderdsaca2.DataStructure;

public class GraphLink<T> {
    public GraphNode<T> destNode;
    public int cost;
    public String lineName;

    public GraphLink(GraphNode<T> destNode, int cost, String lineName) {
        this.destNode = destNode;
        this.cost = cost;
        this.lineName = lineName;
    }

    public GraphNode<T> getDestNode() {
        return destNode;
    }

    public int getCost() {
        return cost;
    }

    public String getLineName() {
        return lineName;
    }
}
