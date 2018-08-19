const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();


// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

// exports.greetingMessage = functions.https.onRequest((request, response) => {
//   console.log("Hello!")
//  response.send("Hello World!");
// });
exports.updateXP = functions.database.ref('/items/{todoID}/pomodoros')
.onUpdate((change, context) => {
  const before= change.before.val();
  const after= change.after.val();
  if(after<before){
  return admin.database().ref('/xp').once('value')
    .then(snapshot =>{
    var xp = snapshot.val();
    xp=parseInt(xp)+10;    
    return xp;
    })
    .then(result =>{
      const xp_count= parseInt(result);      
      return admin.database().ref('/items').parent.update({"xp": xp_count});
    })
    .catch(error =>{
      console.log(error);
    });
  }else{
    return null;
  }
  });  