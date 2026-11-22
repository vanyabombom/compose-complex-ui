package com.example.newproj.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newproj.model.*
import com.example.newproj.ui.components.*
import com.example.newproj.ui.theme.*

private val INITIAL_LAYERS = listOf(
    StudioLayer(
        id = "sky",
        name = "Atmosphere & Sky",
        type = LayerType.BACKGROUND_SKY,
        isVisible = true,
        opacity = 1.0f,
        blendModeName = "Normal"
    ),
    StudioLayer(
        id = "aurora",
        name = "Nebula Aurora & Stars",
        type = LayerType.NEBULA_AURORA,
        isVisible = true,
        opacity = 0.9f,
        blendModeName = "Screen"
    ),
    StudioLayer(
        id = "sun",
        name = "Synthwave Sun",
        type = LayerType.GEOMETRIC_SUN,
        isVisible = true,
        opacity = 1.0f,
        blendModeName = "Add"
    ),
    StudioLayer(
        id = "skyline",
        name = "Neon Cyber Skyline",
        type = LayerType.CYBER_SKYLINE,
        isVisible = true,
        opacity = 1.0f,
        blendModeName = "Normal"
    ),
    StudioLayer(
        id = "grid",
        name = "3D Horizon Grid",
        type = LayerType.PERSPECTIVE_GRID,
        isVisible = true,
        opacity = 0.85f,
        blendModeName = "Overlay"
    ),
    StudioLayer(
        id = "typo",
        name = "Signature Watermark",
        type = LayerType.TYPOGRAPHY_WATERMARK,
        isVisible = true,
        opacity = 0.75f,
        blendModeName = "Normal"
    )
)

@Composable
fun LuminaStudioScreen() {
    var adjustments by remember { mutableStateOf(ImageAdjustments()) }
    var layers by remember { mutableStateOf(INITIAL_LAYERS) }
    var selectedLayerId by remember { mutableStateOf(INITIAL_LAYERS.first().id) }

    var selectedTool by remember { mutableStateOf(StudioTool.ADJUST) }
    var selectedPresetId by remember { mutableStateOf<String?>(null) }
    var selectedRatio by remember { mutableStateOf(AspectRatioOption.SQUARE) }

    var rotationAngle by remember { mutableFloatStateOf(0f) }
    var isFlippedH by remember { mutableStateOf(false) }
    var isFlippedV by remember { mutableStateOf(false) }
    var showGrid by remember { mutableStateOf(false) }

    var isCompareActive by remember { mutableStateOf(false) }
    var showExportSheet by remember { mutableStateOf(false) }

    // History undo/redo stacks
    val undoStack = remember { mutableStateListOf<ImageAdjustments>() }
    val redoStack = remember { mutableStateListOf<ImageAdjustments>() }

    fun updateAdjustments(newAdjustments: ImageAdjustments) {
        undoStack.add(adjustments)
        if (undoStack.size > 20) undoStack.removeAt(0)
        redoStack.clear()
        adjustments = newAdjustments
    }

    Scaffold(
        containerColor = StudioDarkBg,
        topBar = {
            StudioTopBar(
                canUndo = undoStack.isNotEmpty(),
                canRedo = redoStack.isNotEmpty(),
                isCompareActive = isCompareActive,
                onUndoClick = {
                    if (undoStack.isNotEmpty()) {
                        val last = undoStack.removeLast()
                        redoStack.add(adjustments)
                        adjustments = last
                    }
                },
                onRedoClick = {
                    if (redoStack.isNotEmpty()) {
                        val next = redoStack.removeLast()
                        undoStack.add(adjustments)
                        adjustments = next
                    }
                },
                onCompareToggle = {
                    isCompareActive = !isCompareActive
                },
                onExportClick = {
                    showExportSheet = true
                }
            )
        },
        bottomBar = {
            StudioBottomNavigation(
                selectedTool = selectedTool,
                onSelectTool = { selectedTool = it }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Live Interactive Canvas Area (Upper part)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                CanvasWorkspace(
                    adjustments = adjustments,
                    layers = layers,
                    aspectRatio = selectedRatio,
                    rotationAngle = rotationAngle,
                    isFlippedH = isFlippedH,
                    isFlippedV = isFlippedV,
                    showGrid = showGrid,
                    isCompareActive = isCompareActive,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Lower Interactive Control Studio
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
                color = StudioSurface,
                tonalElevation = 6.dp
            ) {
                AnimatedContent(
                    targetState = selectedTool,
                    transitionSpec = {
                        fadeIn() togetherWith fadeOut()
                    },
                    label = "ToolPanelTransition"
                ) { tool ->
                    when (tool) {
                        StudioTool.ADJUST -> {
                            AdjustmentsPanel(
                                adjustments = adjustments,
                                onAdjustmentsChange = { updateAdjustments(it) },
                                onResetAll = {
                                    updateAdjustments(ImageAdjustments())
                                    selectedPresetId = null
                                }
                            )
                        }

                        StudioTool.FILTERS -> {
                            FilterPresetsPanel(
                                selectedPresetId = selectedPresetId,
                                onPresetSelect = { preset ->
                                    selectedPresetId = preset.id
                                    updateAdjustments(preset.adjustments)
                                },
                                onResetFilters = {
                                    selectedPresetId = null
                                    updateAdjustments(ImageAdjustments())
                                }
                            )
                        }

                        StudioTool.LAYERS -> {
                            LayersPanel(
                                layers = layers,
                                selectedLayerId = selectedLayerId,
                                onSelectLayer = { selectedLayerId = it },
                                onToggleVisibility = { id ->
                                    layers = layers.map { l ->
                                        if (l.id == id) l.copy(isVisible = !l.isVisible) else l
                                    }
                                },
                                onOpacityChange = { id, opacity ->
                                    layers = layers.map { l ->
                                        if (l.id == id) l.copy(opacity = opacity) else l
                                    }
                                },
                                onAddLayer = {
                                    val newId = "fx_${System.currentTimeMillis() % 1000}"
                                    val newLayer = StudioLayer(
                                        id = newId,
                                        name = "Cyber Glow FX #${layers.size + 1}",
                                        type = LayerType.NEBULA_AURORA,
                                        isVisible = true,
                                        opacity = 0.7f,
                                        blendModeName = "Screen"
                                    )
                                    layers = listOf(newLayer) + layers
                                    selectedLayerId = newId
                                }
                            )
                        }

                        StudioTool.TRANSFORM -> {
                            CropTransformPanel(
                                selectedRatio = selectedRatio,
                                onRatioSelected = { selectedRatio = it },
                                rotationAngle = rotationAngle,
                                onRotateClockwise = {
                                    rotationAngle = (rotationAngle + 90f) % 360f
                                },
                                onRotateCounterClockwise = {
                                    rotationAngle = (rotationAngle - 90f + 360f) % 360f
                                },
                                isFlippedH = isFlippedH,
                                isFlippedV = isFlippedV,
                                onToggleFlipH = { isFlippedH = !isFlippedH },
                                onToggleFlipV = { isFlippedV = !isFlippedV },
                                showGrid = showGrid,
                                onToggleGrid = { showGrid = !showGrid }
                            )
                        }

                        StudioTool.FX -> {
                            // Quick Creative FX panel
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text = "CREATIVE SYNTH FX",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        letterSpacing = 0.8.sp
                                    ),
                                    color = StudioTextSecondary
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            updateAdjustments(
                                                adjustments.copy(
                                                    brightness = 18f,
                                                    contrast = 40f,
                                                    saturation = 60f,
                                                    vignette = 45f
                                                )
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = StudioPrimary
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Neon Glow")
                                    }

                                    Button(
                                        onClick = {
                                            updateAdjustments(
                                                adjustments.copy(
                                                    brightness = -15f,
                                                    contrast = 65f,
                                                    saturation = -80f,
                                                    vignette = 70f
                                                )
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = StudioSurfaceElevated
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Retro Noir", color = StudioTextPrimary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Export Modal Bottom Sheet
        if (showExportSheet) {
            ExportBottomSheet(
                onDismissRequest = { showExportSheet = false }
            )
        }
    }
}
