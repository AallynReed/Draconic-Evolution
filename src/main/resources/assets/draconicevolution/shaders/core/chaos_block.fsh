#version 150

#moj_import <fog.glsl>
#moj_import <dynamictransforms.glsl>
#moj_import <brandonscore:math.glsl>
#moj_import <brandonscore:chaos.glsl>
#moj_import <draconicevolution:chaos_block_uniforms.glsl>

uniform sampler2D Sampler0;

in vec3 fPos;
in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 normal;
in vec2 posMod;

out vec4 fragColor;

void main() {
    vec4 col = chaos(Sampler0, Time, Yaw, Pitch, Alpha, fPos, posMod);

    col *= vertexColor * ColorModulator;

    fragColor = apply_fog(col, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
