const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const admin = require('firebase-admin');
const serviceAccount = require('./chatman-a99e6-firebase-adminsdk-e2t5v-36aaaada49.json');

const indexRouter = require('./routes/index');
const usersRouter = require('./routes/users');

const app = express();

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



app.get('/send', function(req, res) {
  admin.messaging().send(message)
  .then(response => {
    console.log('Success : ' + response)
    res.send('success');
  })
  .catch(err => {
    console.log('Error : ' + err)
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

app.listen(3000, function() {
  console.log('chatman running at port 3000');
});

module.exports = app;
