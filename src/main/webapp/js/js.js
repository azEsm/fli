var stompClient = null;

        /*function setConnected() {
           //alert('Connected: ' );
        }*/
        function setConnected(connected) {
            document.getElementById('connect').disabled = connected;
            document.getElementById('disconnect').disabled = !connected;
            document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
            document.getElementById('response').innerHTML = '';
        }

        function connect() {
            var socket = new SockJS('/get');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function(frame) {
                setConnected();
                console.log('Connected: ' + frame);

                stompClient.subscribe('/data/userList',function(get){
                showGreeting(JSON.parse(get.body));
                });


                //alert("lll");
               // send();
            });
        }

        /*function connect() {

                    var socket = new SockJS('/hello');
                    stompClient = Stomp.over(socket);
                    stompClient.connect({}, function(frame) {
                        setConnected(true);
                        console.log('Connected: ' + frame);
                        stompClient.subscribe('/topic/greetings', function(greeting){
                            showGreeting(JSON.parse(greeting.body).content);
                        });
                    });
                }*/



        function send() {
             var userId = document.getElementById("str").value;
             var userSex = document.getElementById("sex").value;

             userId=reg(userId);

              var userJson = toJson(userId, userSex);

              //alert(userJson);

             stompClient.send("/fli/get", {}, userJson);
                          // alert(userJson);

        }

        function reg(str){

            str=str.replace(/.+id/,"")
            return str;
        }

        function toJson(userId, userSex){
            var user = {
                id: userId,
                sex: userSex
            };

            userJson = JSON.stringify(user);

            return userJson;
         }

        function showGreeting(message) {
                       //alert("s");
           alert(message);
                      // alert("s2");

        }