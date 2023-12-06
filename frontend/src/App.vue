<script>
import {RouterView} from "vue-router";

export default {
  data(){
    return{
      headerProps: {
        author: "Glotov E. D.",
        group: "P3232",
        varNum: "4432"
      }
    }

  }


}


</script>

<template>
    <my-header v-bind="headerProps"></my-header>
    <RouterView/>
</template>

<style scoped>

</style>
