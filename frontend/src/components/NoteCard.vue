<template>
  <v-card elevation="0" class="note-card" @click="emit('click', note)">
    <v-card-text style="padding: 20px">
      <!-- Top row: icon badge (col auto) + spacer + three-dot menu (col auto) -->
      <v-row no-gutters align="start" class="mb-3">
        <v-col cols="auto">
          <div class="icon-badge" :style="{ background: badgeGradient }">
            <v-icon color="white" size="20">{{ categoryIcon }}</v-icon>
          </div>
        </v-col>
        <v-spacer />
        <v-col cols="auto">
          <v-menu location="bottom end">
            <template #activator="{ props: menuProps }">
              <v-btn
                v-bind="menuProps"
                icon
                size="x-small"
                variant="text"
                @click.stop
              >
                <v-icon size="18">mdi-dots-vertical</v-icon>
              </v-btn>
            </template>
            <v-list density="compact" rounded="lg" min-width="140">
              <v-list-item
                prepend-icon="mdi-pencil-outline"
                title="Edit"
                @click.stop="emit('edit', note)"
              />
              <v-list-item
                prepend-icon="mdi-delete-outline"
                title="Delete"
                base-color="error"
                @click.stop="emit('delete', note)"
              />
            </v-list>
          </v-menu>
        </v-col>
      </v-row>

      <!-- Title -->
      <div class="note-title mb-3 text-truncate">
        {{
          note.title ||
          note.category.charAt(0) + note.category.slice(1).toLowerCase()
        }}
      </div>

      <!-- Content preview -->
      <div
        class="note-content text-medium-emphasis content-preview mb-3 text-gray-600"
      >
        {{ note.content }}
      </div>

      <!-- Footer -->
      <v-divider class="mb-2" />
      <span
        class="note-date text-medium-emphasis d-flex align-center px-2 gap-1 text-gray-500"
      >
        <v-icon size="14">mdi-calendar-outline</v-icon>
        {{ formatDate(note.modifiedDate) }}
      </span>
    </v-card-text>
  </v-card>
</template>

<script lang="ts" setup>
import { computed } from "vue";
import type { Note } from "@/stores/notes";

const props = defineProps<{ note: Note }>();
const emit = defineEmits<{
  (e: "click", note: Note): void;
  (e: "edit", note: Note): void;
  (e: "delete", note: Note): void;
}>();

const categoryMeta: Record<string, { gradient: string; icon: string }> = {
  TASK: {
    gradient: "linear-gradient(135deg,#7c3aed,#a855f7)",
    icon: "mdi-checkbox-marked-circle-outline",
  },
  SHOPPING: {
    gradient: "linear-gradient(135deg,#2563eb,#60a5fa)",
    icon: "mdi-cart-outline",
  },
  COOKING: {
    gradient: "linear-gradient(135deg,#ea580c,#fb923c)",
    icon: "mdi-silverware-fork-knife",
  },
  SPORT: {
    gradient: "linear-gradient(135deg,#16a34a,#4ade80)",
    icon: "mdi-dumbbell",
  },
  HEALTH: {
    gradient: "linear-gradient(135deg,#dc2626,#f87171)",
    icon: "mdi-heart-pulse",
  },
  UNCATEGORIZED: {
    gradient: "linear-gradient(135deg,#6b7280,#9ca3af)",
    icon: "mdi-folder-outline",
  },
};

const badgeGradient = computed(
  () =>
    categoryMeta[props.note.category]?.gradient ??
    categoryMeta.UNCATEGORIZED.gradient,
);

const categoryIcon = computed(
  () => categoryMeta[props.note.category]?.icon ?? "mdi-folder-outline",
);

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleDateString(undefined, {
    month: "short",
    day: "numeric",
    year: "numeric",
  });
}
</script>

<style scoped>
.note-card {
  cursor: pointer;
  transition:
    transform 0.15s,
    box-shadow 0.15s;
  border: 0px solid rgba(0, 0, 0, 0.07) !important;
  border-radius: 20px !important;
  background: #ffffff !important;
}
.note-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1) !important;
}
.icon-badge {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.note-title {
  font-size: 16px;
  font-weight: 700;
  line-height: 1.4;
}
.note-content {
  font-size: 14px;
  line-height: 1.6;
}
.note-date {
  font-size: 13px;
}
.content-preview {
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  min-height: 4.8em;
}
</style>
