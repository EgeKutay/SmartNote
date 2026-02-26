import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '@/api/axios'

export type CategoryType = 'SHOPPING' | 'HEALTH' | 'SPORT' | 'TASK' | 'COOKING' | 'UNCATEGORIZED'

export interface Note {
  id: number
  title: string | null
  content: string
  category: CategoryType
  createdDate: string
  modifiedDate: string
}

export const useNotesStore = defineStore('notes', () => {
  const notes = ref<Note[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function fetchNotes(search?: string, category?: CategoryType | null) {
    loading.value = true
    error.value = null
    try {
      const params: Record<string, string> = {}
      if (search && search.trim()) params.search = search.trim()
      if (category) params.category = category
      const res = await api.get('/notes', { params })
      notes.value = res.data
    } catch (e: any) {
      error.value = 'Failed to load notes.'
    } finally {
      loading.value = false
    }
  }

  async function createNote(title: string, content: string) {
    const res = await api.post('/notes', { title: title || null, content })
    notes.value.unshift(res.data)
    return res.data as Note
  }

  async function updateNote(id: number, title: string, content: string) {
    const res = await api.put(`/notes/${id}`, { title: title || null, content })
    const idx = notes.value.findIndex(n => n.id === id)
    if (idx !== -1) notes.value[idx] = res.data
    return res.data as Note
  }

  async function deleteNote(id: number) {
    await api.delete(`/notes/${id}`)
    notes.value = notes.value.filter(n => n.id !== id)
  }

  return { notes, loading, error, fetchNotes, createNote, updateNote, deleteNote }
})
