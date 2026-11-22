package com.example.newproj.model

import androidx.compose.ui.graphics.Color

enum class StudioTool(val label: String) {
    ADJUST("Adjust"),
    FILTERS("Filters"),
    LAYERS("Layers"),
    TRANSFORM("Crop"),
    FX("Effects")
}

data class ImageAdjustments(
    val brightness: Float = 0f,      // -100f .. 100f
    val contrast: Float = 0f,        // -100f .. 100f
    val saturation: Float = 0f,      // -100f .. 100f
    val warmth: Float = 0f,          // -100f .. 100f
    val vignette: Float = 0f,        // 0f .. 100f
    val exposure: Float = 0f,        // -100f .. 100f
    val tint: Float = 0f             // -100f .. 100f
) {
    fun isDefault(): Boolean =
        brightness == 0f && contrast == 0f && saturation == 0f &&
                warmth == 0f && vignette == 0f && exposure == 0f && tint == 0f
}

enum class LayerType {
    BACKGROUND_SKY,
    NEBULA_AURORA,
    GEOMETRIC_SUN,
    CYBER_SKYLINE,
    PERSPECTIVE_GRID,
    TYPOGRAPHY_WATERMARK,
    VIGNETTE_OVERLAY
}

data class StudioLayer(
    val id: String,
    val name: String,
    val type: LayerType,
    val isVisible: Boolean = true,
    val opacity: Float = 1.0f,
    val blendModeName: String = "Normal"
)

data class FilterPreset(
    val id: String,
    val name: String,
    val subtitle: String,
    val previewColors: List<Color>,
    val adjustments: ImageAdjustments
)

enum class AspectRatioOption(val label: String, val ratio: Float?) {
    FREE("Free", null),
    SQUARE("1:1", 1.0f),
    PORTRAIT("4:5", 0.8f),
    STORY("9:16", 0.5625f),
    LANDSCAPE("16:9", 1.777f),
    CLASSIC("3:2", 1.5f)
}

enum class ExportFormat(val extension: String, val mimeType: String) {
    JPEG("JPG", "image/jpeg"),
    PNG("PNG", "image/png"),
    WEBP("WEBP", "image/webp")
}

enum class ExportResolution(val label: String, val dimensions: String) {
    FHD("1080p", "1920 x 1080"),
    QHD("2K", "2560 x 1440"),
    UHD("4K", "3840 x 2160")
}
