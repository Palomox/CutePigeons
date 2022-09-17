import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import {loadFonts} from "@/plugins/webfontloader";
/* import the fontawesome core */
import {library} from '@fortawesome/fontawesome-svg-core'

/* import font awesome icon component */
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome'

/* import specific icons */
import {faHeart as farHeart, faTrashCan} from '@fortawesome/free-regular-svg-icons'
import {faBinoculars, faCheck, faHeart as fasHeart, faHouse, faPlus, faXmark} from '@fortawesome/free-solid-svg-icons'
import Toast, {PluginOptions, POSITION} from "vue-toastification";


import "vue-toastification/dist/index.css";

/* add icons to the library */
library.add(farHeart, fasHeart, faPlus, faXmark, faTrashCan, faCheck, faBinoculars, faHouse)

export const DEFAULT_REDIRECT_CALLBACK = (appState: any) => window.history.replaceState({}, document.title, window.location.pathname)

loadFonts()

const app = createApp(App)
app.use(router)
    .component("font-awesome-icon", FontAwesomeIcon);

const options: PluginOptions = {
    position: POSITION.BOTTOM_LEFT
}
app.use(Toast, options);

app.mount('#app')
