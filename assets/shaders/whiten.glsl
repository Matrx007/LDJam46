#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D texture;

varying vec4 vertColor;
varying vec2 texCoord;

uniform float whiteness;

void main() {
    gl_FragColor = texture2D(texture, texCoord) * vertColor;
}