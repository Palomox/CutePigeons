import Vue, { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import {Auth0Plugin} from "@/auth/Auth";
import {domain, audience, clientId} from '@/auth0.config.json'
import {Configuration, V0alpha2Api} from "@ory/kratos-client";

export const DEFAULT_REDIRECT_CALLBACK = (appState: any) => window.history.replaceState({}, document.title, window.location.pathname)
const app = createApp(App)
app.use(router)

const ory =  new V0alpha2Api(
    new Configuration({
        basePath: "http://localhost:4000/.ory",
    })
)

app.provide("$kratos", ory)
/*
app.use(Auth0Plugin, {
    domain: domain,
    clientId: clientId,
    audience: audience,
    scope: "write:pigeons",
    onRedirectCallback: (appState : any ) => {
        router.push(
            appState && appState.targetUrl
                ? appState.targetUrl
                : window.location.pathname
        )
    }
});
*/
app.mount('#app')
