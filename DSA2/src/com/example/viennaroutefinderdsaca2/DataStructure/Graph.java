package com.example.viennaroutefinderdsaca2.DataStructure;

import com.example.viennauroutefinderdsaca2.BWConverter;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.*;

public class Graph implements Initializable {
    public static Graph graph;

    public static class CostedPath {
        public int pathCost = 0;
        public List<GraphNode<?>> pathList = new ArrayList<>();
        public int index;

        @Override
        public String toString() {
            StringBuilder pathString = new StringBuilder("[ ROUTE " + index + " ]  -   PATH COST : " + pathCost + "\n\n");
            for (int i = 0; i < pathList.size(); i++) {
                GraphNode<?> node = pathList.get(i);
                pathString.append(node.getName());
                if (i < pathList.size() - 1) {
                    pathString.append("  ->  ");
                }
            }
            return pathString + "\n\n";
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public static <T> CostedPath findCheapestPathDijkstra(GraphNode<T> startNode, T lookingFor, Set<GraphNode<T>> nodesToAvoid) {
        CostedPath cp = new CostedPath();
        LinkedList<GraphNode<T>> encountered = new LinkedList<>();
        LinkedList<GraphNode<T>> notEncountered = new LinkedList<>();
        startNode.nodeValue = 0;
        notEncountered.add(startNode);
        GraphNode<T> currentNode;

        if (nodesToAvoid == null) nodesToAvoid = new HashSet<>();

        do {
            currentNode = notEncountered.removeFirst();
            encountered.add(currentNode);

            if (currentNode.name.equals(lookingFor)) {
                cp.pathList.add(currentNode);
                cp.pathCost = currentNode.nodeValue;

                while (currentNode != startNode) {
                    for (GraphNode<T> n : encountered) {
                        for (GraphLink<T> e : n.adjList) {
                            if (e.destNode == currentNode &&
                                currentNode.nodeValue - e.cost == n.nodeValue &&
                                !nodesToAvoid.contains(n)) {
                                cp.pathList.add(0, n);
                                currentNode = n;
                                break;
                            }
                        }
                    }
                }

                for (GraphNode<T> n : encountered) n.nodeValue = Integer.MAX_VALUE;
                for (GraphNode<T> n : notEncountered) n.nodeValue = Integer.MAX_VALUE;
                return cp;
            }

            for (GraphLink<T> e : currentNode.adjList) {
                if (!encountered.contains(e.destNode) && !nodesToAvoid.contains(e.destNode)) {
                    e.destNode.nodeValue = Integer.min(e.destNode.nodeValue, currentNode.nodeValue + e.cost);
                    if (!notEncountered.contains(e.destNode)) notEncountered.add(e.destNode);
                }
            }

            notEncountered.sort(Comparator.comparingInt(n -> n.nodeValue));
        } while (!notEncountered.isEmpty());

        return null;
    }

    public static <T> List<CostedPath> searchGraphDepthFirst(GraphNode<?> from, List<GraphNode<?>> encountered, int totalCost, T lookingFor, Set<GraphNode<T>> nodesToAvoid) {
        List<CostedPath> allPaths = new ArrayList<>();

        if (from.name.equals(lookingFor)) {
            CostedPath cp = new CostedPath();
            cp.pathList.add(from);
            cp.pathCost = totalCost;
            allPaths.add(cp);
            return allPaths;
        }

        if (nodesToAvoid == null) nodesToAvoid = new HashSet<>();
        if (encountered == null) encountered = new ArrayList<>();
        encountered.add(from);

        for (GraphLink adjLink : from.adjList) {
            if (!encountered.contains(adjLink.destNode) && !nodesToAvoid.contains(adjLink.destNode)) {
                List<CostedPath> pathsFromAdj = searchGraphDepthFirst(adjLink.destNode, encountered, totalCost + adjLink.cost, lookingFor, nodesToAvoid);
                for (CostedPath path : pathsFromAdj) {
                    path.pathList.add(0, from);
                    allPaths.add(path);
                }
            }
        }
        return allPaths;
    }

    public static ArrayList<Point2D> searchBFS(Point2D start, Point2D end) {
        Image im = BWConverter.convert();
        int width = (int) im.getWidth();
        int height = (int) im.getHeight();

        Set<Point2D> visitedPixels = new HashSet<>();
        Queue<Point2D> queue = new LinkedList<>();
        Map<Point2D, Point2D> cameFrom = new HashMap<>();

        queue.add(start);
        visitedPixels.add(start);

        while (!queue.isEmpty()) {
            Point2D current = queue.poll();
            if (current.equals(end)) {
                ArrayList<Point2D> path = new ArrayList<>();
                while (current != null) {
                    path.add(0, current);
                    current = cameFrom.get(current);
                }
                return path;
            }

            for (Point2D neighbor : getNeighbors(current, width, height)) {
                if (!visitedPixels.contains(neighbor)) {
                    queue.add(neighbor);
                    visitedPixels.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }

        return new ArrayList<>();
    }

    private static List<Point2D> getNeighbors(Point2D current, int width, int height) {
        List<Point2D> neighbors = new ArrayList<>();
        int[] pixelArray = BWConverter.getPixelArray();
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, -1, 0, 1};

        int currX = (int) current.getX();
        int currY = (int) current.getY();

        for (int i = 0; i < dx.length;
    }
}
