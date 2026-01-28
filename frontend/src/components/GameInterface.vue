<template>
  <div class="min-h-screen bg-gray-900 text-gray-100 p-6">
    <div class="bg-gray-800 rounded-2xl p-4 m-6 shadow">
      <div class="">
        <div
          class="flex flex-col gap-10 lg:flex-row items-center lg:justify-around text-sm mx-auto sm:grid-cols-4 lg:flex lg:gap-6"
        >
          <div class="flex justify-start">
            <button
              class="w-full px-6 py-2 rounded-xl font-semibold cursor-pointer max-h-15 m-auto max-w-40"
              :class="
                gameState.gameId ? 'bg-red-600 hover:bg-red-700' : 'bg-green-600 hover:bg-green-700'
              "
              @click="startGame"
            >
              {{ gameState.gameId ? 'Restart Game' : 'Start Game' }}
            </button>
          </div>
          <div>
            <h3 class="text-xs w-full text-center">Game state</h3>
            <div class="grid grid-cols-3 xl:grid-cols-6 gap-3">
              <Stat label="Score" :value="gameState.score" />
              <Stat label="Gold" :value="gameState.gold" />
              <Stat label="Lives" :value="gameState.lives" />
              <Stat label="Turn" :value="gameState.turn" />
              <Stat label="Level" :value="gameState.level" />
              <Stat label="High Score" :value="gameState.highScore" />
            </div>
          </div>
          <div>
            <h3 class="text-xs text-center p-2">Reputation</h3>
            <div class="grid grid-cols-3 gap-3">
              <Stat label="People" :value="reputation.people" />
              <Stat label="State" :value="reputation.state" />
              <Stat label="Underworld" :value="reputation.underworld" />
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="!gameState.gameId" class="text-center text-gray-400 flex-row">
      Start the game to load tasks, shop, and inventory.
    </div>

    <div v-else class="grid grid-cols-1 lg:grid-cols-5 gap-6">
      <GameTable
        title="Tasks"
        :columns="['ID', 'Message', 'ExpiresIn', 'Reward', 'Probability', 'Action']"
        class="col-span-3"
      >
        <tr v-for="task in tasks" :key="task.adId" class="hover:bg-gray-700">
          <td :class="cellClass">{{ task.adId }}</td>
          <td :class="cellClass">{{ task.message }}</td>
          <td :class="cellClass">{{ task.expiresIn }}</td>
          <td :class="cellClass">{{ task.reward }}</td>
          <td :class="cellClass">{{ task.probability }}</td>
          <td :class="cellClass">
            <button
              class="px-3 py-1 bg-indigo-600 hover:bg-indigo-700 rounded-lg cursor-pointer disabled:bg-gray-600 disabled:cursor-not-allowed"
              @click="solveTask(task.adId)"
              :disabled="isSolving"
            >
              Complete
            </button>
          </td>
        </tr>
      </GameTable>

      <GameTable title="Shop" :columns="['ID', 'Name', 'Cost', 'Action']" class="col-span-2">
        <tr v-for="item in shop" :key="item.id" class="hover:bg-gray-700">
          <td :class="cellClass">{{ item.id }}</td>
          <td :class="cellClass">{{ item.name }}</td>
          <td :class="cellClass">{{ item.cost }}</td>
          <td :class="cellClass">
            <button
              class="px-3 py-1 bg-yellow-600 hover:bg-yellow-700 rounded-lg disabled:bg-gray-600 disabled:cursor-not-allowed cursor-pointer"
              @click="buyItem(item.id)"
              :disabled="gameState.gold < item.cost || isSolving || isBuying"
            >
              Buy
            </button>
          </td>
        </tr>
      </GameTable>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Ref } from 'vue'
import { ref } from 'vue'
import GameTable from './GameTable.vue'
import Stat from './Stat.vue'
import type { Task } from '@/types/Task.ts'
import type { Item } from '@/types/Item.ts'
import { gameApiClient } from '@/repositories/GameApiClient.ts'
import { toasts } from '@/toasts/Toasts.ts'
import {
  gameState,
  resetGameState,
  updateStateFromBuyItemResponse,
  updateStateFromGameResponse,
  updateStateFromSolvedTaskResponse,
} from '@/stores/GameState.ts'
import { reputation, resetReputation, updateReputation } from '@/stores/ReputationState.ts'

const tasks: Ref<Task[]> = ref([])
const shop: Ref<Item[]> = ref([])
const blacklistedTaskIds: Ref<string[]> = ref([])
const cellClass: string =
  'px-4 py-2 border-b border-gray-700 text-sm p-2 max-w-lg break-words overflow-scroll'
const isSolving: Ref<boolean> = ref(false)
const isBuying: Ref<boolean> = ref(false)

const gameApi = gameApiClient

async function startGame() {
  if (gameState.gameId) {
    resetGameState()
    await startGame()
    toasts.info('Game restarted.')
    return
  }
  try {
    const res = await gameApi.startGame()
    updateStateFromGameResponse(res.data)
    toasts.info('Game started.')
    await refreshState()
  } catch (error) {
    toasts.error('Error starting game.')
  }
}

async function refreshState() {
  if (!gameState.gameId) {
    toasts.error('No active game. Please start a game first.')
    resetGameState()
    return
  }

  if (gameState.lives < 1) {
    toasts.error('Game over. Please start a new game.')
    resetGameState()
    return
  }
  await Promise.all([getTasks(), getShop(), getReputation()])
}

async function getShop() {
  if (!gameState.gameId) {
    toasts.error('No active game. Please start a game first.')
    shop.value = []
    resetGameState()
    return
  }
  try {
    const res = await gameApi.fetchShop(gameState.gameId)
    shop.value = res.data
  } catch (error) {
    toasts.error('Failed to load shop items.')
    shop.value = []
  }
}

async function getTasks() {
  if (!gameState.gameId) {
    toasts.error('No active game. Please start a game first.')
    tasks.value = []
    resetGameState()
    return
  }

  try {
    const res = await gameApi.fetchTasks(gameState.gameId)
    tasks.value = res.data.filter((task) => !blacklistedTaskIds.value.includes(task.adId))
  } catch (error) {
    toasts.error('Failed to load tasks.')
    tasks.value = []
  }
}

async function getReputation() {
  if (!gameState.gameId) {
    toasts.error('No active game. Please start a game first.')
    resetGameState()
    return
  }
  try {
    const res = await gameApi.fetchReputation(gameState.gameId)
    updateReputation(res.data)
  } catch (error) {
    toasts.error('Failed to load reputation.')
    resetReputation()
    return
  }
}

async function solveTask(taskId: string) {
  if (!gameState.gameId) {
    toasts.error('No active game. Please start a game first.')
    resetGameState()
    return
  }
  try {
    isSolving.value = true
    const res = await gameApi.solveTask(gameState.gameId, taskId)
    if (res.data.success) {
      toasts.success('Task completed successfully.')
    } else {
      toasts.error('Task failed.')
    }

    updateStateFromSolvedTaskResponse(res.data)
    await refreshState()
  } catch (error) {
    toasts.error('Error solving task.')
    blacklistedTaskIds.value.push(taskId)
    await refreshState()
  }
  isSolving.value = false
}

async function buyItem(itemId: string) {
  if (!gameState.gameId) {
    toasts.error('No active game. Please start a game first.')
    resetGameState()
    return
  }
  isBuying.value = true
  try {
    const res = await gameApi.buyItem(gameState.gameId, itemId)
    updateStateFromBuyItemResponse(res.data)
    if (res.data.shoppingSuccess) {
      toasts.success('Item purchased successfully.')
    } else {
      toasts.error('Item purchase failed.')
    }
  } catch (error) {
    toasts.error('Error purchasing item.')
  }

  isBuying.value = false
  await refreshState()
}
</script>
