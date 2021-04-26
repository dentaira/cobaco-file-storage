<template>
  <div id="app">
    <Header />
    <router-view />
  </div>
</template>

<style>
html, body {
    color: #393939;
    font-family: sans-serif;
    font-size: 16px;
}
*, *::before, *::after {
    box-sizing: border-box;
}
body, div, p, h1, h2, h3, h4, ul, figure {
    margin: 0;
    padding: 0;
}
p, td, th, li {
    line-height: 1.8;
}
img {
    height: auto;
    width: 100%;
}
a {
    color: #1864b9;
    text-decoration: none;
}
a:hover {
    color: #1864b9;
}
.img-responsive {
    display: block;
    height: auto;
    max-width: 100%;
}

.float-clear {
    overflow: hidden;
}

.container {
    margin: 0 auto;
    max-width: 1000px;
}
</style>

<script lang="ts">
import 'normalize.css'
import { defineComponent } from 'vue'
import HeaderComponent from '@/components/Header.vue'

export default defineComponent({
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  setup() {
    
  },
  components: {
    Header: HeaderComponent,
  }
})
</script>
