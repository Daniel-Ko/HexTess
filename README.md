A an in-place-array algorithm to create one of the various, perfect hexagon tessellations.
Implementation is in Java

An example of this tessellation:

![Hexagon Tessellation](https://qph.ec.quoracdn.net/main-qimg-004a26d70999fad8a1323bd219789b04-c)

The speciality of this algorithm is to find the most efficient vertex-assignment code as possible.

To do this, we note two observations:
1) The hexagon tessellation requires 30+ vertices to draw between. The majority of these vertices, however, are shared between multiple smaller shapes. 
2) The hexagon tessellation is mirrored across the x-axis.

Perhaps with this realisation we could also sense patterns in the geometry that would let us take advantage of the interconnectivity and symmetry. 
