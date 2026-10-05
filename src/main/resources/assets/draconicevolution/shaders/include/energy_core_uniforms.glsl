#version 330

layout(std140) uniform BCUniforms {
    mat4 ModelMat;
    float Time;
    float Decay;
    float Activation;
    vec3 EffectColour;
    vec3 FrameColour;
    vec3 InnerTriColour;
};
