#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;

uniform vec4 ColorModulate;
uniform float residue;

in vec2 texCoord;

out vec4 fragColor;

float easeOutQuint(in float x) {
    return 1f - pow(1f - x, 10f);
}

float linearizeDepth(in float depth)
{
    //return depth;
    float zNear = 0.5;
    float zFar  = 2000.0;
    return (2.0 * zNear) / (zFar + zNear - depth * (zFar - zNear));
}

void main(){
    float depth = easeOutQuint(linearizeDepth(texture(DiffuseDepthSampler, texCoord).x) - residue);
    depth = clamp(depth, 0.0, 1.0);
    vec4 color = texture(DiffuseSampler, texCoord);
    vec4 depthColor = vec4(depth, depth, depth, depth);
    color.xyz = color.xyz * (1f-depth);

    // transition to full fade
    float progress = smoothstep(0.2, 0.1, residue);
    color = mix(color, vec4(0.0, 0.0, 0.0, 1.0), progress);

    fragColor = (color) * ColorModulate;
}
