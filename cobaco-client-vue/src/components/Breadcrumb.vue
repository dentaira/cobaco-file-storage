<template>
  <section>
    <div class="container">
      <div class="breadcrumb">
        <ol>
          <li v-if="!isHome">
            <a href="#" @click="toHome">home</a>
          </li>
          <template v-for="(id, index) in folder?.ancestorIds" :key="index">
            <li>
              <a href="#" @click="updateFolder(id)">{{
                folder.ancestorNames[index]
              }}</a>
            </li>
          </template>
          <li>
            <span>{{ folder?.name }}</span>
          </li>
        </ol>
      </div>
    </div>
  </section>
</template>

<style>
/* パンくずリスト */
.breadcrumb {
  margin-bottom: 20px;
  min-height: 80px;
}
.breadcrumb ol {
  list-style-type: none;
  margin: 0;
  padding: 0;
}
.breadcrumb li {
  display: inline;
  font-size: 1.4rem;
}
.breadcrumb li::after {
  color: #999;
  content: "/";
  margin: 0 7px;
}
.breadcrumb li:last-child::after {
  content: none;
}
</style>

<script lang="ts">
import { computed, defineComponent, PropType, reactive, toRefs } from "vue";
import { Folder } from "@/store/folder.model";

export default defineComponent({
  props: {
    folder: {
      type: Object as PropType<Folder>,
    },
    isHome: {
      type: Boolean,
      required: true,
    }
  },

  setup(props, context) {
    const state = reactive({
      folder: computed(() => {
        return props.folder;
      }),
      isHome: computed(() => {
        return props.isHome;
      })
    });

    const toHome = () => {
      context.emit("toHomeEvent");
    };

    const updateFolder = (id: string) => {
      context.emit("folderId", id);
    };

    return {
      ...toRefs(state),
      toHome,
      updateFolder,
    };
  },
});
</script>
