import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Home from '../views/Home.vue'
import Mod from '../views/Mod.vue'
import User from '../views/User.vue'
import {ory, userApi} from "@/store/apis";
import {Session} from "@ory/client";


const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'Home',
        component: Home,
    },
    {
        path: '/user',
        name: 'User',
        component: User,
        meta: {
            requiresAuth: true
        }
    },
    {
        path: '/mod',
        name: 'Mod',
        component: Mod,
        meta: {
            requiresAuth: true,
            group: "user"
        }
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})
router.beforeEach(async (to, from)  => {
    if (to.meta.requiresAuth) {
        let session = await ory.toSession().catch((error) => {
            return null
        });
        if(session == null){
            return router.push("/");
        }

        if (session.data.active) {
            switch (to.meta.group) {
                case "moderator" || "admin":
                    userApi.getUserDetails().then((response) => {
                        if (response.data.group === "user") {
                            return router.push("/");
                        }
                    });
                default:
                    return true;
            }
        }
    }else{
        return true;
    }
});

    export default router
