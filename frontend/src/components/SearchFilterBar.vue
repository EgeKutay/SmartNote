<template>
  <div class="d-flex align-center gap-3 w-100">
    <!-- Search -->
    <v-text-field
      v-model="searchModel"
      placeholder="Search notes..."
      prepend-inner-icon="mdi-magnify"
      variant="outlined"
      density="compact"
      hide-details
      rounded="lg"
      bg-color="surface"
      style="max-width: 360px"
      @update:model-value="emit('update:search', $event ?? '')"
    />

    <v-spacer />

    <!-- Sort -->
    <v-select
      v-model="sortModel"
      :items="sortOptions"
      item-title="label"
      item-value="value"
      variant="outlined"
      density="compact"
      hide-details
      rounded="lg"
      bg-color="surface"
      style="max-width: 180px"
      @update:model-value="emit('update:sort', $event)"
    >
      <template #prepend-inner>
        <v-icon size="16" class="mr-1">mdi-sort-calendar-descending</v-icon>
      </template>
    </v-select>
  </div>
</template>

<script lang="ts" setup>
import { ref, watch } from "vue";

const props = defineProps<{ search: string; sort: string }>();
const emit = defineEmits<{
  (e: "update:search", v: string): void;
  (e: "update:sort", v: string): void;
}>();

const searchModel = ref(props.search);
const sortModel = ref(props.sort);

watch(
  () => props.search,
  (v) => {
    searchModel.value = v;
  },
);
watch(
  () => props.sort,
  (v) => {
    sortModel.value = v;
  },
);

const sortOptions = [
  { label: "Newest First", value: "newest" },
  { label: "Oldest First", value: "oldest" },
  { label: "Title A–Z", value: "title_asc" },
  { label: "Title Z–A", value: "title_desc" },
];
</script>
