#version 330

layout(std140) uniform BCUniforms {
    mat4 ModelMat;
    float Time;
    float Decay;
    float Activation;
    vec4 BaseColour;
    int BarMode;
};
