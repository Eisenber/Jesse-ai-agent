import { createRouter, createWebHistory } from 'vue-router'

import HomePage from '../pages/HomePage.vue'
import LoveAppPage from '../pages/LoveAppPage.vue'
import ManusPage from '../pages/ManusPage.vue'

const routes = [
  { path: '/', name: 'home', component: HomePage },
  { path: '/love', name: 'love', component: LoveAppPage },
  { path: '/manus', name: 'manus', component: ManusPage }
]

export default createRouter({
  history: createWebHistory(),
  routes
})

