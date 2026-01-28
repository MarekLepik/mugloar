import Repository from './Repository.ts'
import type { AxiosResponse } from 'axios'
import type { Game } from '../types/Game.ts'
import type { Task } from '../types/Task.ts'
import type { SolvedTask } from '../types/SolvedTask.ts'
import type { Item } from '../types/Item.ts'
import type { BuyItemResponse } from '../types/BuyItemResponse.ts'
import type { Reputation } from '../types/Reputation.ts'

export const gameApiClient = {
  startGame(): Promise<AxiosResponse<Game>> {
    return Repository.post('/game/start')
  },
  fetchTasks(gameId: string): Promise<AxiosResponse<Task[]>> {
    if (!gameId) {
      return Promise.reject(new Error('Invalid gameId'))
    }
    return Repository.get(`${gameId}/messages`)
  },
  fetchShop(gameId: string): Promise<AxiosResponse<Item[]>> {
    if (!gameId) {
      return Promise.reject(new Error('Invalid gameId'))
    }
    return Repository.get(`${gameId}/shop`)
  },
  solveTask(gameId: string, adId: string): Promise<AxiosResponse<SolvedTask>> {
    if (!gameId) {
      return Promise.reject(new Error('Invalid gameId'))
    }
    return Repository.post(`${gameId}/solve/${adId}`)
  },
  buyItem(gameId: string, itemId: string): Promise<AxiosResponse<BuyItemResponse>> {
    if (!gameId || !itemId) {
      return Promise.reject(new Error('Invalid gameId'))
    }
    return Repository.post(`${gameId}/shop/buy/${itemId}`)
  },
  fetchReputation(gameId: string): Promise<AxiosResponse<Reputation>> {
    if (!gameId) {
      return Promise.reject(new Error('Invalid gameId'))
    }
    return Repository.post(`${gameId}/investigate/reputation`)
  },
}
