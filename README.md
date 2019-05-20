# Graphy - Graph Visualisation for BFS, DFS, Dijkstra algorithms
#

## Credits:
- **Nguyen Gia Bach:** 
	- `IntroWindow` 
	- `AboutWindow` 
	- `GraphWindow` 
		- create components
		- event handlers 
		- create new node: map each `Point` onclick with each `node` in list of `<node, adjacent nodes>`
		- drag nodes
		- delete nodes 
	- Animation: dotted line moving from `Source` to `Destination`
	- Class diagram
	- Use-Case diagram
- **Pham Tuan Son:** 
	- `Algorithms` 
	- `GraphWindow`: 
		- implement algorithms with list of nodes: `<node, adjacent nodes>` 
		- graphics
		- create new edge: drag and drop
		- Handle `Full mode` + `Step mode` in simulation

## Manual:
- Simulation:
	- **Create node**: click Left Mouse Button (`LMB`)
	- **Create edge**: click and hold `LMB` a node, drag and drop to the other node
	- **Move node**: click and hold Wheel Button
	- **Delete node**: click Right Mouse Button (`RMB`) a node
	- **Select source**: double click `LMB` a node 
	- **Select destination**: double click `LMB` the other node
	- **Choose algorithm**: select Combo box
	- Click `Play` to **start simulation**:
		- `Full mode`: other buttons untouched
		- `Step mode`: click `Pause` to pause simulation -> click `step` buttons (`<<`,`<`,`>`,`>>`) to change step