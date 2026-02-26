<template>
  <v-app :theme="theme">
    <div
      class="auth-bg"
      style="
        min-height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
      "
    >
      <v-card class="auth-card" rounded="lg" elevation="8" width="420">
        <v-card-text class="pa-8">
          <v-row justify="center" no-gutters class="mb-6">
            <v-col cols="auto" class="d-flex align-center">
              <div class="logo-icon d-flex align-center justify-center mr-3">
                <v-icon color="white" size="38">mdi-note-text</v-icon>
              </div>
            </v-col>
            <v-col cols="auto" class="d-flex align-center">
              <div>
                <h1 class="text-h5 font-weight-bold" style="line-height: 1.2">
                  Smart Notes
                </h1>
                <p class="text-body-2 text-medium-emphasis mb-0">
                  Sign in to your account
                </p>
              </div>
            </v-col>
          </v-row>

          <v-form @submit.prevent="submit">
            <v-text-field
              v-model="email"
              label="Email"
              type="email"
              prepend-inner-icon="mdi-email-outline"
              variant="outlined"
              density="comfortable"
              :error-messages="emailError"
              class="mb-3"
            />
            <v-text-field
              v-model="password"
              label="Password"
              :type="showPassword ? 'text' : 'password'"
              prepend-inner-icon="mdi-lock-outline"
              :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
              @click:append-inner="showPassword = !showPassword"
              variant="outlined"
              density="comfortable"
              class="mb-4"
            />

            <v-alert
              v-if="authStore.error"
              type="error"
              density="compact"
              class="mb-4"
              rounded="lg"
            >
              {{ authStore.error }}
            </v-alert>

            <v-btn
              type="submit"
              color="primary"
              block
              size="large"
              rounded="lg"
              :loading="authStore.loading"
            >
              Sign In
            </v-btn>
          </v-form>

          <div class="text-center mt-4">
            <span class="text-body-2 text-medium-emphasis"
              >Don't have an account?
            </span>
            <router-link
              to="/register"
              class="text-primary text-body-2 font-weight-medium"
              >Register</router-link
            >
          </div>
        </v-card-text>
      </v-card>
    </div>
  </v-app>
</template>

<script lang="ts" setup>
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "@/stores/auth";

const router = useRouter();
const authStore = useAuthStore();

const email = ref("");
const password = ref("");
const showPassword = ref(false);
const emailError = ref("");

const theme = computed(() => localStorage.getItem("theme") ?? "light");

function validateEmail(val: string) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val);
}

async function submit() {
  emailError.value = "";
  if (!validateEmail(email.value)) {
    emailError.value = "Enter a valid email address.";
    return;
  }
  const ok = await authStore.login(email.value, password.value);
  if (ok) router.push("/");
}
</script>

<style scoped>
.auth-bg {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.auth-card {
  background: #f5f2ff !important;
}
.logo-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea, #764ba2);
}
</style>
