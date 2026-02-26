<template>
  <v-app :theme="theme">
    <AppSidebar
      v-model="drawerOpen"
      :active-category="activeCategory"
      :theme="theme"
      @select="onCategorySelect"
    />

    <v-main>
      <!-- Sticky top bar -->
      <div class="top-bar">
        <!-- Hamburger (mobile only) -->
        <v-btn
          v-if="xs"
          icon="mdi-menu"
          variant="text"
          @click="drawerOpen = !drawerOpen"
        />
        <!-- Theme toggle on the LEFT -->
        <v-btn
          v-if="false"
          :icon="theme === 'dark' ? 'mdi-weather-sunny' : 'mdi-weather-night'"
          variant="text"
          @click="toggleTheme"
        />

        <!-- Search — grows to fill space -->
        <v-text-field
          v-model="search"
          placeholder="Search notes..."
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          density="compact"
          hide-details
          rounded="lg"
          style="flex: 1; min-width: 0"
          @update:model-value="onSearch"
        />

        <!-- Sort dropdown (tablet+) -->
        <div v-if="!xs" style="max-width: 200px; flex-shrink: 0">
          <v-select
            v-model="sort"
            :items="sortOptions"
            item-title="label"
            item-value="value"
            variant="outlined"
            density="compact"
            hide-details
            rounded="lg"
            @update:model-value="onSort"
          >
            <template #prepend-inner>
              <v-icon size="16" class="mr-1">mdi-sort</v-icon>
            </template>
          </v-select>
        </div>

        <!-- Sort icon menu (mobile) -->
        <v-menu v-else>
          <template #activator="{ props }">
            <v-btn icon="mdi-sort" variant="text" v-bind="props" />
          </template>
          <v-list density="compact">
            <v-list-item
              v-for="opt in sortOptions"
              :key="opt.value"
              :title="opt.label"
              :active="sort === opt.value"
              active-color="primary"
              @click="onSort(opt.value)"
            />
          </v-list>
        </v-menu>
      </div>

      <v-container fluid class="pa-6 px-12 main-content">
        <!-- Page title + count + New Note button -->
        <v-row no-gutters align="center" class="mb-6">
          <v-col>
            <h2 class="text-h5 font-weight-bold">{{ pageTitle }}</h2>
            <p class="text-body-2 text-medium-emphasis mt-1 text-gray-600">
              {{ notesStore.notes.length }} notes in total
            </p>
          </v-col>
          <v-col cols="auto">
            <v-btn
              class="btn-gradient"
              variant="flat"
              prepend-icon="mdi-plus"
              rounded="lg"
              @click="openCreate"
            >
              New Note
            </v-btn>
          </v-col>
        </v-row>

        <!-- Loading -->
        <div v-if="notesStore.loading" class="d-flex justify-center py-12">
          <v-progress-circular indeterminate color="primary" />
        </div>

        <!-- Empty state -->
        <div
          v-else-if="sortedNotes.length === 0"
          class="d-flex flex-column align-center py-16 text-medium-emphasis"
        >
          <v-icon size="64" class="mb-4">mdi-note-off-outline</v-icon>
          <p class="text-h6">No notes found</p>
          <p class="text-body-2">
            Create your first note with the button above.
          </p>
        </div>

        <!-- Notes grid -->
        <v-row v-else>
          <v-col
            v-for="note in sortedNotes"
            :key="note.id"
            cols="12"
            sm="4"
            lg="3"
          >
            <NoteCard
              :note="note"
              @click="openView"
              @edit="openEdit"
              @delete="confirmDelete"
            />
          </v-col>
        </v-row>
      </v-container>
    </v-main>

    <!-- Create / Edit dialog -->
    <v-dialog v-model="dialog" max-width="560" rounded="lg">
      <v-card rounded="lg">
        <v-card-title class="pa-5 pb-3 text-h6">
          {{ editingNote ? "Edit Note" : "New Note" }}
        </v-card-title>
        <v-card-text class="px-5 pb-2">
          <v-text-field
            v-model="form.title"
            label="Title (optional)"
            variant="outlined"
            density="comfortable"
            class="mb-3"
          />
          <v-textarea
            v-model="form.content"
            label="Content"
            variant="outlined"
            rows="5"
            auto-grow
          />
        </v-card-text>
        <v-card-actions class="px-5 pb-4">
          <v-spacer />
          <v-btn variant="text" @click="dialog = false">Cancel</v-btn>
          <v-btn
            color="primary"
            variant="flat"
            rounded="lg"
            :loading="saving"
            @click="saveNote"
          >
            {{ editingNote ? "Save" : "Create" }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Delete confirm dialog -->
    <v-dialog v-model="deleteDialog" max-width="380">
      <v-card rounded="lg">
        <v-card-title class="pa-5 pb-2">Delete note?</v-card-title>
        <v-card-text class="px-5 pb-2 text-medium-emphasis">
          This action cannot be undone.
        </v-card-text>
        <v-card-actions class="px-5 pb-4">
          <v-spacer />
          <v-btn variant="text" @click="deleteDialog = false">Cancel</v-btn>
          <v-btn
            color="error"
            variant="flat"
            rounded="lg"
            :loading="saving"
            @click="doDelete"
          >
            Delete
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-app>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from "vue";
import { useDisplay } from "vuetify";
import { useNotesStore, type Note, type CategoryType } from "@/stores/notes";
import AppSidebar from "@/components/AppSidebar.vue";
import NoteCard from "@/components/NoteCard.vue";

const { xs } = useDisplay();
const drawerOpen = ref(false);

const notesStore = useNotesStore();

// Theme
const theme = ref<string>(localStorage.getItem("theme") ?? "light");
function toggleTheme() {
  theme.value = theme.value === "dark" ? "light" : "dark";
  localStorage.setItem("theme", theme.value);
}

// Category filter
const activeCategory = ref<CategoryType | null>(null);
function onCategorySelect(cat: CategoryType | null) {
  activeCategory.value = cat;
  loadNotes();
}

const pageTitle = computed(() => {
  if (!activeCategory.value) return "All Notes";
  return (
    activeCategory.value.charAt(0) + activeCategory.value.slice(1).toLowerCase()
  );
});

// Search & sort
const search = ref("");
const sort = ref("newest");
const sortOptions = [
  { label: "Newest First", value: "newest" },
  { label: "Oldest First", value: "oldest" },
  { label: "Title A–Z", value: "title_asc" },
  { label: "Title Z–A", value: "title_desc" },
];
let searchTimeout: ReturnType<typeof setTimeout>;

function onSearch(val: string) {
  clearTimeout(searchTimeout);
  searchTimeout = setTimeout(() => {
    search.value = val;
    loadNotes();
  }, 350);
}
function onSort(val: string) {
  sort.value = val;
}

function loadNotes() {
  notesStore.fetchNotes(search.value, activeCategory.value);
}

onMounted(loadNotes);

// Sorting (client-side after fetch)
const sortedNotes = computed(() => {
  const arr = [...notesStore.notes];
  switch (sort.value) {
    case "oldest":
      return arr.sort(
        (a, b) =>
          new Date(a.createdDate).getTime() - new Date(b.createdDate).getTime(),
      );
    case "title_asc":
      return arr.sort((a, b) => (a.title ?? "").localeCompare(b.title ?? ""));
    case "title_desc":
      return arr.sort((a, b) => (b.title ?? "").localeCompare(a.title ?? ""));
    default:
      return arr.sort(
        (a, b) =>
          new Date(b.modifiedDate).getTime() -
          new Date(a.modifiedDate).getTime(),
      );
  }
});

// Create / Edit
const dialog = ref(false);
const saving = ref(false);
const editingNote = ref<Note | null>(null);
const form = ref({ title: "", content: "" });

function openCreate() {
  editingNote.value = null;
  form.value = { title: "", content: "" };
  dialog.value = true;
}
function openEdit(note: Note) {
  editingNote.value = note;
  form.value = { title: note.title ?? "", content: note.content };
  dialog.value = true;
}
function openView(note: Note) {
  openEdit(note);
}

async function saveNote() {
  saving.value = true;
  try {
    if (editingNote.value) {
      await notesStore.updateNote(
        editingNote.value.id,
        form.value.title,
        form.value.content,
      );
      await loadNotes();
    } else {
      await notesStore.createNote(form.value.title, form.value.content);
    }
    dialog.value = false;
  } finally {
    saving.value = false;
  }
}

// Delete
const deleteDialog = ref(false);
const noteToDelete = ref<Note | null>(null);

function confirmDelete(note: Note) {
  noteToDelete.value = note;
  deleteDialog.value = true;
}
async function doDelete() {
  if (!noteToDelete.value) return;
  saving.value = true;
  try {
    await notesStore.deleteNote(noteToDelete.value.id);
    deleteDialog.value = false;
  } finally {
    saving.value = false;
  }
}
</script>

<style scoped>
.top-bar {
  position: sticky;
  top: 0;
  z-index: 10;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 36px 36px;
  height: 64px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.08);
  background: rgb(var(--v-theme-surface));
}
.main-content {
  min-height: calc(100vh - 64px);
  background-color: #f1f5f9;
}
.btn-gradient {
  background: linear-gradient(to right, #3b82f6, #4f46e5) !important;
  color: white !important;
  box-shadow: 0 10px 15px -3px rgba(59, 130, 246, 0.25) !important;
  transition: background 0.2s ease !important;
}
.btn-gradient:hover {
  background: linear-gradient(to right, #2563eb, #4338ca) !important;
}
</style>
