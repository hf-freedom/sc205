import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import VehicleManagement from '../views/VehicleManagement.vue'
import SpaceManagement from '../views/SpaceManagement.vue'
import MonthlyCardManagement from '../views/MonthlyCardManagement.vue'
import ReservationManagement from '../views/ReservationManagement.vue'
import Statistics from '../views/Statistics.vue'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/vehicle',
    name: 'VehicleManagement',
    component: VehicleManagement
  },
  {
    path: '/space',
    name: 'SpaceManagement',
    component: SpaceManagement
  },
  {
    path: '/monthly-card',
    name: 'MonthlyCardManagement',
    component: MonthlyCardManagement
  },
  {
    path: '/reservation',
    name: 'ReservationManagement',
    component: ReservationManagement
  },
  {
    path: '/statistics',
    name: 'Statistics',
    component: Statistics
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
