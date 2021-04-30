class Pigeon {
    id: number;
    url: string;

    constructor(id: number, url: string) {
        this.id = id;
        this.url = url;
    }
}
// https://www.mockable.io/a/#/space/demo4167035/rest/XpaI3GAAA
const APIURLMOCK = "https://608be45c9f42b20017c3d13f.mockapi.io/api/v1/"
const APIURL = "https://cutepigeons.palomox.ga/api/v1/"
document.getElementById("random").addEventListener("click", ev => {
    getRandomPigeon().then(p => {
        document.getElementById("pigeonid").textContent = "Pigeon number " + p.id.toString()
        document.getElementById("pigeonimage").setAttribute("src", p.url)
    })
})
document.addEventListener("load", ev => {
    getRandomPigeon().then(p => {
        document.getElementById("pigeonid").textContent = "Pigeon number " + p.id.toString()
        document.getElementById("pigeonimage").setAttribute("src", p.url)
    })
})

async function getPigeonById(id: number) {
    let response = await fetch(APIURL + "getPigeonById?id=" + id,
        {
            method: "GET",
            mode: "cors"
        })
    let responseJson = await response.json()
    let pigeonId = responseJson.id
    let pigeonUrl = responseJson.url
    return new Pigeon(pigeonId, pigeonUrl)
}
async function getPigeonsCount(){
    let json = await getPigeonsJson();
    return await json.pigeons.length
}
async function getRandomPigeon() {
    var r = getRandomInt(1, await getPigeonsCount())
    return getPigeonById(r)
}

function getRandomInt(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
/**
 * Returns the JSON of all the pigeons
 */
async function getPigeonsJson(){
    let response = await fetch(APIURL+"getPigeons", {
        method: "GET",
        mode: "cors"
    })
    let json = await response.json()
    console.log(json.pigeons.length)
    return json
}