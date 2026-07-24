package com.inappstory.sdk.compose.iasapi

import android.content.Context
import com.inappstory.sdk.InAppStoryManager
import com.inappstory.sdk.core.IASCore
import com.inappstory.sdk.core.UseIASCoreCallback
import com.inappstory.sdk.stories.outercallbacks.common.gamereader.GameReaderCallback
import com.inappstory.sdk.stories.outercallbacks.common.reader.ContentData


class IASGames {
    fun close() {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.gamesAPI().close()
            }
        })
    }

    fun open(context: Context, gameId: String) {
        InAppStoryManager.useCoreInSeparateThread(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.gamesAPI().open(context, gameId)
            }
        })
    }

    fun callback(
        startGame: (
            gameLaunchSourceData: ContentData?,
            gameId: String?
        ) -> Unit,
        closeGame: (
            gameLaunchSourceData: ContentData?,
            gameId: String?
        ) -> Unit,
        eventGame: (
            gameLaunchSourceData: ContentData?,
            gameId: String?,
            eventName: String?,
            payload: String?
        ) -> Unit,
        gameLoadError: (
            gameLaunchSourceData: ContentData?,
            gameId: String?
        ) -> Unit,
        gameOpenError: (
            gameLaunchSourceData: ContentData?,
            gameId: String?
        ) -> Unit
    ) {
        InAppStoryManager.useCore(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.gamesAPI().callback(object : GameReaderCallback {
                    override fun startGame(
                        gameLaunchSourceData: ContentData?,
                        gameId: String?
                    ) {
                        startGame.invoke(gameLaunchSourceData, gameId)
                    }

                    override fun closeGame(
                        gameLaunchSourceData: ContentData?,
                        gameId: String?
                    ) {
                        closeGame.invoke(gameLaunchSourceData, gameId)
                    }

                    override fun eventGame(
                        gameLaunchSourceData: ContentData?,
                        gameId: String?,
                        eventName: String?,
                        payload: String?
                    ) {
                        eventGame.invoke(
                            gameLaunchSourceData,
                            gameId,
                            eventName,
                            payload
                        )
                    }

                    override fun gameLoadError(
                        gameLaunchSourceData: ContentData?,
                        gameId: String?
                    ) {
                        gameLoadError.invoke(gameLaunchSourceData, gameId)
                    }

                    override fun gameOpenError(
                        gameLaunchSourceData: ContentData?,
                        gameId: String?
                    ) {
                        gameOpenError.invoke(gameLaunchSourceData, gameId)
                    }
                })
            }
        })
    }

    fun preloadGames() {
        InAppStoryManager.useCoreInSeparateThread(object : UseIASCoreCallback() {
            override fun use(core: IASCore) {
                core.contentPreload().restartGamePreloader()
            }
        })
    }
}