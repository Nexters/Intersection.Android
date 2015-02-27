/**
 * Created by BoBinLee on 2015-02-08.
 */

var markerImgs = ["images/pin_1.png", "images/pin_2.png", "images/pin_3.png", "images/pin_4.png", "images/pin_5.png", "images/pin_6.png", "images/pin_7.png", "images/pin_8.png", "images/pin_9.png"];
var transImgs = ["images/pin_10.png", "images/pin_11.png", "images/pin_12.png"];

// 중심점 이미지
var centerImgUrl = 'http://i1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png';
// 전철 마커 이미지
// var transMarkerImgUrl = 'http://i1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png';

var chkMarkerImgs = [];

var selectedItem = {
    markers: [],
    area: null,
    center: null,
    addressName: null,
    transPlaces: null,
    transMarkers: null,
    drawClear: function () {
        // 마커 지우기
        showMarkers(this.markers, null);

        // 교통 마커 지우기
        showMarkers(this.transMarkers, null);

        if (selectedItem.area != null)
            selectedItem.area.setMap(null);
        if (selectedItem.center != null)
            selectedItem.center.setMap(null);

        chkMarkerImgs = [];
        this.markers = [];
        this.transMarkers = [];
        selectedItem.center = null;

        sendMarkerCnt();

        closeFooter();
    }
};

var areaConfig = {
    strokeWeight: 3, // 선의 두께입니다
    strokeColor: '#39DE2A', // 선의 색깔입니다
    strokeOpacity: 0.8, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
    strokeStyle: 'longdash', // 선의 스타일입니다
    fillColor: '#A2FF99', // 채우기 색깔입니다
    fillOpacity: 0.7, // 채우기 불투명도 입니다
    zIndex: -10
};

function searchIntersection() {
    var center = {lat: 0, lng: 0};
    var centerLatLng = null;
    var maxDistance = 0;

    // 초기화
    if (selectedItem.area != null)
        selectedItem.area.setMap(null);
    if (selectedItem.center != null)
        selectedItem.center.setMap(null);

    // 중간 지점 찾아내기
    for (var i = 0; i < selectedItem.markers.length; i++) {
//            circlePath.push(selectedItem.markers[i].getPosition());
        center.lat += selectedItem.markers[i].getPosition().getLat();
        center.lng += selectedItem.markers[i].getPosition().getLng();
    }
    center.lat = center.lat / selectedItem.markers.length;
    center.lng = center.lng / selectedItem.markers.length;
    centerLatLng = new daum.maps.LatLng(center.lat, center.lng);

    // 중간지점 이미지 지정
    var imageSrc = centerImgUrl,
    // 마커 이미지 url, 스프라이트 이미지를 씁니다 1, 2, 3, 4... 여러 이미지들이 있는 png
        imageSize = new daum.maps.Size(31, 35),  // 마커 이미지의 크기
        markerImage = new daum.maps.MarkerImage(imageSrc, imageSize, null);

    selectedItem.center = showMarker(centerLatLng, {image: markerImage});
    // 중심점
//        console.log("center : " + center.lat + ", " + center.lng);

    // 중심 주소 찾아내기
    searchAddrFromCoords(centerLatLng, function (status, result) {
        if (status === daum.maps.services.Status.OK) {
            selectedItem.addressName = result[0].fullName;
            // 중심 주소
//                console.log("address Name : " + selectedItem.addressName);
            // 전철역 찾기
            searchPlaces(selectedItem.addressName + " 전철", function (places) {
                // 전철 초기화
                selectedItem.transPlaces = places;
                showMarkers(selectedItem.transMarkers, null);
                selectedItem.transMarkers = [];

                var threePlaces = [];
                for (var i = 0, cnt = 0; i < places.length; i++) {
                    places[i].title = places[i].title.split(" ")[0];

                    if(places[i].title.slice(-1) != '역')
                        continue;

                    function searchTitle(places, title) {
                        for (var i = 0; i < places.length; i++) {
                            if (places[i].title == title)
                                return true;
                        }
                        return false;
                    }

                    if (!searchTitle(threePlaces, places[i].title) && cnt < 3) {
                        threePlaces.push(places[i]);
                        cnt += 1;
                    }
                }

                // show Tran
                for (var i = 0; i < threePlaces.length; i++) {
                    var latLng = new daum.maps.LatLng(threePlaces[i].latitude, threePlaces[i].longitude);

                    // 가까운 전철역 이미지 지정
                    var imageSrc = transImgs[i],
                    // 마커 이미지 url, 스프라이트 이미지를 씁니다 1, 2, 3, 4... 여러 이미지들이 있는 png
                        imageSize = new daum.maps.Size(21, 33),  // 마커 이미지의 크기
                        markerImage = new daum.maps.MarkerImage(imageSrc, imageSize, null);
                    var marker = showMarker(latLng, {image: markerImage});

                    // 교통 위치 저장
                    selectedItem.transMarkers.push(marker);
                    // 마커 항목에 click 했을때
                    // 해당 장소에 인포윈도우에 장소명을 표시합니다
                    // click 했을 때는 인포윈도우를 닫습니다
                    (function (marker, title, address) {
                        // 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
                        var infowindow = new daum.maps.InfoWindow({zIndex: 1});
                        var content = '<div style="padding:5px;z-index:1;">' + title + '<span> ' +
                            ' Like 100 </span></div>';
                        var isOpen = false;

                        daum.maps.event.addListener(marker, 'click', function () {
                            getTranslation(title, address);
                            //if (!isOpen) {
                            //    infowindow.setContent(content);
                            //    infowindow.open(map, marker);
                            //} else
                            //    infowindow.close();
                            //isOpen = !isOpen;
                        });
                    })(marker, threePlaces[i].title, threePlaces[i].address);
                }
                // 가까운 전철역 보여주기
//                    var res = "";
//                    for(var i=0; i<places.length; i++)
//                        res += places[i].title + " ";
//                    console.log("중심에 가까운 전철역 " + res);
            });
        }
    });

    // convex hull algorithm
    var convexHull = new ConvexHullGrahamScan();

    for (var i = 0; i < selectedItem.markers.length; i++) {
        var marker = selectedItem.markers[i].getPosition();
        convexHull.addPoint(marker.getLng(), marker.getLat());
    }

    // 다각형 설정
    var hullPoints = convexHull.getHull();

    //Convert to daum map latlng objects
    hullPoints = hullPoints.map(function (item) {
        return new daum.maps.LatLng(item.y, item.x);
    });
    areaConfig.path = hullPoints;

    // 지도에 표시할 선을 생성합니다
    selectedItem.area = new daum.maps.Polygon(areaConfig);

    // 지도에 선을 표시합니다
    selectedItem.area.setMap(map);
}

// Common
var collection, androidId = 1; // default id : 1
var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
var options = { //지도를 생성할 때 필요한 기본 옵션
    center: new daum.maps.LatLng(37.542504779183616, 126.97701084507932), //지도의 중심좌표.
    level: 8
    //지도의 레벨(확대, 축소 정도)
};
var map = new daum.maps.Map(container, options); //지도 생성 및 객체 리턴

// TODO Marker
function showMarker(latLng, config) {
    var marker = new daum.maps.Marker(config);
    marker.setPosition(latLng);
    marker.setMap(map);
    return marker;
}

function hiddenMarker(marker) {
    marker.setMap(null);
    return marker;
}

function showMarkers(markers, map) {
    if (markers == null)
        return;

    for (var i = 0; i < markers.length; i++) {
        if(markers[i].infowindow)
            markers[i].infowindow.close();

        if(markers[i].infowindow1)
            markers[i].infowindow1.close();

        markers[i].setMap(map);
    }
}

// TODO Place
// 주소-좌표 변환 객체를 생성합니다
var geocoder = new daum.maps.services.Geocoder();
// 장소 검색 객체를 생성합니다
var ps = new daum.maps.services.Places();

function searchAddrFromCoords(coords, callback) {
    // 좌표로 주소 정보를 요청합니다
    geocoder.coord2addr(coords, callback);
}

// 검색으로 지도 이동하기 위한 함수입니다
function placesSearch(status, data, pagination) {
    if (status === daum.maps.services.Status.OK) {

        // 지도 부드럽게 이동하기 위해 bound알아냄
        var bounds = new daum.maps.LatLngBounds();

        for (var i = 0; i < data.places.length; i++) {
            bounds.extend(new daum.maps.LatLng(data.places[i].latitude, data.places[i].longitude));
        }

        var south = bounds.getSouthWest();
        var north = bounds.getNorthEast();

        var moveLatLon = new daum.maps.LatLng((south.getLat() + north.getLat()) / 2, (south.getLng() + north.getLng()) / 2);

        map.panTo(moveLatLon);
    }
}

// 키워드 검색을 요청하는 함수입니다
function searchPlaces(keyword, callback) {
    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        console.log('잘못된 키워드');
        return false;
    }
    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
    // 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
    ps.keywordSearch(keyword, function placesSearchCB(status, data, pagination) {
        if (status === daum.maps.services.Status.OK) {
            callback(data.places);

        } else if (status === daum.maps.services.Status.ZERO_RESULT) {
            alert('검색 결과가 존재하지 않습니다.');
            return;
        } else if (status === daum.maps.services.Status.ERROR) {
            alert('검색 결과 중 오류가 발생했습니다.');
            return;
        }
    });
    return true;
}

function directSearch(searchPlace) {
    ps.keywordSearch(searchPlace, placesSearch);
}

// 내위치 마커
var imageSrc = "./images/pin_13.png",
    imageSize = new daum.maps.Size(21,33),
    markerImage = new daum.maps.MarkerImage(imageSrc, imageSize, null);

// 안드로이드 gps로 좌표받아 내 위치 이동
function moveLocation(lat, lng) {
    var moveLoc = new daum.maps.LatLng(lat, lng);
    map.panTo(moveLoc);
    var marker = showMarker(moveLoc, {image: markerImage});

    procEventMarker(marker);
}

// TODO Event
var isRightClicked = false;

// rightclick 이벤트
daum.maps.event.addListener(map, 'rightclick', function (mouseEvent) {
    var eventMarker = new daum.maps.Marker({});
    eventMarker.setPosition(mouseEvent.latLng);

    var duplicateChk = $.grep(selectedItem.markers, function(marker){

        console.log("m : " + marker.getPosition());
        console.log("m1 : " + eventMarker.getPosition());

        return marker.getPosition().getLat() == eventMarker.getPosition().getLat() && marker.getPosition().getLng() == eventMarker.getPosition().getLng();
    });

    if(duplicateChk.length >= 1)
        return ;

    //  console.log(duplicateChk);

    var curCount = 1;

    for(; curCount<=markerImgs.length ; curCount += 1){
        if(chkMarkerImgs.indexOf(curCount) == -1){
            chkMarkerImgs.push(curCount);
            break;
        }
    }

    var imageSrc = markerImgs[curCount - 1],
    // 마커 이미지 url, 스프라이트 이미지를 씁니다 1, 2, 3, 4... 여러 이미지들이 있는 png
        imageSize = new daum.maps.Size(21, 33),  // 마커 이미지의 크기
        markerImage = new daum.maps.MarkerImage(imageSrc, imageSize, null);

    // 클릭한 위도, 경도 정보를 가져옵니다
    var marker = showMarker(mouseEvent.latLng, {image: markerImage});


    procEventMarker(marker);
});

function procEventMarker(marker){
   isRightClicked = true;
   selectedItem.markers.push(marker);

   sendMarkerCnt();


  // 선택 지점 취소
    var infowindow = new daum.maps.InfoWindow({
        position: marker,
        content: '<p class="iw-delete"><img style="position: absolute; top: 36px; left: 92px; width: 15px;" src="./images/pin_option_2.png"></p>'
    });

    var infowindow1 =  new daum.maps.InfoWindow({
        position: marker,
        content: '<p class="iw-myLoc"><img style="position: absolute; top: 12px; left: 68px; width: 15px;" src="./images/pin_option_1.png"></p>'
    });

    var isOpen = false;
    var isFirst = true;

    marker.infowindow = infowindow;
    marker.infowindow1 = infowindow1;
    daum.maps.event.addListener(marker, 'click', function () {
        if (!isOpen){
            infowindow.open(map, marker);
            infowindow1.open(map, marker);
        } else {
            infowindow.close();
            infowindow1.close();
        }

        isOpen = !isOpen;

        $(".iw-delete").parent().parent().css("background", "none");
        $(".iw-delete").parent().parent().children("div").css("background", "none");
        $(".iw-delete").parent().parent().css("border", "none");


        $(".iw-myLoc").parent().parent().css("background", "none");
        $(".iw-myLoc").parent().parent().children("div").css("background", "none");
        $(".iw-myLoc").parent().parent().css("border", "none");


        if (isFirst) {
            $(".iw-delete").click(function () {
                var index = selectedItem.markers.indexOf(marker);

                if (index > -1){
                    selectedItem.markers.splice(index, 1);
                    chkMarkerImgs.splice(index, 1);
                }
                hiddenMarker(marker);
                infowindow.close();
                infowindow1.close();

                sendMarkerCnt();
            });

            $(".iw-myLoc").click(function () {
                fixMyLocation(marker.getPosition().getLat(), marker.getPosition().getLng());
                toast("내 위치가 고정되었습니다");
            });
            isFirst = !isFirst;
        }
    });
}

// click 이벤트
daum.maps.event.addListener(map, 'click', function (mouseEvent) {
    if (!isRightClicked)
        toggleToolbar();
    isRightClicked = false;
});

// bounds_changed 이벤트
daum.maps.event.addListener(map, 'bounds_changed', function () {
    onScrollChangedCallback();
    //window.DaumApp.test("test111");
});

// Transfer TODO
function sendCode(params) {
    window.DaumApp.sendCode(JSON.stringify(params));
    //getTranslation
}

function onScrollChangedCallback() {
    window.DaumApp.onScrollChangedCallback();
}

function toggleToolbar(){
    window.DaumApp.ToggleToolbar();
}

function fixMyLocation(lat, lng){
    window.DaumApp.fixMyLocation(lat, lng);
}

function getTranslation(name, address) {
    window.DaumApp.getTranslation(name, address);
}

function receiveCode(json) {
    collection = eval(json);
    alert(collection[code]);
}

function receiveAndroidId(id) {
    androidId = id;
}

function sendMarkerCnt() {
    window.DaumApp.sendMarkerCnt(selectedItem.markers.length);
}

function toast(msg) {
    window.DaumApp.toast(msg);
}

function closeFooter() {
    window.DaumApp.closeFooter();
}