<b>Note to Reviewer</b><br><br>
Literal "YOUR API KEY" on line 42 of MainActivity.java must be changed to your own API key from themoviedb.org.<br>
<br>
App will terminate if this key is not changed or if you are not online. This termination is done via a dialog box.<br>

<br>
<b>Project Summary</b>

Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. <br><br>
In this project, you’ll build an app to allow users to discover the most popular movies playing.





Stage 1:  Main Discovery Screen, A Details View, and Settings<br><br>
<b>User Experience</b><br>
In this stage you’ll build the core experience of your movies app.


Your app will:

* Upon launch, present the user with an grid arrangement of movie posters.
* Allow your user to change sort order via a setting:<br>
  * The sort order can be by most popular, or by top rated
* Allow the user to tap on a movie poster and transition to a details screen with additional information such as:<br>
* original title
* movie poster image thumbnail
* A plot synopsis (called overview in the api)
* user rating (called vote_average in the api)
* release date
