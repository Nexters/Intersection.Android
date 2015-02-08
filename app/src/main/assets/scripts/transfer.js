/**
 * Created by BoBinLee on 2015-02-08.
 */
// Transfer TODO
function test() {
    searchIntersection();
    // window.DaumApp.test("test111");
}

function addTransLike(name) {
    $.ajax({
        url: "",
        data: "",
        success: function () {

        }
    });
}

function receiveAndroidId(id) {
    androidId = id;
}

function receiveCode(json) {
    collection = eval(json);
    alert(collection[code]);
}

function sendCode(params) {
    window.DaumApp.sendCode(JSON.stringify(params));
}
