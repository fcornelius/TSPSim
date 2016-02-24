# TSPSim
A GUI Simulation for the *Travelling Salesman Problem* ([TSP](https://en.wikipedia.org/wiki/Travelling_salesman_problem)) and solving/approximation algorithms in comparison

<img src="https://github.com/phoelix/TSPSim/blob/master/ressources/gif/1.gif?raw=true" width="1000">
(english localization coming)

The following algorithms are currently implemented:

- Exact:
  - Brute-force (you guessed it)
  - Dynamic-programming
- Heuristics for opening:
  - Nearest neighbour
  - Best nearest neighbour
  - Minimum Spanning Tree (MST) Transform
- Optimization:
  - k-Opt (currently only 2-opt)

For testing and benchmarking purposes TSPSim is connected to a fork of Heidelberg's infamous TSP Library, allowing you to import any TSP File and make local changes to it. The Library is hosted at [tsplib.felixcornelius.de](http://tsplib.felixcornelius.de/)
### Creating a new instance
You have 3 options to create a new TSP instance:
Generation pseudo-random vertices of a given number, adding vertices by mouse input or entering a weighted adjacency matrix (experimental. very experimental.)<br>
A good practice strategy then would be to create a first tour with the Nearest neighbour method (or Best nearest neighbour, which is just independent of a starting point) and compare it with the transformed MST tour (click the stats checkbox for detailed tour info). The better one (which is NN in about 90% of all cases) can then be optimized by identifying and reconnecting crossing edges of vertex 2-pairs (2-Opt method)

### Other features
- Import/Export instance to TSPLib compatible .tsp File
- Export instance as PNG Graphic (with optional alpha channel)
- Color/weight customization of vertices and edges
- Define a background image for any TSP instance.<br>
   (with basic image manipulation such as zoom, translation and opacity). A few maps are also included

<img src="https://github.com/phoelix/TSPSim/blob/master/ressources/gif/2.gif?raw=true" width="400">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/phoelix/TSPSim/blob/master/ressources/gif/3.gif?raw=true" width="400">



#### TODO
   - [ ] English localization
   - [ ] Fix 2-Opt issues
   - [ ] 3-Opt implementation
   - [ ] Commandline version for speed
