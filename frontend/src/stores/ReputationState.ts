import { reactive } from 'vue'
import type { Reputation } from '../types/Reputation.ts'

export const reputation: Reputation = reactive({
  people: 0,
  state: 0,
  underworld: 0,
})

export function updateReputation(newReputation: Reputation): void {
  reputation.people = parseFloat(Number(newReputation.people).toFixed(2))
  reputation.state = parseFloat(Number(newReputation.state).toFixed(2))
  reputation.underworld = parseFloat(Number(newReputation.underworld).toFixed(2))
}

export function resetReputation(): void {
  reputation.people = 0
  reputation.state = 0
  reputation.underworld = 0
}
