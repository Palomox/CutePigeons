var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var Pigeon = /** @class */ (function () {
    function Pigeon(id, url) {
        this.id = id;
        this.url = url;
    }
    return Pigeon;
}());
// https://www.mockable.io/a/#/space/demo4167035/rest/XpaI3GAAA
var APIURLMOCK = "https://608be45c9f42b20017c3d13f.mockapi.io/api/v1/";
var APIURL = "https://cutepigeons.palomox.ga/api/v1/";
var mode = 'dark';
document.getElementById('darkswitch').addEventListener("click", function (ev) {
    if (mode == 'light') {
        document.querySelector("html").classList.add('dark');
        mode = 'dark';
    }
    else {
        document.querySelector("html").classList.remove('dark');
        mode = 'light';
    }
});
document.getElementById("random").addEventListener("click", function (ev) {
    getRandomPigeon().then(function (p) {
        document.getElementById("pigeonid").textContent = "Pigeon number " + p.id.toString();
        document.getElementById("pigeonimage").setAttribute("src", p.url);
    });
});
function onLoad() {
    getRandomPigeon().then(function (p) {
        document.getElementById("pigeonid").textContent = "Pigeon number " + p.id.toString();
        document.getElementById("pigeonimage").setAttribute("src", p.url);
    });
}
function getPigeonById(id) {
    return __awaiter(this, void 0, void 0, function () {
        var response, responseJson, pigeonId, pigeonUrl;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0: return [4 /*yield*/, fetch(APIURL + "getPigeonById?id=" + id, {
                        method: "GET",
                        mode: "cors"
                    })];
                case 1:
                    response = _a.sent();
                    return [4 /*yield*/, response.json()];
                case 2:
                    responseJson = _a.sent();
                    pigeonId = responseJson.id;
                    pigeonUrl = responseJson.url;
                    return [2 /*return*/, new Pigeon(pigeonId, pigeonUrl)];
            }
        });
    });
}
function getPigeonsCount() {
    return __awaiter(this, void 0, void 0, function () {
        var json;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0: return [4 /*yield*/, getPigeonsJson()];
                case 1:
                    json = _a.sent();
                    return [4 /*yield*/, json.pigeons.length];
                case 2: return [2 /*return*/, _a.sent()];
            }
        });
    });
}
function getRandomPigeon() {
    return __awaiter(this, void 0, void 0, function () {
        var r, _a, _b;
        return __generator(this, function (_c) {
            switch (_c.label) {
                case 0:
                    _a = getRandomInt;
                    _b = [1];
                    return [4 /*yield*/, getPigeonsCount()];
                case 1:
                    r = _a.apply(void 0, _b.concat([_c.sent()]));
                    return [2 /*return*/, getPigeonById(r)];
            }
        });
    });
}
function getRandomInt(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
/**
 * Returns the JSON of all the pigeons
 */
function getPigeonsJson() {
    return __awaiter(this, void 0, void 0, function () {
        var response, json;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0: return [4 /*yield*/, fetch(APIURL + "getPigeons", {
                        method: "GET",
                        mode: "cors"
                    })];
                case 1:
                    response = _a.sent();
                    return [4 /*yield*/, response.json()];
                case 2:
                    json = _a.sent();
                    console.log(json.pigeons.length);
                    return [2 /*return*/, json];
            }
        });
    });
}