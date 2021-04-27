#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform vec2 screenPixelSize;

uniform sampler2D texture;

varying vec4 vertColor;
varying vec4 vertTexCoord;

void main() {
    vec4 color = texture2D(texture, vertTexCoord.st) * vertColor;
    gl_FragColor = color;
    if(color != vec4(0, 0, 0, 1)) {
        gl_FragColor = color;
        return;
    }

    float unitX = 1.0 / screenPixelSize.x;
    float unitY = 1.0 / screenPixelSize.y;

    // Find distance from coast
    int distance = 0;
    for(int i = 1; i < 6; i++) {
        if(texture2D(texture, vertTexCoord.st - vec2(0, i*unitY)) != vec4(0, 0, 0, 1)) continue;
        distance++;
    }

    gl_FragColor = texture2D(texture, vec2(vertTexCoord.s, vertTexCoord.t-unitY)) * vertColor;
}