attribute vec4 aPosition;

void main() {
    gl_Position = aPosition;
    gl_PointSize = 2.0;
}