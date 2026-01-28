import axios from 'axios'

const baseUrl = 'https://dragonsofmugloar.com/api/v2'

export default axios.create({
  baseURL: baseUrl,
})
