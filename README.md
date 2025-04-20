# RouteFinder-DS2
🎯 Core Functionality
  The app is a JavaFX-based route-finding system for Vienna's U-Bahn. It helps users:
  Find routes between two stations on the U-Bahn system.
  Visualize routes on a Vienna map (using station coordinates).
  Choose from four different pathfinding strategies:
  DFS – to list all possible valid routes.
  BFS – to find the fewest station hops (using image pixel proximity).
  Dijkstra – to find the shortest-distance route (based on cost/distance).
  Dijkstra with Line Change Penalty – shortest route, but minimizes train transfers.
🧠 Algorithms Implemented
  ✅ Depth-First Search (searchGraphDepthFirst)
  → Enumerates all possible paths between two stations.
  ✅ Breadth-First Search (searchBFS)
  → Finds the path with fewest station stops using pixel distance (from greyscale Vienna map).
  ✅ Dijkstra’s Algorithm (findCheapestPathDijkstra)
  → Finds the shortest route in distance/cost, avoiding user-specified stations.
  ✅ Dijkstra’s with Penalty (LineAwareDijkstra)
  → Same as above, but applies user-defined penalty for every train line change.
🖼️ GUI Features
  📍 Draws station landmarks on a live map (ViennaMap.png)
  🎚️ Station selection dropdowns for:
  Start station
  Destination
  Waypoints (must-visit)
Avoid (stations to avoid)
  🖱️ Tooltip hover on map to show station names + info
  📋 Shows DFS result list of routes
  🎛️ Sliders and buttons for route calculation
  🎨 Map highlights paths using colored lines
  💾 Data Loading
  Loads the full graph from a prebuilt XML file: viennaGraphNodes.xml
  Graph includes:
  Station name
  Coordinates
  Landmark status
  Cultural significance
  Connections to other stations
Line names (e.g. U1, U2)
  🧰 Additional Capabilities
  ✅ Add stations to “avoid” and “must visit”
  ✅ Minimize or close the app from GUI
  ✅ Tooltip-based map interactivity
  ✅ Modular design using MVC and JavaFX FXML
  ✅ Optional CSV loader available for parsing data into XML format
