#version 120

attribute vec4 vPosition;

void main() {
    gl_position = vPosition;
}