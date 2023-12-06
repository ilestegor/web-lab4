<script>
export default {
  name: "my-button",
  methods:{
    onClickFunc(){
      this.$emit('click')
    }
  }
}
</script>

<template>
    <button class="btn" @click="onClickFunc">
      <slot></slot>
    </button>
</template>



<style scoped>
.btn {
  cursor: grab;
  border: none;
  font-weight: 700;
  transition: background 0.3s;
  -webkit-user-select: none;
  padding: 7px;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.35);
  background-color: #303f9f;
  color: white;
}

.btn:hover {
  background: #1a237e;
}
</style>