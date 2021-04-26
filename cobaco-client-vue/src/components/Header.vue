<template>
  <header>
    <div class="container">
      <div class="header-inner" v-if="signInUser">
        <h1 class="header-logo">
          <a href="#"><img src="@/assets/logo.png" alt="Cobaco" /></a>
        </h1>
        <a href="#" @click="signOut">サインアウト</a>
      </div>
    </div>
  </header>
</template>

<style>
.header-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-logo {
  margin: 0;
  padding: 10px 0;
  width: 180px;
}
</style>

<script lang="ts">
import { computed, defineComponent, reactive, toRefs } from "vue";
import { profileMockData, profileStore } from "@/store/profile";
import { useRouter } from "vue-router";

export default defineComponent({
  setup(prop, context) {
    const state = reactive({
      signInUser: computed(() => {
        return profileStore.profile;
      }),
    });

    const router = useRouter();
    
    const signOut = () => {
      profileStore.profile = null;
      router.push("/sign-in");
    };

    return {
      ...toRefs(state),
      signOut,
    };
  },
});
</script>
