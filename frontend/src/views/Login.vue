<template>
  <div v-if="response.loaded">
    <button @click="logout">Logout</button>
    <form :method="response.response.ui.method" @submit.prevent="submit">
      <component :is="node.type" v-for="node in response.response.ui.nodes" :name="node.attributes.name" :type="node.attributes.type"
              :required="node.attributes.required" :disabled="node.attributes.disabled" :label="node.meta.label ? node.meta.label.text : null"
             v-model="node.attributes.value" />
      <!--component
          :is="'form-' + node.type + (node.attributes.type && !node.attributes.type.includes('/') ? '-' + node.attributes.type : '')"
          v-for="(node, idx) in response.response.ui.nodes"
          :key="idx"
          :node="node"
      />

      < input name="email" type="email"
          required="true"
         v-model="request.traits.email"/>
      <input name="password" type="password"
             required="true"
             v-model="request.password"/>
      <input name="Submit" type="submit"/ -->
    </form>
  </div>
</template>

<script lang="ts">
import {Options, Vue} from "vue-class-component";
import {V0alpha2Api, SelfServiceRegistrationFlow} from "@ory/kratos-client"
import {inject, nextTick, reactive, Ref, ref} from "vue";

@Options({
  name: "login",
  props: {
    flow: {
      required: false,
      type: String
    }
  }
})
export default class Login extends Vue {
  $kratos ?: V0alpha2Api
  request = reactive({
    method: "password",
    csrf_token: "",
    password: "",
    traits:{
      email: "",
    }
  })

  response = reactive({loaded: false, response: {}});
  flow?: string

  logout(){
    this.$kratos?.createSelfServiceLogoutFlowUrlForBrowsers().then(
        r => {this.$router.push(r.data.logout_url)}
    )
  }

  submit(){
    console.log(JSON.stringify(this.request));
    // @ts-ignore
    this.$kratos?.submitSelfServiceRegistrationFlow(this.response.response.id, this.request, {
      withCredentials: true
    }).then(value => {
      console.log(JSON.stringify(value))
    })
  }

  beforeMount() {
    this.$kratos = inject('$kratos')
    this.initLogin()
  }
  initLogin() {
      this.$kratos?.initializeSelfServiceRegistrationFlowForBrowsers("http://localhost:8080/login").then(value => {
        this.response.response = value.data
        this.response.loaded = true;
        // @ts-ignore
        this.request.csrf_token = this.response.response.ui.nodes[0].attributes.value
        console.log(JSON.stringify(this.response));
      });
    }
}
</script>

<style scoped>
@tailwind base;
@tailwind components;
@tailwind utilities;

form{
  @apply flex flex-col gap-1 text-black;
}
div{
  @apply flex flex-col items-center  w-1/2;
}
</style>
