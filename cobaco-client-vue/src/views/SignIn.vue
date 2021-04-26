<template>
  <section class="signInArea">
    <form class="signInForm">
      <img src="@/assets/logo.png" class="logo" alt="Cobaco" />
      <input v-on="username" placeholder="Username" />
      <input v-on="password" placeholder="Password" />
      <button class="button" href="#" @click="signIn">Sign in</button>
    </form>
  </section>
</template>

<style>
.signInArea {
  margin: 100px auto;
}
.signInForm {
  display: flex;
  flex-flow: column;
  margin: auto;
  width: 300px;
}
.sigInForm,
input {
  width: 100%;
  height: 40px;
  margin: 10px auto;
  outline: none;
}
.logo {
  width: 210px;
}
.button {
  display: inline-block;
  width: 100%;
  height: 45px;
  border-radius: 4px;
  background-color: #007bff;
  border: 0;
  color: #fff;
  font-size: 1.2rem;
  margin: 10px auto;
  text-align: center;
  text-decoration: none;
}
.button:hover,
.button:focus {
  opacity: 0.8;
  outline: none;
}
</style>

<script lang="ts">
import { profileMockData, profileStore } from "@/store/profile";
import { defineComponent } from "vue";
import { useRouter } from "vue-router";

export default defineComponent({
  setup() {
    const router = useRouter();

    const signIn = () => {
      profileStore.profile = profileMockData;
      router.push("/home");
    };

    return {
      signIn,
    };
  },
});
</script>
