<template>
    <div class="login">
    <h1>Login</h1>

    <input type="text" v-model="username" placeholder="username">
    <input type="text" v-model="password" placeholder="password">

    <button @click="login()">Login</button>

    <div v-if="showResponse"><h6>User created with email: {{ response }}</h6></div>

<!--    <button v-if="showResponse" @click="retrieveUser()">Retrieve user {{user.email}} data from database</button>-->
<!--    <h4 v-if="showRetrievedUser">Retrieved User {{retrievedUser.name}} {{retrievedUser.email}}</h4>-->
    </div>
</template>

<script>
    // import axios from 'axios'
    import {AXIOS} from './http-common'

    export default {
        name: 'login',
        data () {
            return {
                response: [],
                errors: [],
                username: '',
                password: '',
                showResponse: true,
            }
        },
        methods: {
            // Fetches posts when the component is created.
            login(){
                var params = new URLSearchParams();
                params.append('grant_type', 'password');
                params.append('username', this.username);
                params.append('password',this.password);
                AXIOS.post({
                    method:'post',
                    url:'oauth/token',
                    auth:{username:'my-trusted-client',password:'secret'},
                    headers: {"Content-type": "application/x-www-form-urlencoded; charset=utf-8"},
                    data:params
                }).then(function(response){
                    set_cookie("access_token",response.data.access_token);
                    this.response = response.data;
                    console.log(response.data)
                    // document.location.replace("/");
                }).catch(e => {
                    this.errors.push(e)
                })
            }
        }
    }
</script>
