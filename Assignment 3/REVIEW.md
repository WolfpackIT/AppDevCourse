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

The overall structure is good. As far as I can tell, all points
except (8) have been met. However, the use of comments
did not increase. I would like to see comments being used more,
especially in something `OldMainActivity.java`.

### Specific comments

#### Permissions

First of all, in (8), the use of permissions,
I've seen being properly used in `AndroidManifest.xml`, so that's good.
However, I've seen the following:

* No permission is asked for writing to external storage in `OldMainActivity.java`.
* No permission is asked for opening the dialer in `CallWPActivity.java`.

#### Unimplemented method with wrong default value / actually unused file
The method `availableInternetConnection(...)` in `SendSlackMessage.java`
is not implemented and will return a default value of `null`, which will be 
interpreted as `false` unless explicitly checked using `== false`.

However, after looking further I saw that you're using
`SlackMessageSender.java`instead `SendSlackMessage.java`,
which does implement it. The comment doesn't apply anymore.
However, please also remove unused files such as `SendSlackMessage.java`.

#### Exception thrown
in `OldMainActivity.java`, right before the method `OnAttach(...)`, there is the comment:

`// TODO: Exception thrown when this is called. FIX IT.`.

If is still throwing this exception, then I'm guessing the
the `OnFragmentInteractionListener` has not been attached.
Look into this (unless you forgot to remove the comment 
and no exception is thrown?). It isn't clear for me, because
there are no further comments. TODO's are ok, as long as you
explain them well (also for yourself).