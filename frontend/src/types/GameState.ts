import type { Game } from '@/types/Game.ts'

export interface GameState {
  gameId: string | null
  lives: number
  gold: number
  level: number
  score: number
  highScore: number
  turn: number
}
