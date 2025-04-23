// Vienna U-Bahn Route Finder - LineAwareDijkstra.java
package com.example.viennaroutefinderdsaca2.DataStructure;

import java.util.*;

public class LineAwareDijkstra {
    public static class StationPath {
        public List<GraphNode<String>> path = new ArrayList<>();
        public int totalCost;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("Path (Cost: ").append(totalCost).append(") -> ");
            for (GraphNode<String> node : path) {
                sb.append(node.getName()).append(" -> ");
            }
            return sb.substring(0, sb.length() - 4);
        }
    }

    public static StationPath findPathWithPenalty(GraphNode<String> start, GraphNode<String> end, int penalty, Set<GraphNode<String>> avoidNodes) {
        class Entry {
            GraphNode<String> node;
            int cost;
            String currentLine;
            List<GraphNode<String>> path;

            Entry(GraphNode<String> node, int cost, String currentLine, List<GraphNode<String>> path) {
                this.node = node;
                this.cost = cost;
                this.currentLine = currentLine;
                this.path = new ArrayList<>(path);
                this.path.add(node);
            }
        }

        PriorityQueue<Entry> queue = new PriorityQueue<>(Comparator.comparingInt(e -> e.cost));
        Set<GraphNode<String>> visited = new HashSet<>();

        queue.add(new Entry(start, 0, null, new ArrayList<>()));

        while (!queue.isEmpty()) {
            Entry current = queue.poll();

            if (visited.contains(current.node)) continue;
            visited.add(current.node);

            if (current.node.equals(end)) {
                StationPath result = new StationPath();
                result.path = current.path;
                result.totalCost = current.cost;
                return result;
            }

            for (GraphLink<String> link : current.node.adjList) {
                GraphNode<String> neighbor = link.destNode;
                if (avoidNodes.contains(neighbor) || visited.contains(neighbor)) continue;

                String nextLine = link.lineName;
                int transitionPenalty = (current.currentLine != null && !current.currentLine.equals(nextLine)) ? penalty : 0;

                queue.add(new Entry(neighbor, current.cost + link.cost + transitionPenalty, nextLine, current.path));
            }
        }
        return null;
    }
}
