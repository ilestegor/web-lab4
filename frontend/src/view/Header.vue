<script>
export default {
  name: "my-header",

  props: {
    author: {
      type: String,
      default: ""
    },
    group: {
      type: String,
      default: ""
    },
    varNum: {
      type: String,
      default: ""
    }
  }
}
</script>

<template>
  <div class="header">
    <p>{{ author }} {{ group }}</p>
    <p>Var: {{ varNum }}</p>
  </div>
</template>

<style scoped>
.header {
  width: 100%;
  border-radius: 10px;
  padding: 10px 0;
  background-color: #232931;
  color: white;
  box-shadow: 0 0 20px -10px white;
  transition: all 0.5s ease;
}

.header p:first-child {
  margin-bottom: 5px;
}

.header:hover {
  transform: scale(1.05);
}
</style>