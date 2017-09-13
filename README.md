An implementation in Java of an in-place-array algorithm to create one of the various perfect hexagon tessellations.

An example of the tessellation I am building:

https://qph.ec.quoracdn.net/main-qimg-004a26d70999fad8a1323bd219789b04-c

The speciality of this algorithm is to cut out as much redundant vertex-assignment code as possible.

Knowing the geometry of this pattern, given one vertex of the shape or of the bounding box surrounding it all, the entire tessellation can be found relative to the first. These relative calculations are useful if we want to make a whole board of the same patterns! We can map a grid like so:

1 2 3 4 5 6 7 8 9

and create 9 identical tessellations based only on its grid position.

But given the numerous shapes surrounding each hexagon, finding each vertex for each shape will be upwards of 30 points to map to different shapes and draw lines between, the majority of them belonging to multiple shapes. Furthermore, we can see that the hexagon tessellation is mirrored across the x-axis, but thanks to relative calculations, we must map both halves separately.

But, what if there was a pattern in finding the vertices between the square-triangle-square-etc. sequence?

Using just one x and y position array, we can create a sort of path between this nicely shaped, pointy graph. And to draw the bottom sequence, we need no more than to mirror the pattern and shift it down from the top. This would cut out a lot of repeated code and already-seen vertices!

One note, please:

While the Vertices Algorithm is sound, my implementation is lacking due to lack of knowledge. This will be quite evident when seeing how I don't have a way to draw yet and am fumbling with finding the right Shapes to store my points to. Please think of it as my kiddie swing at Java Swing. I hope to complete the implementation as I learn more!