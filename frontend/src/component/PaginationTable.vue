<script>

import {VueAwesomePaginate} from "vue-awesome-paginate";

export default {
  name: "PaginationTable",
  components: {VueAwesomePaginate},

  data() {
    return {
      curPage: 1,
    }
  },
  props: {
    array: {
      type: Array,
      default: ""
    },
    contentPerPage: {
      type: Number,
      default: 4
    },
    maxPageShow: {
      type: Number,
      default: 2
    },
    headers: {
      type: Array,
      default: ""
    },
    labels: {
      type: Array
    }
  },
  computed: {
    totalPages() {
      //TODO: put array length instead of hardcode
      return Math.ceil(this.array.length / this.contentPerPage)
    },
    indexStart() {
      return (this.curPage - 1) * this.contentPerPage
    },
    indexEnd() {
      return this.indexStart + this.contentPerPage;
    }
  },
  methods: {
    paginated() {
      return this.array.slice(this.indexStart, this.indexEnd)
    }
  }
}
</script>

<template>
  <div class="table-stat">
    <vue-awesome-paginate
        :total-items="this.array.length"
        :items-per-page="this.contentPerPage"
        :show-breakpoint-buttons="true"
        :max-pages-shown="this.maxPageShow"
        v-model="curPage"
        prev-button-content="⬅️"
        next-button-content="➡️"
        back-button-class="arrow-style"
        next-button-class="arrow-style"
    />
    <table>
      <thead>
      <th v-for="(header, index) in this.headers" :key="index">
        {{ header.value }}
      </th>
      </thead>
      <tbody>
      <tr v-for="(s, id) in paginated()" :key="id">
        <td v-for="(k) in headers">
          {{ s[k.label] }}
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<style>
.control-panel {
  display: flex;
  justify-content: center;
  text-align: center;
  margin-bottom: 10px;
  gap: 5px;
}

.control-panel > button {
  background-color: transparent;
  border: none;
  cursor: pointer;
}

table, th, tr, td {
  border: 1px solid white;
  border-collapse: collapse;
  padding: 3px;
}

.pagination-container {
  display: flex;
  column-gap: 10px;
  justify-content: center;
  list-style: none;
  margin-bottom: 10px;
}

.paginate-buttons {
  width: 40px;
  border-radius: 20px;
  cursor: pointer;
  background-color: rgb(242, 242, 242);
  border: 1px solid rgb(217, 217, 217);
  color: black;
}

.paginate-buttons:hover {
  background-color: #d8d8d8;
}

.active-page {
  background-color: #3498db;
  border: 1px solid #3498db;
  color: white;
}

.active-page:hover {
  background-color: #2988c8;
}

.arrow-style {
  background-color: transparent;
  border: none;
}

.paginate-buttons {
  background-color: transparent;
  color: white;
}

.paginate-buttons:hover {
  background-color: transparent;
}
</style>