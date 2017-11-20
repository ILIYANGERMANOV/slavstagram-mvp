// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNewCommentNotification = functions.database.ref('/comments/{postId}/{commentId}').onCreate(event => {
  const postId = event.params.postId;
  const commentIdRef = event.data.ref;


  const commentAuthorPromise = commentIdRef.child('author').once('value');
  const postAuthorPromise = admin.database().ref(`/posts/${postId}/author`).once('value');

  return Promise.all([postAuthorPromise, commentAuthorPromise]).then(results => {
      const postAuthor = results[0].val();
      const commentAuthor = results[1].val();

      if(postAuthor.uid === commentAuthor.uid) {
        return console.log('User ', commentAuthor.display_name, ' commented its own post.')
      }

      const notificationToken = postAuthor.notification_token;
      console.log('Resolved notificationToken = ', notificationToken);
      console.log('Resolved commment author name', commentAuthor.display_name);
      console.log('Resolved commment author photo url', commentAuthor.photo_url);

      const payload = {
        data: {
          type: "comment_on_post",
          author_name: commentAuthor.display_name,
          author_photo_url: commentAuthor.photo_url,
          post_id: postId
        }
      };

      // Send a message to the device corresponding to the provided
      // registration token.
      admin.messaging().sendToDevice(notificationToken, payload)
        .then(function(response) {
          // See the MessagingDevicesResponse reference documentation for
          // the contents of response.
          console.log("Successfully sent message:", response);
        })
        .catch(function(error) {
          console.log("Error sending message:", error);
        });
  });
});
