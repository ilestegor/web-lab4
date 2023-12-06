<script>

import axios from "axios";
import useVuelidate from "@vuelidate/core";
import {required, helpers, url} from "@vuelidate/validators";
import CustomInput from "@/component/CustomInput.vue";


export default {
  name: 'registration-component',
  components: {CustomInput},
  data(){
    return {
      v$: useVuelidate(),
      event:{
        login: "",
        password: ""
      },
      userHasAccount: true,
    }
  },
  validations(){
    return {
      event:{
        login: {required: helpers.withMessage('Username field cannot be empty', required)},
        password: {required: helpers.withMessage('Password field cannot be empty', required)}
      }
    }
  },
  methods:{
    userLogin(){
      this.v$.$validate();
      axios.defaults.withCredentials = true;
      if (!this.v$.$error){
        fetch("http://localhost:8080/lab4Spring/api/auth/login", {
          credentials: 'include',
          method: "post",
          headers:{
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Cache': 'no-cache'
          },
          body: JSON.stringify({
            username: this.event.login,
            password: this.event.password
          })
        }).then(r => console.log(r.headers));


        // axios({
        //   url:"http://localhost:8080/lab4Spring/api/auth/login",
        //   method: "post",
        //   credentials: 'include',
        //   data:{
        //     username: this.event.login,
        //     password: this.event.password
        //   },
        //   headers:{
        //     "Accept": "*/*",
        //     "Content-Type": 'application/json',
        //     "Cache-Control": "no-cache",
        //   },
        // }).then(r => console.log(r));


        // axios.post("http://localhost:8080/lab4Spring/api/auth/login",  {
        //   username: this.event.login,
        //   password: this.event.password
        // }, {
        //   withCredentials: true,
        //   headers:{
        //     'Accept': 'application/json',
        //     'Content-Type': 'application/json',
        //     'Cache': 'no-cache'
        //   }
        // }).then((r) => console.log(r.headers['set-cookie'])).catch(error => this.AxiosError(error.response.data))
      }
    },
    userRegister(){

    },
    changeUserDoesntHaveAccount(){
      this.userHasAccount = false;
    },
    changeUserHasAccount(){
      this.userHasAccount = true;
    },
    AxiosError(text){
      console.log(text)
    }
  }
}
</script>

<template>
  <div class="form-wrap common-box-style">
    <h1 v-if="userHasAccount">Login</h1>
    <h1 v-else>Signup</h1>
    <form @submit.prevent class="form m-top10">
      <div class="field-wrap">
        <custom-input
            class="input-field m-top10"
            input-type="text"
            placeholder-text="Username"
            v-model:input-value.trim="event.login"
            placeholder="Username"
        />
        <p v-for="error in v$.event.login.$errors" :key="error.$uid">{{error.$message}}</p>
      </div>
      <div class="field-wrap">
        <custom-input
            class="input-field m-top10"
            input-type="password"
            placeholder-text="Password"
            v-model:input-value.trim="event.password"
            placeholder="Password"
        />
        <p v-for="error in v$.event.password.$errors" :key="error.$uid">{{error.$message}}</p>
      </div>
      <my-button v-if="userHasAccount" class="registration-btn" @click="userLogin()">Login</my-button>
      <my-button v-else class="registration-btn" @click="userRegister()">Register</my-button>
    </form>
    <div class="signup-wrapper m-top10">
      <p v-if="userHasAccount">Don`t have an account? <span class="signup-link" @click="changeUserDoesntHaveAccount()">SignUp</span></p>
      <p v-else>Already have an account? <span class="signup-link" @click="changeUserHasAccount()">Login</span></p>
    </div>
  </div>
</template>

<style>
.form-wrap{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 10px;
  width: 100%;
  max-width: 350px;
  margin-top: 10px;
  border-radius: 10px;
  height: 100%;
  min-height: 300px;
}
.form-wrap > h1{
  font-size: 28px;
}
.registration-btn{
  margin-top: 15px;
  width: 90%;
}
.form{
  width: 100%;
}
.input-field > .input{
  width: 90%;
}
.registration-btn > .btn{
  width: 90%;
}
.signup-link{
  cursor: pointer;
  text-decoration: none;
  color: #6472da;
}
.field-wrap{
  display: flex;
  flex-direction: column;
  width: 100%;
}
.field-wrap > p{
  text-align: left;
  margin-left: 5.7%;
  margin-top: 3px;
  font-size: 11px;
  color: #e54545;
}
</style>