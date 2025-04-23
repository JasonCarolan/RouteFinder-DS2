// Vienna U-Bahn Route Finder - RouteFinder.java (Controller)
package com.example.viennaroutefinderdsaca2;

import com.example.viennaroutefinderdsaca2.DataStructure.BWConverter;
import com.example.viennauroutefinderdsaca2.DataStructure.Graph;
import com.example.viennauroutefinderdsaca2.DataStructure.GraphLink;
import com.example.viennauroutefinderdsaca2.DataStructure.GraphNode;
import com.example.viennauroutefinderdsaca2.DataStructure.LineAwareDijkstra;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.io.FileReader;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.*;

public class RouteFinder implements Initializable {
    public static RouteFinder routeFinder;

    @FXML public AnchorPane mapPane;
    @FXML public ImageView mapView;
    @FXML public ComboBox<GraphNode<String>> startPointBox;
    @FXML public ComboBox<GraphNode<String>> endPointBox;
    @FXML public ComboBox<GraphNode<String>> avoidBox;
    @FXML public ComboBox<GraphNode<String>> waypointsBox;
    @FXML public RadioButton dfsButton, bfsButton, dijkstraButton;
    @FXML public ToggleGroup algoSelection;
    @FXML public ListView<Graph.CostedPath> dfsListView;
    @FXML public Slider historicalVal;
    @FXML public Label systemMessage, avoidingLabel, visitLabel;

    private final Color visit = Color.rgb(0, 191, 99);
    private final Color route = Color.rgb(13, 137, 232);
    private final Color landMark = Color.rgb(0, 74, 173);

    private Map<String, GraphNode<String>> graphNodes = new HashMap<>();
    private Set<GraphNode<String>> avoidNodes = new HashSet<>();
    private Set<GraphNode<String>> waypointNodes = new HashSet<>();

    private Tooltip nodeTip;
    private boolean isMapPopulated = false;
    private Circle circle;
    private Text text;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        routeFinder = this;
        nodeTip = new Tooltip();
        mapPane.setOnMouseMoved(this::toolTipHover);
        Image mapViewImage = new Image(Objects.requireNonNull(BWConverter.class.getResourceAsStream("/com/example/viennauroutefinderdsaca2/resources/ViennaMap.png")));
        mapView.setImage(mapViewImage);
        populateDatabase();
    }

    public void populateDatabase() {
        try {
            Class<?>[] classes = new Class[]{GraphNode.class, GraphLink.class};
            XStream xstream = new XStream(new DomDriver());
            XStream.setupDefaultSecurity(xstream);
            xstream.allowTypes(classes);
            ObjectInputStream is = xstream.createObjectInputStream(new FileReader("viennaGraphNodes.xml"));
            graphNodes = Collections.unmodifiableMap((Map<String, GraphNode<String>>) is.readObject());
            is.close();
            populateMap();
        } catch (Exception e) {
            System.err.println("Error loading graph: " + e.getMessage());
        }
    }

    public void populateMap() {
        if (isMapPopulated) return;
        for (GraphNode<String> node : graphNodes.values()) {
            if (node.isLandmark()) {
                drawNode(node, landMark);
                startPointBox.getItems().add(node);
                endPointBox.getItems().add(node);
                avoidBox.getItems().add(node);
                waypointsBox.getItems().add(node);
            }
        }
        isMapPopulated = true;
    }

    private void drawNode(GraphNode<String> node, Color color) {
        Circle c = new Circle(node.getGraphX(), node.getGraphY(), 5, color);
        Circle ring = new Circle(node.getGraphX(), node.getGraphY(), 8);
        ring.setStroke(color);
        ring.setFill(Color.TRANSPARENT);
        ring.setStrokeWidth(2);
        mapPane.getChildren().addAll(c, ring);
    }

    public void findRoute() {
        if (startPointBox.getValue() == null || endPointBox.getValue() == null || algoSelection.getSelectedToggle() == null) {
            Utils.showWarningAlert("Missing Input", "Select start, end, and algorithm");
            return;
        }

        clearLines();
        GraphNode<String> start = startPointBox.getValue();
        GraphNode<String> end = endPointBox.getValue();

        if (algoSelection.getSelectedToggle().equals(dfsButton)) {
            List<Graph.CostedPath> paths = Graph.searchGraphDepthFirst(start, null, 0, end.getName(), avoidNodes);
            dfsListView.getItems().clear();
            int i = 1;
            for (Graph.CostedPath p : paths) {
                p.setIndex(i++);
                dfsListView.getItems().add(p);
            }
        } else if (algoSelection.getSelectedToggle().equals(bfsButton)) {
            Point2D p1 = new Point2D(start.getGraphX(), start.getGraphY());
            Point2D p2 = new Point2D(end.getGraphX(), end.getGraphY());
            List<Point2D> path = Graph.searchBFS(p1, p2);
            drawPath(path);
        } else if (algoSelection.getSelectedToggle().equals(dijkstraButton)) {
            Graph.CostedPath cp = Graph.findCheapestPathDijkstra(start, end.getName(), avoidNodes);
            if (cp != null) drawGraphPath(cp.pathList);
        }
    }

    public void findPathWithLinePenalty(int penalty) {
        GraphNode<String> start = startPointBox.getValue();
        GraphNode<String> end = endPointBox.getValue();
        LineAwareDijkstra.StationPath result = LineAwareDijkstra.findPathWithPenalty(start, end, penalty, avoidNodes);
        if (result != null) drawGraphPath(result.path);
    }

    private void drawGraphPath(List<GraphNode<?>> path) {
        clearLines();
        for (int i = 0; i < path.size() - 1; i++) {
            Line l = new Line(path.get(i).getGraphX(), path.get(i).getGraphY(), path.get(i + 1).getGraphX(), path.get(i + 1).getGraphY());
            l.setStroke(route);
            l.setStrokeWidth(3);
            mapPane.getChildren().add(l);
        }
    }

    private void drawPath(List<Point2D> points) {
        clearLines();
        for (int i = 0; i < points.size() - 1; i++) {
            Line line = new Line(points.get(i).getX(), points.get(i).getY(), points.get(i + 1).getX(), points.get(i + 1).getY());
            line.setStroke(route);
            line.setStrokeWidth(3);
            mapPane.getChildren().add(line);
        }
    }

    private void clearLines() {
        mapPane.getChildren().removeIf(n -> n instanceof Line);
    }

    public void toolTipHover(MouseEvent e) {
        double x = e.getX(), y = e.getY();
        for (GraphNode<String> g : graphNodes.values()) {
            if (g.isLandmark() && Math.abs(x - g.getGraphX()) <= 3 && Math.abs(y - g.getGraphY()) <= 3) {
                mapPane.setCursor(Cursor.HAND);
                nodeTip.setText(g.getName() + "\nSignificance: " + g.getCulturalSignificance());
                nodeTip.show(mapView, e.getScreenX() + 10, e.getScreenY() + 10);
                return;
            }
        }
        mapPane.setCursor(Cursor.DEFAULT);
        nodeTip.hide();
    }

    public void addToAvoid() {
        GraphNode<String> n = avoidBox.getValue();
        if (n != null && !avoidNodes.contains(n)) {
            avoidNodes.add(n);
            avoidingLabel.setText("Avoiding: " + n.getName());
        }
    }

    public void addToWaypoint() {
        GraphNode<String> n = waypointsBox.getValue();
        if (n != null && !waypointNodes.contains(n)) {
            waypointNodes.add(n);
            visitLabel.setText("Visiting: " + n.getName());
        }
    }

    public void closeApp() {
        System.exit(0);
    }

    public void minimiseApp() {
        Main.mainStage.setIconified(true);
    }
}
