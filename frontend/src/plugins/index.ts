/**
 * plugins/index.ts
 *
 * Automatically included in `./src/main.ts`
 */

// Types
import type { App } from 'vue'

// Plugins
import vuetify from './vuetify'
import { createPinia } from 'pinia'
import router from '@/router'

export function registerPlugins (app: App) {
  app.use(createPinia())
  app.use(router)
  app.use(vuetify)
}
