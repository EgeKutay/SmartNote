import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: () => import('@/pages/LoginPage.vue') },
    { path: '/register', component: () => import('@/pages/RegisterPage.vue') },
    { path: '/', component: () => import('@/pages/HomePage.vue'), meta: { requiresAuth: true } },
  ],
})

router.beforeEach((to, _, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) return next('/login')
  if ((to.path === '/login' || to.path === '/register') && token) return next('/')
  next()
})

export default router
