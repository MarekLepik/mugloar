import { reactive } from 'vue'
import type { GameState } from '../types/GameState.ts'
import type { Game } from '../types/Game.ts'
import type { SolvedTask } from '../types/SolvedTask.ts'
import type { BuyItemResponse } from '../types/BuyItemResponse.ts'

export const gameState: GameState = reactive({
  gameId: null,
  gold: 0,
  lives: 3,
  score: 0,
  highScore: 0,
  level: 1,
  turn: 1,
})

export function updateStateFromGameResponse(game: Game): void {
  gameState.gameId = game.gameId
  gameState.gold = game.gold
  gameState.lives = game.lives
  gameState.score = game.score
  gameState.highScore = game.highScore
  gameState.level = game.level
  gameState.turn = game.turn
}

export function updateStateFromSolvedTaskResponse(solvedTask: SolvedTask): void {
  gameState.lives = solvedTask.lives
  gameState.gold = solvedTask.gold
  gameState.score = solvedTask.score
  gameState.highScore = solvedTask.highScore
  gameState.turn = solvedTask.turn
}

export function updateStateFromBuyItemResponse(buyItemResponse: BuyItemResponse): void {
  gameState.lives = buyItemResponse.lives
  gameState.gold = buyItemResponse.gold
  gameState.level = buyItemResponse.level
  gameState.turn = buyItemResponse.turn
}

export function resetGameState(): void {
  gameState.gameId = null
  gameState.gold = 0
  gameState.lives = 3
  gameState.score = 0
  gameState.highScore = 0
  gameState.level = 1
  gameState.turn = 1
}
