import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import {Auth0Plugin} from "@/auth/Auth";
import {domain, audience, clientId} from '@/auth0.config.json'

export const DEFAULT_REDIRECT_CALLBACK = (appState: any) => window.history.replaceState({}, document.title, window.location.pathname)
const app = createApp(App)
app.use(router)
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
app.mount('#app')
