# Code comments
This file contains comments following from
a code review on the work done in AppDevCourse.

## Assignment 3
The assignment was to build a simple
call and email app that can send an
email and call a fixed phone number.

For assignment 3, an added functionality
is to add a "shout fragment" that can send
a request to the Slack API to post something
in a WolfpackIT Slack channel (with Toast
feedback). E-mail sending also needs toast
feedback.

The overall structure was a little hard to follow me, mainly due to the lack fo comments.
I added some comments myself to figure out what is going on.
The code appears not to be in alignment with the requirements for `Assignment 3`

* No toast feedback is given upon sending an email
* While the the whole shouts fragment base and 
	and the recycler view etc. are there and
	correctly implemented as far as I can see, it appears
	that upon clicking a shout list item a detail activity
	is opened but nothing is done after that except for setting
	a text field with the shout text. It could obviously be something 
	I am missing (and please let me know).

### Specific comments
There were no errors in the code but there are three things I would pay attention to:

* The use of permissions.

In `ClickToContinueFragment.java`, an intent to the dialer is started
without asking for permission. In `ContactWolfpackFragment.java`, you
write to external storage as well as start a camera intent without asking
for permission. I placed comments there, as well as in the `AndroidManifest.xml` 
where there should be permission items as well (except for external storage, which
is already in the android manifest).

* Intent resolve check

In `ClickToContinueFragment.java` and `ContactWolfpackFragment.java`,
intents are started without checking if a suitable activity is available,
which might result in an exception, see e.g. the warning
at https://developer.android.com/guide/components/intents-filters.
This needs to be checked using resolveActivity(...).
