attribute vec4 aPosition;
attribute float aPointSize;

void main() {
    gl_Position = aPosition;
    gl_PointSize = aPointSize;
}