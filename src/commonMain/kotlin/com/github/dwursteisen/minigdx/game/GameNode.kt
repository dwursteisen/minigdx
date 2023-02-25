package com.github.dwursteisen.minigdx.game

import com.github.dwursteisen.minigdx.GameContext
import com.github.dwursteisen.minigdx.ecs.Engine
import com.github.dwursteisen.minigdx.graphics.FrameBuffer
import com.github.dwursteisen.minigdx.render.ClearBufferRenderStage

internal class GameNode(
    var name: String,
    var game: Game,
    var engine: Engine,
    var parent: GameNode? = null,
    var children: List<GameNode> = emptyList()
) {
    fun bootstrap(gameContext: GameContext) = gameContext.postRenderLoop {

        val defaultSystems = game.createDefaultSystems(engine)
        defaultSystems.forEach { engine.addSystem(it) }
        gameContext.logger.debug("BOOTSTRAP") {
            "Created ${defaultSystems.size} default systems"
        }

        val gameSystems = game.createSystems(engine)
        gameSystems.forEach { engine.addSystem(it) }
        gameContext.logger.debug("BOOTSTRAP") {
            "Created ${gameSystems.size} game systems"
        }

        val postRenderSystem = game.createPostRenderSystem(engine)
        postRenderSystem.forEach { engine.addSystem(it) }
        gameContext.logger.debug("BOOTSTRAP") {
            "Created ${postRenderSystem.size} post render systems"
        }

        // FIXME: code cleanup
        fun traverse(frameBuffer: FrameBuffer): List<FrameBuffer> {
            return frameBuffer.dependencies.flatMap { traverse(it) } + frameBuffer
        }

        val frameBuffers = game.createFrameBuffers(gameContext)
        // Keep the frame buffers into the context
        gameContext.frameBuffers = frameBuffers
            .flatMap { traverse(it) }
            .associateBy { buffer -> buffer.name }

        gameContext.logger.debug("BOOTSTRAP") {
            "Created ${gameContext.frameBuffers.size} framebuffers " +
                "(named: ${gameContext.frameBuffers.keys.joinToString()})"
        }

        // Check if there is one frame buffer that will render on screen
        // And therefore, will take the lead on the render stage.
        val rootFrameBuffers = frameBuffers.filter { frameBuffer -> frameBuffer.renderOnScreen }

        val renderStage = when (rootFrameBuffers.size) {
            0 -> game.createRenderStage()
            1 -> emptyList()
            else -> throw IllegalStateException(
                "Only one frame buffer can be render directly on screen." +
                    "Please configure Frame buffers so only one will render on screen by setting" +
                    " to false the property of one of those frame buffer: " +
                    "${rootFrameBuffers.joinToString { it.name }} "
            )
        }

        // When there is one frame buffer to be render on the screen, we add a clear stage before it
        // So the screen is clean.
        // It can be disable be removing the clear color of the game.
        if (rootFrameBuffers.size == 1) {
            game.clearColor?.run { engine.addSystem(ClearBufferRenderStage(gameContext, this)) }
        }

        frameBuffers.forEach { engine.addSystem(it) }

        val debugRenderStage = game.createDebugRenderStage(gameContext.options)

        (renderStage + debugRenderStage).forEach { engine.addSystem(it) }

        game.createEntities(engine.entityFactory)
        // Load assets that can be loaded
        gameContext.assetsManager.update()

        gameContext.logger.debug("BOOTSTRAP") {
            "Compiling ${renderStage.size + debugRenderStage.size + frameBuffers.size} shaders"
        }

        renderStage.forEach { it.compileShaders() }
        debugRenderStage.forEach { it.compileShaders() }
        frameBuffers.forEach { it.compileShaders() }

        gameContext.logger.debug("BOOTSTRAP") {
            "Bootstrap sequence finished. Will start the game by calling onGameStart"
        }

        engine.onGameStart()
    }
}
