#version 150

#moj_import <fog.glsl>
#moj_import <dynamictransforms.glsl>
#moj_import <brandonscore:math.glsl>
#moj_import <draconicevolution:tool_uniforms.glsl>

in vec3 fPos;
in vec3 vPos;
in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;
in vec3 vNorm;

out vec4 fragColor;

void main() {
    float intensity = 1;
    vec3 coord = vec3(texCoord0, 0.0);

    coord.x += ((fPos.x + vNorm.x) / 10);
    coord.y += ((fPos.y + vNorm.y) / 10);

    float density = 8;
    float brightness = BaseColor.w;
    for (int i = 1; i <= 5; i++) {
        float power = pow(2, float(i));
        brightness += (2 / power) * max(snoise(coord + vec3(Time * -0.05, Time * 0.16, Time * -0.02), power * density), -1);
    }
    brightness = max(brightness, 0);

    vec4 color = vec4(pow(brightness * BaseColor.r, 3 * (1.0-BaseColor.r)), pow(brightness * BaseColor.g, 3*(1.0-BaseColor.g)), pow(brightness * BaseColor.b, 3*(1.0-BaseColor.b)), 1.0);

    color *= vertexColor * ColorModulator;
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;

    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
