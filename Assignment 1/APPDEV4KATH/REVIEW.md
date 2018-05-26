# Code comments
This file contains comments following from
a code review on the work done in AppDevCourse.

## Assignment 1
The assignment was to build a simple
call and email app that can send an
email and call a fixed phone number.

The overall structure is good, conforming with the specifications
given in `Assignment 1` (e.g. the use of separate activities that handle the fragment transactions). The code speaks for itself, since it is a small project, and no comments are needed for now. In bigger projects I would definitely add some comments, even if they seem/are redundant.

### Specific comments
There were no errors in the code but there are three things I would pay attention to:

* The use of permissions.

In `ClickToContinueFragment.java`, an intent to the dialer is started
without asking for permission. I placed a comment there,
as well as in the `AndroidManifest.xml` where there should be
a permission item as well.

* Intent resolve check

In `ClickToContinueFragment.java` intents are started
without checking if a suitable activity is available,
which might result in an exception, see e.g. the warning
at https://developer.android.com/guide/components/intents-filters.
This needs to be checked using resolveActivity(...).

* Passing data to fragments

Right now, you use a `public String btnName` for passing
the string that is used as the button text in `ClickToContinueFragment`.
I understand why, and it is not wrong, but you might want to look at how 
to properly pass data to fragments in the future using a `Bundle`, mainly
for complex, dynamic data. 

```
SomeFragment newFragment = new SomeFragment();
Bundle args = new Bundle();
args.putString(SomeFragment.ARG_BUTTON_TEXT, 'YOUR BUTTON TEXT');
newFragment.setArguments(args);
```

The prevents that you have to create a new public class property for
every argument you want to pass. Plus, Android actually knows about
your data and can safely manage it throughout the lifecycle.
