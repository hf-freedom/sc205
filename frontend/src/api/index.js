import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

api.interceptors.response.use(
  response => response.data,
  error => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

export const vehicleApi = {
  entry: (data) => api.post('/vehicle/entry', data),
  exit: (data) => api.post('/vehicle/exit', data),
  abnormal: (data) => api.post('/vehicle/abnormal', data),
  getActive: () => api.get('/vehicle/active')
}

export const spaceApi = {
  list: () => api.get('/space/list'),
  get: (spaceId) => api.get(`/space/${spaceId}`),
  lock: (data) => api.post('/space/lock', data),
  unlock: (data) => api.post('/space/unlock', data),
  maintenance: (data) => api.post('/space/maintenance', data),
  releaseMaintenance: (data) => api.post('/space/release-maintenance', data)
}

export const monthlyCardApi = {
  create: (data) => api.post('/monthly-card/create', data),
  renew: (data) => api.post('/monthly-card/renew', data),
  cancel: (data) => api.post('/monthly-card/cancel', data),
  list: () => api.get('/monthly-card/list'),
  getByPlate: (plateNumber) => api.get(`/monthly-card/plate/${plateNumber}`)
}

export const reservationApi = {
  create: (data) => api.post('/reservation/create', data),
  cancel: (data) => api.post('/reservation/cancel', data),
  list: () => api.get('/reservation/list'),
  active: () => api.get('/reservation/active')
}

export const statisticsApi = {
  get: () => api.get('/statistics')
}

export const recordApi = {
  list: () => api.get('/record/list')
}

export const scanLogApi = {
  list: () => api.get('/scan-log/list'),
  byType: (scanType) => api.get(`/scan-log/type/${scanType}`),
  recent: (limit) => api.get(`/scan-log/recent/${limit}`)
}
