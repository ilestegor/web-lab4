<script>
export default {
  name: "ContentBox"
}
</script>

<template>
  <div class="box">
    <slot/>
  </div>
</template>

<style scoped>
.box{
  border-radius: 10px;
  padding: 10px 10px;
  max-height: 350px;
  min-height: 320px;
  flex: 1;
  background-color: #232931;
  color: white;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.35);
}
</style>