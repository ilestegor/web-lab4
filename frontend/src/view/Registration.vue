<script>

import useVuelidate from "@vuelidate/core";
import {helpers, required} from "@vuelidate/validators";
import CustomInput from "@/component/CustomInput.vue";


export default {
  name: 'registration-component',
  components: {CustomInput},
  data() {
    return {
      v$: useVuelidate(),
      event: {
        login: "",
        password: ""
      },
      userHasAccount: true,
    }
  },
  validations() {
    return {
      event: {
        login: {required: helpers.withMessage('Username field cannot be empty', required)},
        password: {required: helpers.withMessage('Password field cannot be empty', required)}
      }
    }
  },
  methods: {
    async userLogin() {
      this.v$.$validate();
      if (!this.v$.$error) {
        const user = {username: this.event.login, password: this.event.password}
        const url = "http://localhost:8080/lab4Spring/api/auth/login";
        const loginRequest = await this.$store.dispatch('auth/userAuthRequest', {user, url});
        if (loginRequest === null) {
          this.$notify({group: 'user_login', text: 'Server is down, cannot login'})
          return;
        }
        if (loginRequest !== undefined && loginRequest.status === 200) {
          const validResponse = await loginRequest.json()
          localStorage.setItem("exp_date", validResponse.tokenExpirationDate.toString())
          this.$router.push("/main")
        } else if (loginRequest !== undefined) {
          const jsonResponse = await loginRequest.json();
          this.$notify({group: 'user_login', text: jsonResponse.detailMessage})
        }
      }
    },
    async userRegister() {
      this.v$.$validate();
      if (!this.v$.$error) {
        const user = {username: this.event.login, password: this.event.password}
        const url = "http://localhost:8080/lab4Spring/api/auth/register";
        const registerRequest = await this.$store.dispatch('auth/userAuthRequest', {user, url})
        if (registerRequest !== null) {
          console.log(registerRequest)
          switch (registerRequest.status) {
            case 200:
              localStorage.setItem("exp_date", validResponse.tokenExpirationDate.toString())
              this.$router.push("/main");
              break;
            case 400:
              const jsonResponse = await registerRequest.json();
              this.$notify({group: 'user_login', text: jsonResponse.detailMessage})
          }
        } else {
          this.$notify({group: 'user_login', text: 'Server is down, cannot register'})
        }
      }
    },
    changeUserDoesntHaveAccount() {
      this.userHasAccount = false;
    },
    changeUserHasAccount() {
      this.userHasAccount = true;
    }
  },
  mounted() {
    if (localStorage.getItem("exp_date") !== null && localStorage.getItem('exp_date') < Date.now()) {
      this.$notify({group: 'user_login', text: 'Token is expired, log in again'})
    }
  }
}
</script>

<template>
  <notifications group="user_login"/>
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
        <p v-for="error in v$.event.login.$errors" :key="error.$uid">{{ error.$message }}</p>
      </div>
      <div class="field-wrap">
        <custom-input
            class="input-field m-top10"
            input-type="password"
            placeholder-text="Password"
            v-model:input-value.trim="event.password"
            placeholder="Password"
        />
        <p v-for="error in v$.event.password.$errors" :key="error.$uid">{{ error.$message }}</p>
      </div>
      <my-button v-if="userHasAccount" class="registration-btn" @click="userLogin">Login</my-button>
      <my-button v-else class="registration-btn" @click="userRegister()">Register</my-button>
    </form>
    <div class="signup-wrapper m-top10">
      <p v-if="userHasAccount">Don`t have an account? <span class="signup-link" @click="changeUserDoesntHaveAccount()">SignUp</span>
      </p>
      <p v-else>Already have an account? <span class="signup-link" @click="changeUserHasAccount()">Login</span></p>
    </div>
  </div>
</template>

<style>
.form-wrap {
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

.form-wrap > h1 {
  font-size: 28px;
}

.registration-btn {
  margin-top: 15px;
  width: 90%;
}

.form {
  width: 100%;
}

.input-field > .input {
  width: 90%;
}

.registration-btn > .btn {
  width: 90%;
}

.signup-link {
  cursor: pointer;
  text-decoration: none;
  color: #6472da;
}

.field-wrap {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.field-wrap > p {
  text-align: left;
  margin-left: 7%;
  margin-top: 3px;
  font-size: 11px;
  color: #e54545;
}
</style>