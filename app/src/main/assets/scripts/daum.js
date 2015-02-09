/**
 * Created by BoBinLee on 2015-02-08.
 */

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

        this.markers = [];
        this.transMarkers = [];
        selectedItem.center = null;
    }
};

// 중심점 이미지
var centerImgUrl = 'http://i1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png';
// 전철 마커 이미지
var transMarkerImgUrl = 'http://i1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png';

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
                    var imageSrc = transMarkerImgUrl,
                    // 마커 이미지 url, 스프라이트 이미지를 씁니다 1, 2, 3, 4... 여러 이미지들이 있는 png
                        imageSize = new daum.maps.Size(36, 37),  // 마커 이미지의 크기
                        imgOptions = {
                            spriteSize: new daum.maps.Size(36, 691), // 스프라이트 이미지의 크기
                            spriteOrigin: new daum.maps.Point(0, (i * 46) + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
                            offset: new daum.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
                        },
                        markerImage = new daum.maps.MarkerImage(imageSrc, imageSize, imgOptions);
                    var marker = showMarker(latLng, {image: markerImage});

                    // 교통 위치 저장
                    selectedItem.transMarkers.push(marker);

                    // 마커 항목에 mouseover 했을때
                    // 해당 장소에 인포윈도우에 장소명을 표시합니다
                    // mouseout 했을 때는 인포윈도우를 닫습니다
                    (function (marker, title) {
                        daum.maps.event.addListener(marker, 'mouseover', function () {
                            var content = '<div style="padding:5px;z-index:1;">' + title + '<span> Like 100 </span></div>';

                            infowindow.setContent(content);
                            infowindow.open(map, marker);
                        });

                        daum.maps.event.addListener(marker, 'mouseout', function () {
                            infowindow.close();
                        });
                    })(marker, threePlaces[i].title);
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

function showMarkers(markers, map) {
    if (markers == null)
        return;

    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}

// TODO Place
// 주소-좌표 변환 객체를 생성합니다
var geocoder = new daum.maps.services.Geocoder();
// 장소 검색 객체를 생성합니다
var ps = new daum.maps.services.Places();
// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
var infowindow = new daum.maps.InfoWindow({zIndex: 1});

function searchAddrFromCoords(coords, callback) {
    // 좌표로 주소 정보를 요청합니다
    geocoder.coord2addr(coords, callback);
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
// bounds_changed 이벤트
daum.maps.event.addListener(map, 'bounds_changed', function () {
    window.DaumApp.onScrollChangedCallback();
    window.DaumApp.test("test111");
});

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {

    var el = document.createElement('li'),
        itemStr = '<span class="markerbg marker_' + (index + 1) + '"></span>' +
            '<div class="info">' +
            '   <h5>' + places.title + '</h5>';

    if (places.newAddress) {
        itemStr += '    <span>' + places.newAddress + '</span>' +
        '   <span class="jibun gray">' + places.address + '</span>';
    } else {
        itemStr += '    <span>' + places.address + '</span>';
    }

    itemStr += '  <span class="tel">' + places.phone + '</span>' +
    '</div>';

    el.innerHTML = itemStr;
    el.className = 'item';

    return el;
}

// TODO Event
// click 이벤트
daum.maps.event.addListener(map, 'click', function (mouseEvent) {
   window.DaumApp.ToggleToolbar();
});

// rightclick 이벤트
daum.maps.event.addListener(map, 'rightclick', function (mouseEvent) {
   // 클릭한 위도, 경도 정보를 가져옵니다
   var latlng = mouseEvent.latLng;
   selectedItem.markers.push(showMarker(latlng, {}));
});