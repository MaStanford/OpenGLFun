attribute vec4 vPosition;
attribute vec4 vPointSize;

uniform mat4 uMVPMatrix;

void main() {
    //gl_Position = uMVPMatrix * vPosition;
    gl_Position = vPosition;
    gl_PointSize = 10.0;
}