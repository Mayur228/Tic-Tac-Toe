package com.demo.tictactoe.core.feature.game.data.source

import com.demo.tictactoe.core.Resource
import com.demo.tictactoe.core.common.model.BluetoothConnectionState
import com.demo.tictactoe.core.common.model.DataModel
import com.demo.tictactoe.core.common.model.DeviceModel
import com.demo.tictactoe.core.common.network.BluetoothApi
import com.demo.tictactoe.core.feature.game.data.model.GameDifficulty
import com.demo.tictactoe.core.feature.game.data.model.GameResult
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface GameSource {
    suspend fun sendMove(move: Int): Resource<DataModel>
    suspend fun observeMoves(): Flow<Int>
    suspend fun connectionState(): Flow<BluetoothConnectionState>
    suspend fun disconnect(): Resource<Unit>
    suspend fun evaluateBoard(board: List<String>): Resource<GameResult>
    suspend fun aiMoves(gameResult: GameResult,board: List<String>,difficulty: GameDifficulty): Resource<Int>
}

@Factory
class GameSourceImpl(
    private val bluetoothApi: BluetoothApi
) : GameSource {

    override suspend fun sendMove(move: Int): Resource<DataModel> {
        return try {
            val result = bluetoothApi.sendMove(move)
            Resource.Data(result)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun observeMoves(): Flow<Int> =
        bluetoothApi.incomingMoves

    override suspend fun connectionState(): Flow<BluetoothConnectionState> =
        bluetoothApi.connectionState

    override suspend fun disconnect(): Resource<Unit> {
        return try {
            val result = bluetoothApi.disconnect()
            Resource.Data(result)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun evaluateBoard(
        board: List<String>
    ): Resource<GameResult> {
        return try {

            val wins = listOf(
                listOf(0,1,2),
                listOf(3,4,5),
                listOf(6,7,8),
                listOf(0,3,6),
                listOf(1,4,7),
                listOf(2,5,8),
                listOf(0,4,8),
                listOf(2,4,6)
            )

            for (line in wins) {
                val (a, b, c) = line

                if (board[a].isNotEmpty() &&
                    board[a] == board[b] &&
                    board[b] == board[c]
                ) {
                    return Resource.Data(
                        GameResult.Win(
                            winner = board[a],
                            winningLine = line
                        )
                    )
                }
            }

            if (board.all { it.isNotEmpty() }) {
                return Resource.Data(GameResult.Draw)
            }

            return Resource.Data(GameResult.Ongoing)

        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun aiMoves(gameResult: GameResult,board: List<String>,difficulty: GameDifficulty): Resource<Int> {
        return try {
            val data = when(difficulty){
                GameDifficulty.EASY -> {0}
                GameDifficulty.MEDIUM -> {0}
                GameDifficulty.HARD -> {0}
            }
            Resource.Data(data)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }
}

