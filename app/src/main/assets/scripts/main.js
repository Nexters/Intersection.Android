/**
 * Created by daehyun on 15. 1. 21..
 */
var iwContent = '<div style= "padding:5px;"> <p class="iw-delete">삭제하기</p></div>';
var container = document.getElementById('map'),
    options = {
      center:  new daum.maps.LatLng(37.575956, 126.976990),
      level:   3
    };
// 지도를 생성
var map = new daum.maps.Map(container, options);
//지도에 클릭 이벤트 생성
daum.maps.event.addListener(map, 'click', function(event){
  addMarker(event.latLng);
});

var infow;
var markers = [ ];
addMarker(new daum.maps.LatLng(33.450701, 126.570667));

//마커 생성 함수
function addMarker(position) {
  var marker = new daum.maps.Marker({
    position: position,
    title: "marker",
    clickable: true
  });
  marker.setMap(map);
  var iwPosition = new daum.maps.LatLng(position);
  var infowindow = new daum.maps.InfoWindow({
    position: iwPosition,
    content: iwContent,
    removable: true
  });
  markers.push(marker);
  daum.maps.event.addListener(marker, 'click', function () {
      infowindow.open(map, marker);
      infow = infowindow;
      $(".iw-delete").click(function(){
        deleteMarker(marker);
        test();
        infowindow.close();
      });
  });
};
function deleteMarker(marker){
  index = markers.indexOf(marker);
  markers.splice(index, index+1);
  marker.setMap(null);
}
// 마커 위치
var markerPosition = new daum.maps.LatLng(33.450701, 126.570667);
var marker = new daum.maps.Marker({
  position: markerPosition
});





//
//marker.setMap(map);
//marker.setDraggable(true);
//
//
//var iwContent = '<div style= "padding:5px;"><a href="">삭제하기</a></div>',
//    iwPosition = new daum.maps.LatLng(33.450701, 126.570667);
//
//var infowindow = new daum.maps.InfoWindow({
//  position :iwPosition,
//  content : iwContent
//});
//
//daum.maps.event.addListener(marker, 'mouseover', function() {
//  infowindow.open(map,marker);
//})
//daum.maps.event.addListener(marker, 'mouseout',function(){
//  infowindow.close();
//})

//code for testing  Webview
 function test(){
            window.DaumApp.test("test111");
}
function receiveCode(json) {
    collection = eval(json);
    alert(collection[code]);
}

function sendCode(params){
    window.DaumApp.sendCode(JSON.stringify(params));
}