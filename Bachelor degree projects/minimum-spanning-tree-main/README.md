# minimum-spanning-tree

The objective of the program is the implementation and application, on an undirected and weighted graph with non-negative weights, of the Prim's algorithm, which aims to create a Minimum Spanning Tree (MST). The MST contains the minimum weight paths that connect the root of the tree to each of its elements, and the total sum of the weights of all the paths is minimized.

Languages: SWI-Prolog 8.3.9, Common Lisp 7.1.2

### Prolog implementation

- Graphs in memory are represented by facts graph/1, vertex/2, arc/4.
- Heap in memory is represented by the facts heap/2, heap_entry/4.
- The MST in memory is represented by the facts vertex_key/3, vertex_previous/3.

### List implementation

- Graphs in memory are represented using the contents of hash tables Graphs, Vertices, and Arcs.
- To keep track of the various heaps in memory, the hash table Heaps is used. However, the heap itself is implemented using an array.
- The MST (Minimum Spanning Tree) in memory is represented by the contents of the hash tables Vertex-keys and Previous.


### creators
catta991 AndreaRivaIT adamInajjar