<template>
  <div class="user">
    <h1>Create User</h1>

    <h3>Just some database interaction...</h3>

    <input type="text" v-model="user.name" placeholder="name">
    <input type="text" v-model="user.email" placeholder="email">

    <button @click="createUser()">Create User</button>

    <div v-if="showResponse"><h6>User created with email: {{ response }}</h6></div>

    <button v-if="showResponse" @click="retrieveUser()">Retrieve user {{user.email}} data from database</button>

    <h4 v-if="showRetrievedUser">Retrieved User {{retrievedUser.name}} {{retrievedUser.email}}</h4>

  </div>
</template>

<script>
  // import axios from 'axios'
  import {AXIOS} from './http-common'

  export default {
    name: 'user',

    data () {
      return {
        response: [],
        errors: [],
        user: {
          email: '',
          name: '',
          // id: 0
        },
        showResponse: false,
        retrievedUser: {},
        showRetrievedUser: false
      }
    },
    methods: {
      // Fetches posts when the component is created.
      createUser () {
        var params = new URLSearchParams()
        params.append('name', this.user.name)
        params.append('email', this.user.email)

        AXIOS.post(`/user`, params)
          .then(response => {
            // JSON responses are automatically parsed.
            this.response = response.data
            this.user.email = response.data
            console.log(response.data)
            this.showResponse = true
          })
          .catch(e => {
            this.errors.push(e)
          })
      },
      retrieveUser () {
        AXIOS.get(`/user/` + this.user.email)
          .then(response => {
            // JSON responses are automatically parsed.
            this.retrievedUser = response.data
            console.log(response.data)
            this.showRetrievedUser = true
          })
          .catch(e => {
            this.errors.push(e)
          })
      }
    }
  }

</script>


<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  h1, h2 {
    font-weight: normal;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a {
    color: #42b983;
  }
</style>
