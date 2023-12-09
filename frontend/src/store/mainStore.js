import {createStore} from "vuex";

const authModule = {
    namespaced: true,
    state:{
        userIsLogged: false

    },
    mutations:{
        changeUserStatus(state, userState){
            state.userIsLogged = userState;
        }
    },
    getters:{
      getUserStatus(state){
          return state.userIsLogged;
      }
    },
    actions: {
        async userAuthRequest({commit}, {user, url}){
            try {
                const response =  await fetch(url, {
                    credentials: 'include',
                    method: "post",
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json',
                        'Cache': 'no-cache'
                    },
                    body: JSON.stringify(user)
                })
                commit('changeUserStatus', true)
                return response;
            } catch (ex){
                commit('changeUserStatus', false)
                return null
            }
        }
    }
}

const store = createStore({
    modules:{
        auth: authModule
    }
})
export default store