attribute vec4 vPosition;

uniform mat4 uMVPMatrix;

void main() {
    //gl_Position = uMVPMatrix * vPosition;
    gl_Position = vPosition;
}