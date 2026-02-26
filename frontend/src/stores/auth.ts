import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '@/api/axios'

export const useAuthStore = defineStore('auth', () => {
  const token    = ref<string | null>(localStorage.getItem('token'))
  const userId   = ref<number | null>(Number(localStorage.getItem('userId')) || null)
  const username = ref<string | null>(localStorage.getItem('username'))
  const email    = ref<string | null>(localStorage.getItem('email'))
  const error    = ref<string | null>(null)
  const loading  = ref(false)

  async function login(inputEmail: string, password: string) {
    loading.value = true
    error.value = null
    try {
      const res = await api.post('/auth/login', { email: inputEmail, password })
      token.value    = res.data.token
      userId.value   = res.data.userId
      username.value = res.data.username
      email.value    = res.data.email
      localStorage.setItem('token',    res.data.token)
      localStorage.setItem('userId',   res.data.userId)
      localStorage.setItem('username', res.data.username)
      localStorage.setItem('email',    res.data.email)
      return true
    } catch (e: any) {
      error.value = e.response?.data?.message ?? 'Invalid email or password.'
      return false
    } finally {
      loading.value = false
    }
  }

  async function register(inputUsername: string, inputEmail: string, password: string) {
    loading.value = true
    error.value = null
    try {
      const res = await api.post('/auth/register', { username: inputUsername, email: inputEmail, password })
      token.value    = res.data.token
      userId.value   = res.data.userId
      username.value = res.data.username
      email.value    = res.data.email
      localStorage.setItem('token',    res.data.token)
      localStorage.setItem('userId',   res.data.userId)
      localStorage.setItem('username', res.data.username)
      localStorage.setItem('email',    res.data.email)
      return true
    } catch (e: any) {
      error.value = e.response?.data?.message ?? 'Registration failed.'
      return false
    } finally {
      loading.value = false
    }
  }

  function logout() {
    token.value    = null
    userId.value   = null
    username.value = null
    email.value    = null
    localStorage.removeItem('token')
    localStorage.removeItem('userId')
    localStorage.removeItem('username')
    localStorage.removeItem('email')
  }

  return { token, userId, username, email, error, loading, login, register, logout }
})
