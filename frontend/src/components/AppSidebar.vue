<template>
  <v-navigation-drawer
    :model-value="display.mdAndUp.value || modelValue"
    :rail="display.sm.value"
    :permanent="display.mdAndUp.value"
    :temporary="display.xs.value"
    width="240"
    :theme="theme"
    @update:model-value="emit('update:modelValue', $event)"
  >
    <div style="height: 100%; display: flex; flex-direction: column">
      <!-- Logo -->
      <div class="px-3 pt-4 pb-3">
        <v-row no-gutters align="center">
          <v-col cols="auto" class="me-3">
            <div class="logo-icon d-flex align-center justify-center">
              <v-icon color="white" size="30">mdi-note-text</v-icon>
            </div>
          </v-col>
          <v-col>
            <div style="font-size: 15px; font-weight: 700; line-height: 1.3">
              Smart Notes
            </div>
            <div style="font-size: 11px; color: #94a3b8; line-height: 1.3">
              Capture your ideas
            </div>
          </v-col>
        </v-row>
      </div>

      <!-- Navigation -->
      <v-list
        nav
        density="compact"
        class="px-2"
        style="flex: 1; overflow-y: auto"
      >
        <v-list-item
          prepend-icon="mdi-note-multiple-outline"
          title="All Notes"
          :active="activeCategory === null"
          active-color="primary"
          rounded="lg"
          class="mb-1"
          @click="emit('select', null)"
        />

        <v-divider class="my-2" />

        <v-list-item
          v-for="cat in categories"
          :key="cat.value"
          :title="cat.label"
          :active="activeCategory === cat.value"
          active-color="primary"
          rounded="lg"
          class="mb-1"
          @click="emit('select', cat.value)"
        >
          <template #prepend>
            <v-icon :color="cat.color" size="20" class="me-0">{{
              cat.icon
            }}</v-icon>
          </template>
        </v-list-item>
      </v-list>

      <!-- Bottom: profile + logout — pinned -->
      <div style="margin-top: auto">
        <v-divider />
        <!-- Rail mode (tablet): icon-only logout -->
        <div v-if="display.sm.value" class="d-flex justify-center py-3">
          <v-btn icon="mdi-logout" variant="text" color="primary" @click="logout" />
        </div>
        <!-- Full mode (desktop): avatar + name + logout button -->
        <div v-else class="px-3 pt-3 pb-2">
          <v-row no-gutters align="center" class="mb-3">
            <v-col cols="auto" class="mr-1">
              <v-icon size="38" color="primary">mdi-account-circle</v-icon>
            </v-col>
            <v-col class="overflow-hidden pb-1">
              <div class="text-sm text-body-1 font-weight-medium text-truncate username">
                {{ authStore.username ?? "User" }}
              </div>
              <div class="text-xs text-caption text-medium-emphasis text-truncate text-gray-500">
                {{ authStore.email ?? "" }}
              </div>
            </v-col>
          </v-row>
          <v-btn
            prepend-icon="mdi-logout"
            variant="tonal"
            color="primary"
            size="default"
            block
            rounded="lg"
            @click="logout"
          >
            Logout
          </v-btn>
        </div>
      </div>
    </div>
  </v-navigation-drawer>
</template>

<script lang="ts" setup>
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";
import type { CategoryType } from "@/stores/notes";
import { useDisplay } from "vuetify";

defineProps<{ activeCategory: CategoryType | null; theme: string; modelValue?: boolean }>();
const emit = defineEmits<{
  (e: "select", category: CategoryType | null): void;
  (e: "update:modelValue", value: boolean): void;
}>();

const display = useDisplay();

const authStore = useAuthStore();
const router = useRouter();

const categories = [
  {
    value: "HEALTH" as CategoryType,
    label: "Health",
    icon: "mdi-heart-pulse",
    color: "#dc2626",
  },
  {
    value: "TASK" as CategoryType,
    label: "Task",
    icon: "mdi-checkbox-marked-circle-outline",
    color: "#7c3aed",
  },
  {
    value: "COOKING" as CategoryType,
    label: "Cooking",
    icon: "mdi-silverware-fork-knife",
    color: "#ea580c",
  },
  {
    value: "SHOPPING" as CategoryType,
    label: "Shopping",
    icon: "mdi-cart-outline",
    color: "#2563eb",
  },
  {
    value: "SPORT" as CategoryType,
    label: "Sport",
    icon: "mdi-dumbbell",
    color: "#16a34a",
  },
  {
    value: "UNCATEGORIZED" as CategoryType,
    label: "Uncategorized",
    icon: "mdi-folder-outline",
    color: "#6b7280",
  },
];

function logout() {
  authStore.logout();
  router.push("/login");
}
</script>

<style scoped>
.logo-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea, #764ba2);
}
.username {
  text-transform: capitalize;
}
:deep(.v-list-item-title) {
  font-size: 14px !important;
}
.avatar-circle {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  flex-shrink: 0;
  font-size: 12px;
}
</style>
