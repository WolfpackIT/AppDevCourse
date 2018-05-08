# Code comments
This file contains comments following from
a code review on the work done in AppDevCourse.

## Assignment 1
The assignment was to build a simple
call and email app that can send an
email and call a fixed phone number.

The overall structure is good. There are no comments in the code.
For now, that is OK, since the code is so simple that it's self-explanatory.
For bigger assignments with more code, code that is not self explanatory
will need to be commented.

### Specific comments
There were no errors in the code but one
thing I would pay attention to is the use of permissions.

In `CallWPActivity`, an intent to the dialer is started
without asking for permission. I placed a comment there,
as well as in the Android manifest where there should be
a permission item as well.