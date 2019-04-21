const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const admin = require('firebase-admin');
const serviceAccount = require('./chatman-a99e6-firebase-adminsdk-e2t5v-36aaaada49.json');

const SerialPort = require('serialport');
const Readline = require('@serialport/parser-readline');
const port = new SerialPort('/dev/cu.usbserial-14510', { baudRate: 9600 });
const parser = port.pipe(new Readline({ delimiter: '\n' }));

const indexRouter = require('./routes/index');
const usersRouter = require('./routes/users');

const app = express();

let communicateSerial = function(message) {
  if (message == 'kunci pintu') {
    port.write('1', (err) => {
    if (err) {
      return console.log('Error on write: ', err.message);
    }
    console.log('Pintu terkunci');
  });
  }
  else if (message == 'buka pintu') {
    port.write('0', (err) => {
    if (err) {
      return console.log('Error on write: ', err.message);
    }
    console.log('Pintu terbuka');
  });
  }
}

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://chatman-a99e6.firebaseio.com/'
});

let registrationToken = "dhoxHmhVQzo:APA91bGt3zNCA0OdxZ4PV2HCiWqFRSaPftZr6-rKB53O_ZSnTiBDB_YV8vSbaCjXLYzy_pkhd2VBppm6ohXcePhhPC-h4jXA2IwPADqmauhpN4_6eWEAmiBiPPw2WOjsP3CSIW6i569w";
let message = {
  notification: {
    title: 'Hi :*',
    body: 'Pakabar ?'
  },
  token: registrationToken
}

const db = admin.database();
let message_db = db.ref('message');
let chatrooms_db = db.ref('chatroom');
var idSender = '';

chatrooms_db.on('child_added', chatroom => {
  console.log('chatroom added');
  chatroom.ref.child('messages').on('child_added', chat => {
  let chatref = message_db.child(chat.val());
  console.log(chat.val());
  chatref.on('value', chatSnap => {
    console.log(chatSnap.val());
    if (chatSnap.val()) {
      if (chatSnap.child('idReceiver').val() == 'BOT_TOKEN') {
        communicateSerial(chatSnap.child('message').val().toLowerCase());
        idSender = chatSnap.child('idSender').val();
        console.log(idSender);
      }
      let message = {
        notification: {
          title: chatSnap.child('nameSender').val(),
          body: chatSnap.child('message').val()
        },
        token: chatSnap.child('idReceiver').val()
      }
      admin.messaging().send(message)
        .then(response => {
          console.log('Success : ' + response)
        })
        .catch(err => {
          console.log('Error : ' + err)
        });
    }
  });
  console.log('chat added');
  });
});

// Read the port data
port.on("open", () => {
  console.log('serial port open');
});
parser.on('data', data =>{
  console.log('got word from arduino:', data);
  let tanggal = new Date();
  if (data == 2) {
    if (idSender) {
      message_db.push().set({
        date: {
          date: tanggal.getDate(),
          day: tanggal.getDay(),
          hours: tanggal.getHours(),
          minutes: tanggal.getMinutes(),
          month: tanggal.getMonth(),
          seconds: tanggal.getSeconds(),
          time: tanggal.getTime(),
          timezoneOffset: tanggal.getTimezoneOffset(),
          year: tanggal.getFullYear()
        },
        idReceiver: idSender,
        idSender: "BOT_TOKEN",
        message: "Ada tamu masuk",
        nameSender: "ChatMan Bot"
      });
    }
    console.log("Guest masuk");
  }
});

app.get('/send', function(req, res) {
  admin.messaging().send(message)
  .then(response => {
    // console.log('Success : ' + response)
    res.send('success');
  })
  .catch(err => {
    // console.log('Error : ' + err)
    res.send('error');
  });
});

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

app.listen(3001, function() {
  console.log('chatman running at port 3000');
});



module.exports = app;
