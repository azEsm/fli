var stompClient = null

function send(){

    var userId = document.getElementById("str").value;//https://new.vk.com/nalmezel
    var userSex = document.getElementById("sex").value;
    var answer = "";


    userId=reg(userId);

    var userJson = toJson(userId, userSex);


     /function connect() {
                    var socket = new SockJS('/hello');
                    stompClient = Stomp.over(socket);
                    stompClient.connect({}, function(frame) {
                        setConnected(true);
                        console.log('Connected: ' + frame);
                        stompClient.subscribe('/topic/greetings', function(greeting){
                            showGreeting(JSON.parse(greeting.body).content);
                        });
                    });
                }

    function setConnected(connected) {
                document.getElementById('connect').disabled = connected;
                document.getElementById('disconnect').disabled = !connected;
                document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
                document.getElementById('response').innerHTML = '';
            }








    //var socket = new WebSocket("ws://echo.websocket.org");//test

    //socket.onopen = connectionOpen;
    //socket.onmessage = messageReceived;

    /*var user = [{
        photo: "images/ava.jpg",
        name: "Павел Дуров",
        url:"https://new.vk.com/durov",
        range: 4
    },
    {
        photo: "images/ava2.jpg",
        name: "Марк Цукерберг",
        url:"https://new.vk.com/mark_zuck",
        range: 2
    },
    {
        photo: "images/ava3.jpg",
        name: "Антон Генадьев",
        url:"https://new.vk.com/id161329987",
        range: 7
    },
    {
        photo: "images/ava4.jpg",
        name: "Максим Вахромеев",
        url:"https://new.vk.com/sincerity1337",
        range: 9
    },
    {
        photo: "images/ava5.jpg",
        name: "Андрей Заварин",
        url:"https://new.vk.com/id115267325",
        range: 6
    },
    {
         photo: "images/ava6.jpg",
         name: "Михаил Ефремов",
         url:"https://new.vk.com/shalom1488rebbe",
         range: 3
    }];*/




    //Json = JSON.stringify(user);

   /* function connectionOpen() {
        socket.send(Json);
    }

    function messageReceived(event) {

        var messageLog = document.getElementById("messageLog");
        answer = event.data;
        answer1(answer);
    }*/
}

function toJson(userId, userSex){
    var user = {
        id: userId,
        sex: userSex
    };

    userJson = JSON.stringify(user);

    return userJson;
 }

function reg(str){

    str=str.replace(/.+vk.com\//,"")
    return str;
}




function answer1(answer){

    var user = JSON.parse(answer);

    for (i=1; i<7; i++){

         document.getElementById("name"+i).innerHTML = user[i-1].name;
         document.getElementById("image"+i).src = user[i-1].photo;
         document.getElementById("userUrl"+i).href = user[i-1].url;
    }
}