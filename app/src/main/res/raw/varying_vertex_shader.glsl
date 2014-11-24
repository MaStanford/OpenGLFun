attribute vec4 aPosition;
attribute vec4 aPointSize;
attribute vec4 aColor;

varying vec4 vColor;

uniform mat4 uMVPMatrix;

void main() {
    vColor = aColor;
    //gl_Position = uMVPMatrix * vPosition;
    gl_Position = aPosition;
    gl_PointSize = 10.0;
}