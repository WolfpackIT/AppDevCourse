# Code comments
This file contains comments following from
a code review on the work done in AppDevCourse.

## Assignment 2
The assignment was to build a simple
call and email app that can send an
email and call a fixed phone number.

The overall structure is good. There are now some 
comments in the code (more than in assignment 1 anyway), 
but this project is of a size where I would already put
some more comments in it.

### Specific comments
There were no errors in the code but one
thing I would pay attention to is the use of permissions.

Nice to see that you check for permission when opening
an intent for the camera app.

You should also ask for permission to write to external
storage. I placed a comment in `MainActivity` related to
this.

This did not change since assignment 1:

In `CallWPActivity`, an intent to the dialer is started
without asking for permission. I placed a comment there,
as well as in the Android manifest where there should be
a permission item as well.
