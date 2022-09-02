import Vue, { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import {loadFonts} from "@/plugins/webfontloader";

export const DEFAULT_REDIRECT_CALLBACK = (appState: any) => window.history.replaceState({}, document.title, window.location.pathname)

loadFonts()

const app = createApp(App)
app.use(router)


app.mount('#app')
