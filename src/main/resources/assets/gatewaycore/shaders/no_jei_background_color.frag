#version 120

uniform sampler2D bgl_RenderedTexture;

const vec4 defaultColor = vec4(0.776, 0.776, 0.776, 1);
void main() {
    vec2 texcoord = vec2(gl_TexCoord[0]);
    vec4 color = texture2D(bgl_RenderedTexture, texcoord);
    vec4 delta = color - defaultColor;
    float dist = min(1, dot(delta, delta) * 1000);
    gl_FragColor = color * vec4(1.0, 1.0, 1.0, dist);
}