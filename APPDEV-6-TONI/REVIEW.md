# Code comments
This file contains comments following from
a code review on the work done in AppDevCourse.

## Assignment 3
The assignment was to build a simple
app with the following requirements:

* (1) When clicking on one of the Shout List items, a background task ensures the shout is send through the API of Slack and posted in our random channel [background tasks]
* (2) Inform the user using toasts of succes/fail of this task
* (3) Sending the message to the slack API is a simple POST call implementation [connectivity]
* (4) Choose your own shouts like 'I want coffee NOW'
* (5) When email is succesfully sent (check the callback by the email intent) A notification (in the notificationbar) has to be posted saying 'Sent an picture to Rene!' [notifications]
* (6) Change the colorscheme of the app to your choice BUT using the correct styling and themeing options.
* (7) Use the debugger to place breakpoints when testing the app. play around with it!
* (8) Don't forget the appropriate permiissions

The overall structure is good and all requirements but (8) and (2) have been met as far as I can tell. The use of comments did somewhat
increase, but they're still sporadic. 
I would like to see comments being used more,
especially in something `ContactWolfpackFragment.java`.

### Specific comments

#### Permissions

First of all,  (8), the use of permissions,
I've not seen being properly used:
* The permissions    
	* `<uses-permission android:name="android.permission.CALL_PHONE" />` 
	* `<uses-permission android:name="android.permission.CAMERA" />` are missing from `AndroidManifest.xml`.
* No permission is asked for opening the dialer in `ClickToContinueFragment.java`.
* No permission is asked for writing to external storage or for opening the camera in `ContactWolfpackFragment.java`.

#### Missing toast
As far as I can tell, no toast will be displayed upon success or error
of the shout api POST call. I see the Toast class import, so perhaps
you just forgot?

#### Structure (+)
I like the clear structure of the shout part of this assignment.
You have a ReycyclerView with a different class for each list item,
which makes it very easy to follow. The API call code in ShoutFragment
is also nicely separated and clear. The only thing I'd change is add some
comments to that part (ShoutFragment).