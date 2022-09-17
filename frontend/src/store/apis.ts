import {Configuration, Session, V0alpha2Api} from "@ory/client";
import {ModApi, PostsApi, UserApi} from "cutepigeons-api";

export const basePath = process.env.ORY_URL || "http://localhost:4000"
export const apiPath = process.env.API_URL || "http://localhost:5555/api/v2"

export function getLoginLink() : string {
    return basePath + "/ui/login?return_to=" + window.location.href;
}


export const ory = new V0alpha2Api(
    new Configuration({
        basePath,
        baseOptions: {
            // Ensures we send cookies in the CORS requests.
            withCredentials: true,
        },
    }));

export const postsApi = new PostsApi(new Configuration({
    basePath: apiPath,
    baseOptions: {
        // Ensures we send cookies in the CORS requests.
        withCredentials: true,
    },
}))

export const modApi = new ModApi(new Configuration({
    basePath: apiPath,
    baseOptions: {
        // Ensures we send cookies in the CORS requests.
        withCredentials: true,
    }
}));

export const userApi = new UserApi(new Configuration({
    basePath: apiPath,
    baseOptions: {
        // Ensures we send cookies in the CORS requests.
        withCredentials: true,
    },
}))

export async function isLogged() : Promise<boolean> {
    let session = await ory.toSession();
    if (session.data) {
        if (session.data.active) {
            return true;
        }
    }
    return false;
}


let session: Session | null = null;

export async function getSession() {
    if (session) {
        return session;
    }

    const response = await ory.toSession();
    session = response.data;
    return session;
}
