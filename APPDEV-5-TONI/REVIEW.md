# Code comments
This file contains comments following from
a code review on the work done in AppDevCourse.

## Assignment 2
The assignment was to build a simple
call and email app that can send an
email and call a fixed phone number.
It also should attach a photo to the
email that was taken using the camera
app.

The overall structure is good, conforming with the specifications
given in `Assignment 2`.

There are some comments in the code, but I would
say that the code is already of size where it requires
more comments, as there many small custom methods.

I placed some small comments for things that could
be improvement in my opinion. Specific comments can be
found below.

### Specific comments
There were no errors in the code but there are two things I would pay attention to:

* The use of permissions.

In `ClickToContinueFragment`, an intent to the dialer is started
without asking for permission. I placed a comment there,
as well as in the `AndroidManifest.xml` where there should be
a permission item as well. This did not change since assignment 1. 

In `MainActivity`, an intent to the native camera app
is started without asking for permission. On succesful image capture,
it is (possibly) written to external storage, for which no permission
is asked either. I placed comments in the code in the relevent places.

* Passing data to fragments

The following is the same as in assignment 1.

Right now, you use a `public static String btn_text` for passing
the string that is used as the button text in `ClickToContinueFragment`.
I understand why, but you might want to look at how to properly pass
data to fragments in the future using a `Bundle`. See below.

```
SomeFragment newFragment = new SomeFragment();
Bundle args = new Bundle();
args.putString(SomeFragment.ARG_BUTTON_TEXT, 'YOUR BUTTON TEXT');
newFragment.setArguments(args);
```

The prevents that you have to create a new public class property for
every argument you want to pass. Plus, Android actually knows about
your data and can safely manage it throughout the lifecycle.
