var stompClient = null;
//var users;
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

        stompClient.subscribe('/data/userList',function(FrontendResponse){

            showUsers(JSON.parse(FrontendResponse.body));
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

    str=str.replace(/.+id/,"")
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

        var block = document.createElement('div');
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
        link.appendChild(nameSpan);

        /*var span = document.createElement('span');
        span.className = "user__massege";
        span.innerHTML = message;
        link.appendChild(span);*/




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