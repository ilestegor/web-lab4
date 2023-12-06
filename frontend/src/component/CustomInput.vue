<script>
export default {
  name: "custom-input",
  props: {
    label: {
      type: String,
      default: ""
    },
    inputValue: {
      type: [String, Number],
      default: ""
    },
    inputType: {
      type: String,
      default: 'text'
    },
    placeholderText: {
      type: String,
      default: ''
    }
  }
}
</script>

<template>
  <div class="input-wrap">
    <label>{{ label }}</label>
    <input
        class="input"
        :value="inputValue"
        :placeholder="placeholderText"
        :type="inputType"
        @input="$emit('update:inputValue', $event.target.value)"
    >
  </div>
</template>

<style scoped>
.input {
  text-align: left;
  outline: none;
  border: 1px solid grey;
  border-radius: 10px;
  padding: 5px 10px;
}
::placeholder {
  text-align: left;
  font-size: 12px;
}
</style>