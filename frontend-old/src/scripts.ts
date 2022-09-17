class Pigeon {
    id: number;
    url: string;

    constructor(id: number, url: string) {
        this.id = id;
        this.url = url;
    }
}
const APIURLMOCK = "https://608be45c9f42b20017c3d13f.mockapi.io/api/v1/"
const APIURL = "https://cutepigeons.palomox.ga/api/v1/"
let pigeons: any
let mode = 'dark'
document.getElementById('darkswitch').addEventListener("click", ev => {
    if(mode == 'light') {
        document.querySelector("html").classList.add('dark')
        mode = 'dark'
    }else{
        document.querySelector("html").classList.remove('dark')
        mode = 'light'
    }
})
document.getElementById("random").addEventListener("click", ev => {
    setRandomPigeon()
})
window.addEventListener("load", ev => {
    let urlParams = new URLSearchParams(window.location.search)
    if (urlParams.has("id")){
        setPigeon(urlParams.get("id"))
    }else {
        setRandomPigeon()
    }
})
window.addEventListener("keypress", ev => {
    if(ev.key == "Enter"){
        setRandomPigeon()
    }
})
document.getElementById("initialpopup__dimiss").addEventListener("click", ev => {
    dismissPopup("initialpopup")
})
function dismissPopup(id : string){
    document.getElementById(id).style.animation = "popupVanish 1s forwards";
    //DEBUG
    //document.getElementById(id).remove()
}
async function setPigeon(id : string) {
    let pigeon = await getPigeonById(id)
    if(pigeon == null){
        document.getElementById("pigeonid").textContent = "Couldn't find pigeon number "+id
        document.getElementById("initialpopup").style.top = "50%"
        document.getElementById("initialpopup").style.visibility="visible"
        document.getElementById("initialpopup").style.backgroundColor="#ff0000"
        document.getElementById("initialpopup__text").textContent = "Pigeon not found"
        document.getElementById("pigeonid").style.fontSize = "50"
        document.getElementById("pigeonimage").style.visibility="hidden"
        return
    }else{
    document.getElementById("pigeonid").textContent = "Pigeon number " + pigeon.id.toString()
    document.getElementById("pigeonimage").setAttribute("src", pigeon.url)
    }
}
function setRandomPigeon(){
    getRandomPigeon().then(p => {
        document.getElementById("pigeonid").textContent = "Pigeon number " + p.id.toString()
        document.getElementById("pigeonimage").setAttribute("src", p.url)
        document.getElementById("pigeonimage").style.visibility = "visible"
    })
}
function onLoad(){
    getRandomPigeon().then(p => {
        document.getElementById("pigeonid").textContent = "Pigeon number " + p.id.toString()
        document.getElementById("pigeonimage").setAttribute("src", p.url)
    })
}

async function getPigeonById(id: string) : Promise<Pigeon>{
    let pigeon = await fetch(APIURL+"public/getPigeonById?id="+id, {
        method: "GET",
        mode: "cors"
    })
    if(!pigeon.ok){
       return null;
    }else {
       return pigeon.json()
    }
}
async function getPigeonsCount(){
    let json = await getPigeonsJson();
    return await json.pigeons.length
}
async function getRandomPigeon() : Promise<Pigeon>{
    let r = getRandomInt(1, await getPigeonsCount())
    return getPigeonById(r.toString())
}

function getRandomInt(min : number, max:number) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
/**
 * Returns the JSON of all the pigeons
 */
async function getPigeonsJson() {
    if(pigeons == null) {
        let response = await fetch(APIURL + "public/getPigeons", {
            method: "GET",
            mode: "cors"
        })
        let json = await response.json()
        console.log(json.pigeons.length)
        pigeons = json;
    }
    return pigeons
}