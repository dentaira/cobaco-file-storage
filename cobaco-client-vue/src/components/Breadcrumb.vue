<template>
  <section>
    <div class="container">
      <div class="breadcrumb">
        <ol>
          <li v-if="!isHome">
            <a href="#" @click="toHome">home</a>
          </li>
          <template v-for="(id, index) in folder.ancestorsId" :key="index">
            <li>
              <a href="#" @click="updateFolder(id)">{{
                folder.ancestorsName[index]
              }}</a>
            </li>
          </template>
          <li>
            <span>{{ folder.name }}</span>
          </li>
        </ol>
      </div>
    </div>
  </section>
</template>

<style>
/* パンくずリスト */
.breadcrumb {
  min-height: 80px;
  margin-bottom: 20px;
}
.breadcrumb ol {
  list-style-type: none;
  margin: 0;
  padding: 0;
}
.breadcrumb li {
  font-size: 1.4rem;
  display: inline;
}
.breadcrumb li::after {
  content: "/";
  color: #999;
  margin: 0 7px;
}
.breadcrumb li:last-child::after {
  content: none;
}
</style>

<script lang="ts">
import { computed, defineComponent, PropType, reactive, toRefs } from "vue";
import { Folder } from "@/store/folder.model";
import { folderStore } from "@/store/folder";

export default defineComponent({
  props: {
    folder: {
      type: Object as PropType<Folder>,
    },
  },

  setup(props, context) {
    const state = reactive({
      folder: computed(() => {
        return props.folder;
      }),
    });

    const isHome = computed(() => {
      return state.folder!.fileId === folderStore.home.fileId;
    });

    const toHome = () => {
      context.emit("toHomeEvent");
    };

    const updateFolder = (id: string) => {
      context.emit("folderId", id);
    };

    return {
      ...toRefs(state),
      isHome,
      toHome,
      updateFolder,
    };
  },
});
</script>
