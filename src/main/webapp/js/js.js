var stompClient = null;
var users;
var fliApp = angular.module("fliApp", []);
/*var users=[
          		{
          			name: 'Web Development',
          			photo: "https://pp.vk.me/c626120/v626120705/20501/kLe17uoUAG4.jpg",
          			url:"https://vk.com/id269611705"
          		},{
          			name: 'Design',
          			photo: "https://pp.vk.me/c626120/v626120705/20501/kLe17uoUAG4.jpg",
          			url:"https://vk.com/id269611705"
          		},{
          			name: 'Integration',
          			photo: "https://pp.vk.me/c626120/v626120705/20501/kLe17uoUAG4.jpg",
          			url:"https://vk.com/id269611705"
          		},{
          			name: 'Training',
          			photo: "https://pp.vk.me/c626120/v626120705/20501/kLe17uoUAG4.jpg",
          			url:"https://vk.com/id269611705"
          		}
          	];*/

function connect() {

    var socket = new SockJS('/process');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function() {

        stompClient.subscribe('/queue/userList',function(FrontendResponse){

            showUsers(JSON.parse(FrontendResponse.body));
        });

        stompClient.subscribe('/queue/errors',function(handleException){

                    alert(handleException.body);
        });
        stompClient.subscribe('/queue/errorUserExist',function(errorUserExist){

                    alert(errorUserExist.body);
        });
    });
}

function send() {
    var userId = document.getElementById("str").value;
    var userSex = document.getElementById("sex").value;

    userId=reg(userId);

    var userJson = toJson(userId, userSex);

    stompClient.send("/fli/process", {}, userJson);
}

function reg(str){

    str=str.replace(/.+vk.com\//,"");
    return str;
}


function toJson(userId, userSex){

    var user = {
        userId: userId,
        sex: userSex
    };

    userJson = JSON.stringify(user);

    return userJson;
}

function showUsers(users) {
users = users.result;
//var message = "�������� �������� � VK!";

    showButton("visible");

    var obj=document.getElementById('usersDiv');
    users.sort(compareRate);
    disconnect();

    for (i=0; i<users.length; i++){

        /*var block = document.createElement('div');
        block.className = "user";

        obj.appendChild(block);

        var innerDiv = document.createElement('div');
        innerDiv.className = "user__inner-ava";

        block.appendChild(innerDiv);

        var image = document.createElement('img');
        image.src = users[i].photoUrl;
        image.width = "300";
        image.height = "300";

        innerDiv.appendChild(image);

        var link = document.createElement('a');
        link.href = users[i].accountUrl;
        link.className = "user__desc";
        link.target = "_blank";

        block.appendChild(link);

        var nameSpan = document.createElement('span');
        nameSpan.className = "user__title";
        nameSpan.innerHTML = users[i].name;
*/

        /*var span = document.createElement('span');
        span.className = "user__massege";
        span.innerHTML = message;
        link.appendChild(span);*/


        var block = document.createElement('article');
        block.className = "opacity user";

        obj.appendChild(block);

        var link = document.createElement('a');
        link.href = users[i].accountUrl;
        link.className = "user__link-block";
        link.target = "_blank";

        block.appendChild(link);

        var avaDiv = document.createElement('div');
        avaDiv.className = "user__wrapper-ava";

        link.appendChild(avaDiv);

        var image = document.createElement('img');
        image.src = users[i].photoUrl;
        image.width = "300";
        image.height = "300";
        image.className = "user__ava";

        avaDiv.appendChild(image);

        var infoDiv = document.createElement('div');
        infoDiv.className = "user__info";

        link.appendChild(avaDiv);

        var userInfo = document.createElement('div');
        userInfo.className = "user__info";

        link.appendChild(userInfo);

        var infoInnerDiv = document.createElement('div');
        infoInnerDiv.className = "user__info-inner";

        userInfo.appendChild(infoInnerDiv);

        var infoH2 = document.createElement('h2');
        infoH2.className = "user__name";
        infoH2.innerHTML = users[i].name;

        infoInnerDiv.appendChild(infoH2);

        var infoP = document.createElement('p');
        infoP.className = "user__hint";
        infoP.innerHTML = "Посетить страницу в VK";

        infoInnerDiv.appendChild(infoP);





        //document.getElementById("name"+i).innerHTML = users.result[i-1].name;
       // document.getElementById("image"+i).src = users.result[i-1].photoUrl;
        //document.getElementById("userUrl"+i).href = users.result[i-1].accountUrl;
    }

}



/*fliApp.controller("showUsers",function ($scope, getUsers){



                                  $scope.users = getUsers;

                                  console.log($scope.users);

                              }).service('getUsers', function () {
                                            //alert(users);

                                            return users;


                                        });*/

function setUsers(message){
    users = message;
}

function getUsers(){
    return users;
}

function showButton(visibility){
    document.getElementById('button').style.visibility = visibility;
}

function compareRate(rateA, rateB){
    return rateA.rate - rateB.rate;
}

function disconnect() {
    stompClient.disconnect();
    //setConnected(false);
    console.log("Disconnected");
}

function checkUrl(){
    url = document.getElementById("str");

    if(/[А-Яа-яёЁ]/.test(url.value)){
        alert("не корректные данные");
        url.style.border.style = "solid";
        url.style.border.color = "red";
        return false;
    }
    if(/^$/.test(url.value)){
        alert("заполниет поле ввода");
        url.style.border.style = "solid";
        url.style.border.color = "red";
        return false;
    }

    return true;


}

function bodyOnLoad(){

    connect();

    firstMainDisplay('block');
    secondMainDisplay('none');
    thirdMainDisplay('none');

}

function firstMainOnClick(){

    if(checkUrl()){
        send();
        firstMainDisplay('none');
        secondMainDisplay('block');
    }

}
function secondMainOnClick(){
    secondMainDisplay('none');
    thirdMainDisplay('block');

}

function firstMainDisplay(visibility){
    document.getElementById('firstMain').style.display = visibility;
}
function secondMainDisplay(visibility){
    document.getElementById('secondMain').style.display = visibility;
}
function thirdMainDisplay(visibility){
    document.getElementById('thirdMain').style.display = visibility;
}