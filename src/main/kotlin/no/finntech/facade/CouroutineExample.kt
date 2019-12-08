package no.finntech.facade

import kotlinx.coroutines.*

class CouroutineExample {
    companion object {
        private const val ADVENTURER_COUNT = 4000
        private const val QUEST_REWARD_SIZE = 100
        private var treasureCount = 0
        private const val TREASURE_THREAD_CONTEXT_NAME = "TreasureContext"
        @ObsoleteCoroutinesApi
        private var treasureContext: ExecutorCoroutineDispatcher
                = newSingleThreadContext(TREASURE_THREAD_CONTEXT_NAME)
    }

    private suspend fun dispatchAdventurers(
            adventurerCount: Int,
            quest: suspend (Int) -> Unit
    ) {
        coroutineScope {
            repeat(adventurerCount) {
                launch {
                    quest(QUEST_REWARD_SIZE)
                }
            }
        }
    }

    fun main() {
        runBlocking {
            withContext(treasureContext) {
                dispatchAdventurers(ADVENTURER_COUNT) { bounty ->
                        treasureCount += bounty
                }
            }
            println("Total haul: \$$treasureCount")
        }
    }

}

fun main() {
    CouroutineExample().main()
}