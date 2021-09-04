<template>
  <section class="signInArea">
    <div class="signInForm">
      <img src="@/assets/logo.png" class="logo" alt="Cobaco" />
      <input v-model="username" placeholder="Username" />
      <input v-model="password" placeholder="Password" />
      <button class="button" href="#" @click="signIn">Sign in</button>
    </div>
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
  height: 40px;
  margin: 10px auto;
  outline: none;
  width: 100%;
}
.logo {
  width: 210px;
}
.button {
  background-color: #007bff;
  border: 0;
  border-radius: 4px;
  color: #fff;
  display: inline-block;
  font-size: 1.2rem;
  height: 45px;
  margin: 10px auto;
  text-align: center;
  text-decoration: none;
  width: 100%;
}
.button:hover,
.button:focus {
  opacity: 0.8;
  outline: none;
}
</style>

<script lang="ts">
import { profileStore, signInAsync } from "@/store/profile";
import { defineComponent, reactive, toRefs } from "vue";
import { useRouter } from "vue-router";

export default defineComponent({
  setup() {
    const state = reactive({
      username: null as string | null,
      password: null as string | null,
    })
    const router = useRouter();

    const signIn = async () => {
      if (state.username === null) {
        console.info(state.username);
        console.error('ユーザー名を入力してください。');
        return;
      }
      if (state.password === null) {
        console.error('パスワードを入力してください。');
        return;
      }

      // eslint-disable-next-line no-useless-catch
      try {
        profileStore.profile =  await signInAsync(state.username!, state.password!);
      } catch (error) {
        throw error;
      }

      router.push("/home");
    };

    return {
      ...toRefs(state),
      signIn,
    };
  },
});
</script>
