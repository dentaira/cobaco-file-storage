<template>
  <Breadcrumb :folder="folder" @folderId="updateFolder" @toHomeEvent="toHome" />
  <section>
    <div class="container">
      <main class="main">
        <div class="action-toolbar float-clear">
          <form
            name="uploadForm"
            action="/file/upload/"
            enctype="multipart/form-data"
            method="POST"
          >
            <button
              class="upload-button"
              onclick="clickFile('uploadFile'); return false"
            >
              ファイルをアップロード
            </button>
            <input
              type="file"
              id="uploadFile"
              onchange="submitUpload()"
              class="upload-button"
            />
          </form>
          <form
            name="createDirForm"
            action="/file/create/directory/"
            method="POST"
          >
            <button
              onclick="clickFile('createDirName'); return false"
              class="create-button"
            >
              フォルダを作成
            </button>
            <input
              type="text"
              style="display: none"
              onclick="submitCreateDir()"
              id="createDirName"
              name="createDirName"
            />
          </form>
        </div>
        <div>
          <table class="file-table">
            <thead>
              <tr>
                <th scope="col">ファイル名</th>
                <th scope="col">サイズ</th>
                <th scope="col">削除</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="file in folder.children" :key="file.name">
                <td>
                  <div class="float-clear">
                    <div class="file-icon" v-bind:file-type="file.type" />
                    <a href="#" @click="updateFolder(file.fileId)">{{
                      file.name
                    }}</a>
                  </div>
                </td>
                <td>
                  <span class="file-size-text" v-if="file.type === 'FILE'">{{
                    file.size
                  }}</span>
                </td>
                <td>
                  <a name="delete" href="#" data-id="storedFile.id">削除</a>
                </td>
              </tr>
            </tbody>
          </table>
          <form name="deleteForm" action="/file/delete/" method="POST" />
        </div>
      </main>
    </div>
  </section>
</template>

<style>
/* アクションツールバー */
.action-toolbar button {
  margin-right: 20px;
  min-height: 40px;
  background-size: 20px 20px;
  background-position-x: 10px;
  background-position-y: center;
  padding-left: 34px;
  padding-right: 12px;
  border: none;
  outline: none;
  color: #fff;
}
#uploadFile {
  display: none;
}
.upload-button {
  float: left;
  background: url(../assets/upload.png) no-repeat mediumaquamarine;
}
.upload-button:hover,
.upload-button:focus,
.upload-button:active {
  background-color: #82cfad;
}
.create-button {
  background: url(../assets/plus.png) no-repeat gold;
}
.create-button:hover,
.create-button:focus,
.create-button:active {
  background-color: #ffe14c;
}

/** ファイル一覧 */
.file-table {
  border-collapse: collapse;
  width: 100%;
}
.file-table th {
  text-align: left;
}
.file-table th,
.file-table td {
  border-bottom: 1px lightgray solid;
  height: 50px;
}
.file-icon {
  float: left;
  margin: auto 15px;
}
.file-icon[file-type="FILE"] {
  content: url(../assets/file.png);
  height: 32px;
  width: 32px;
}
.file-icon[file-type="DIRECTORY"] {
  content: url(../assets/folder.png);
  height: 32px;
  width: 32px;
}
.file-size-text {
  font-size: 0.9rem;
}
</style>

<script lang="ts">
import { defineComponent, reactive, computed, toRefs } from "vue";
import { folderStore } from "@/store/folder";
import { Folder } from '@/store/folder.model';
import BreadcrumbComponent from "@/components/Breadcrumb.vue";

export default defineComponent({
  setup() {
    const state = reactive({
      folder: folderStore.home,
    });

    const updateFolder = (fileId: string) => {
      console.log(fileId);
      state.folder = folderStore.get(fileId);
    };

    const toHome = () => {
      state.folder = folderStore.home;
    };

    return {
      ...toRefs(state),
      updateFolder,
      toHome,
    };
  },

  components: {
    Breadcrumb: BreadcrumbComponent,
  },
});
</script>
